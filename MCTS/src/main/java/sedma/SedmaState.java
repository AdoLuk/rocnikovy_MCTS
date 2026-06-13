package sedma;

import java.util.ArrayList;
import java.util.Arrays;
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
        points = Arrays.asList(0, 0);
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
        return new SedmaState(List.copyOf(drawPile),
                              playedCards,
                              curPlayer,
                              hands.get(0),
                              hands.get(1),
                              List.copyOf(points));
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

        curPlayer = (curPlayer + 1) % 2;
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
}
