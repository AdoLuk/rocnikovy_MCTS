package player;

import sedma.Card;
import sedma.GameState;
import sedma.Move;
import sedma.SedmaState;

import java.util.List;

public class HeuristicSedmaPlayer implements Player {
    RandomPlayer randomPlayer1;
    public HeuristicSedmaPlayer() {
        randomPlayer1 = new RandomPlayer();
    }

    private void makeRandomMove(GameState gs) {
        randomPlayer1.makeMove(gs, 0);
    }

    public void makeMove(GameState gs, long timeMillis) {
        if (!(gs instanceof SedmaState)) {
            throw new IllegalStateException("This player can only play a game of Sedma.");
        }
        SedmaState ss = (SedmaState) gs;
        List<Move> legalMoves = ss.legalMoves();
        if (legalMoves.isEmpty()) {
            throw new IllegalStateException("No valid moves found.");
        }

        Move move = null;
        List<Card> playedCards = ss.getPlayedCards();
        Card.Ranks firstRank = null;
        if (!playedCards.isEmpty()) {
            firstRank = playedCards.getFirst().getRank();
        }
        List<Card> myHand = ss.getCurHand();

        if (playedCards.size() % 2 == 0 &&
                !playedCards.isEmpty() &&
                !ss.overtakes(playedCards.getLast())) {
            for (Card card : playedCards) {
                if ((card.getRank() == Card.Ranks.X || card.getRank() == Card.Ranks.A) &&
                        ss.applyMove(new Move(ss.getCurrentPlayer(), Move.FOLD))) {
                    return;
                }
            }
        }

        if(firstRank == Card.Ranks.X || firstRank == Card.Ranks.A) {
            for (Move m : legalMoves) {
                if (m.getAction() == Move.FOLD) continue;
                Card.Ranks rank = myHand.get(m.getAction()).getRank();
                if (rank == firstRank || rank == Card.Ranks.VII) { // we want to overtake those points
                    move = m;
                    break;
                }
            }
        }
        if (move == null) {
            boolean canFold = false;
            for (Move m : legalMoves) {
                if (m.getAction() == Move.FOLD) {
                    canFold = true;
                    continue;
                }
                Card.Ranks rank = myHand.get(m.getAction()).getRank();
                if (rank != Card.Ranks.A && rank != Card.Ranks.X) { // we don't want to waste X, Ace or VII
                    if (move != null && rank == Card.Ranks.VII) continue;
                    move = m;
                    if (rank == firstRank && rank != Card.Ranks.VII) break; // if it overtakes we lock the option
                }
            }
            if (canFold && (move == null || myHand.get(move.getAction()).getRank() == Card.Ranks.VII)) {
                move = new Move(ss.getCurrentPlayer(), Move.FOLD);
            }
        }

        if (move == null) { // we didn't find our optimal move
            makeRandomMove(gs);
            return;
        }

        if (!gs.applyMove(move)) {
            throw new IllegalStateException("Couldn't apply legal move.");
        }
    }

    @Override
    public String getStrategyName() {
        return "HeuristicPlayer";
    }
}
