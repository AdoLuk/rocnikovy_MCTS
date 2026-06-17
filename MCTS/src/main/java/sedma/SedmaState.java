package sedma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sedma.Card.getDeck;
import static sedma.Move.FOLD;

public class SedmaState implements GameState {
    private List<Card> drawPile;
    private List<Card> playedCards;
    private List<List<Card>> hands;
    private List<Integer> points;
    private int curPlayer, startingPlayer;

    public SedmaState() {
        drawPile = getDeck();
        Collections.shuffle(drawPile);
        playedCards = new ArrayList<>();
        hands = List.of(new ArrayList<>(), new ArrayList<>());
        for (int i = 0; i < 4; i++) {
            hands.get(0).add(drawPile.removeLast());
            hands.get(1).add(drawPile.removeLast());
        }
        points = new ArrayList<>();
        points.add(0); points.add(0);
        curPlayer = 0;
        startingPlayer = 0;
    }

    private SedmaState(List<Card> drawPile,
                      List<Card> playedCards,
                      int curPlayer,
                      List<Card> hand0,
                      List<Card> hand1,
                      List<Integer> points) {

        hands = new ArrayList<>();
        hands.add(hand0); // player0's hand
        hands.add(hand1); // player1's hand
        this.points = points;

        this.drawPile = drawPile;
        this.playedCards = playedCards;
        this.startingPlayer = (curPlayer + playedCards.size()) % 2; // calculated from number of played cards

        this.curPlayer = curPlayer;
    }

    @Override
    public GameState clone() {
        List<Card> dpCopy = new ArrayList<>();
        dpCopy.addAll(drawPile);
        List<Card> pcCopy = new ArrayList<>();
        pcCopy.addAll(playedCards);
        List<Integer> ptsCopy = new ArrayList<>();
        ptsCopy.addAll(points);
        List<Card> h0Copy = new ArrayList<>();
        h0Copy.addAll(hands.get(0));
        List<Card> h1Copy = new ArrayList<>();
        h1Copy.addAll(hands.get(1));
        return new SedmaState(dpCopy,
                              pcCopy,
                              curPlayer,
                              h0Copy,
                              h1Copy,
                              ptsCopy);
    }

    public GameState shuffleUnknowns() {
        List<Card> unknowns = new ArrayList<>();
        unknowns.addAll(drawPile);
        int notNulls = 0;
        for (Card c : hands.get(1 - curPlayer)) {
            if (c != null) {
                notNulls++;
                unknowns.add(c);
            }
        }
        Collections.shuffle(unknowns);
        List<Card> unknownHand = new ArrayList<>();
        for (int i = 0; i < notNulls; i++) unknownHand.add(unknowns.removeLast());
        for (int i = 0; i < 4 - notNulls; i++) unknownHand.add(null);
        List<Card> hand0, hand1;
        if (curPlayer == 0) {
            hand0 = hands.get(0);
            hand1 = unknownHand;
        } else {
            hand0 = unknownHand;
            hand1 = hands.get(1);
        }
        List<Card> pcCopy = new ArrayList<>();
        pcCopy.addAll(playedCards);
        List<Integer> ptsCopy = new ArrayList<>();
        ptsCopy.addAll(points);
        return new SedmaState(
                unknowns,
                pcCopy,
                curPlayer,
                hand0,
                hand1,
                ptsCopy);
    }

    @Override
    public int getCurrentPlayer() {
        return curPlayer;
    }

    @Override
    public List<Move> legalMoves() {
        List<Move> legal = new ArrayList<>();
        if (!playedCards.isEmpty() && startingPlayer == curPlayer) {
            legal.add(new Move(curPlayer, FOLD));
        }
        for (int i = 0; i < 4; i++) {
            if(hands.get(curPlayer).get(i) == null) continue;
            if (curPlayer != startingPlayer ||
                    playedCards.isEmpty() ||
                    overtakes(hands.get(curPlayer).get(i))) {
                legal.add(new Move(curPlayer, i));
            }
        }
        return legal;
    }

    public boolean overtakes(Card c) {
        return playedCards.isEmpty() ||
               c.getRank() == playedCards.getFirst().getRank() ||
               c.getRank() == Card.Ranks.VII;
    }

    @Override
    public boolean applyMove(Move move) {
        if (curPlayer != move.getPlayer() || // not your turn
                ((move.getAction() < 0 || move.getAction() > 3) && move.getAction() != FOLD)) { // invalid action
            return false;
        }

        if (move.getAction() == FOLD) { // starting player decided not to overtake his opponent (or cannot)
            if (curPlayer != startingPlayer || playedCards.isEmpty()) return false; // player cannot fold
            int takingPlayer = 0;
            int countPoints = 0;
            for (int i = 0; i < playedCards.size(); i++) {
                if (playedCards.get(i).getRank() == Card.Ranks.A ||
                        playedCards.get(i).getRank() == Card.Ranks.X) {
                    countPoints += 10;
                } // counting valuable cards

                if (overtakes(playedCards.get(i))) {
                    takingPlayer = (i + startingPlayer) % 2;
                } // overtaking
            }

            points.set(takingPlayer, points.get(takingPlayer) + countPoints);
            startingPlayer = takingPlayer;
            curPlayer = startingPlayer;
            for (int i = 0; i < playedCards.size(); i++) {
                if (drawPile.isEmpty()) break;
                for (int j = 0; j < 4; j++) { // players alternate in drawing cards
                    if (hands.get(i % 2).get(j) == null) {
                        hands.get(i % 2).set(j, drawPile.removeLast());
                        break;
                    }
                }
            }
            playedCards.clear();
            return true;
        }

        List<Card> curHand = hands.get(curPlayer);
        if (curHand.get(move.getAction()) == null) {
            return false; // invalid action
        }

        if (playedCards.isEmpty() || // starting player has to choose first card
                curPlayer != startingPlayer || // the other player has to play any card
                overtakes(curHand.get(move.getAction()))) { // if it is starting player's turn the only legal move is overtaking
            playedCards.add(curHand.get(move.getAction()));
            curHand.set(move.getAction(), null);
        } else {
            return false;
        }

        curPlayer = 1 - curPlayer;
        return true;
    }

    public boolean ended() {
        for (int i = 0; i < 4; i++) {
            if (hands.get(0).get(i) != null) return false;
            if (hands.get(1).get(i) != null) return false;
        }
        return drawPile.isEmpty() && playedCards.isEmpty();
    }

    public int getScore(int player) {
        return points.get(player);
    }

    public List<Card> getPlayedCards() {
        return playedCards;
    }

    public List<Card> getCurHand() {
        return hands.get(curPlayer);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current Player: " + curPlayer + "\n");
        sb.append("Played cards:" + "\n");
        for (Card card : playedCards) {
            sb.append(card + "\n");
        }
        sb.append("Your cards:");
        for (Card card : hands.get(curPlayer)) {
            if (card == null) {
                sb.append("        |");
            } else {
                sb.append(" " + card + " |");
            }
        }
        sb.append("\n");
        sb.append("Opponent's cards:");
        for (Card card : hands.get(1 - curPlayer)) {
            if (card == null) {
                sb.append("        |");
            } else {
                sb.append(" " + card + " |");
            }
        }
        sb.append("\n");
        sb.append("Ended: " + ended() + "\n");
        return sb.toString();
    }
}
