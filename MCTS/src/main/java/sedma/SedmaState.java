package sedma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sedma.Card.getDeck;
import static sedma.Move.FOLD;

public class SedmaState implements GameState {
    private List<Card> drawPile;
    private List<Card> playedCards;
    private List<List<Card>> hand;
    private List<Integer> points;
    private int curPlayer, startingPlayer;

    public SedmaState() {
        List<Card> deck = getDeck();
        Collections.shuffle(deck);
        List<Card> hand0 = new ArrayList<>();
        List<Card> hand1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            hand0.add(deck.removeLast());
            hand1.add(deck.removeLast());
        }
        new SedmaState(deck, new ArrayList<>(), 0, hand0, hand1, List.of(0, 0));
    }

    private SedmaState(List<Card> drawPile,
                      List<Card> playedCards,
                      int curPlayer,
                      List<Card> hand0,
                      List<Card> hand1,
                      List<Integer> points) {

        hand = new ArrayList<>();
        hand.add(hand0); // player0's hand
        hand.add(hand1); // player1's hand
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
                              hand.get(0),
                              hand.get(1),
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
            if (curPlayer != startingPlayer ||
                    playedCards.isEmpty() ||
                    overtakes(hand.get(curPlayer).get(i))) {
                legal.add(new Move(curPlayer, i));
            }
        }
        return legal;
    }

    private boolean overtakes(Card c) {
        return playedCards.isEmpty() ||
               c.getRank() == playedCards.getFirst().getRank() ||
               c.getRank() == Card.Ranks.VII;
    }

    @Override
    public boolean applyMove(Move move) {
        if (curPlayer != move.getPlayer() || // not your turn
                ((move.action() < 0 || move.action() > 3) && move.action() != FOLD)) { // invalid action
            return false;
        }

        if (move.action() == FOLD) { // starting player decided not to overtake his opponent (or cannot)
            if (curPlayer != startingPlayer || playedCards.isEmpty()) return false; // player cannot fold
            int takingPlayer = 0;
            int countPoints = 0;
            for (int i = 0; i < playedCards.size(); i++) {
                if (playedCards.get(i).getRank() == Card.Ranks.Ace ||
                        playedCards.get(i).getRank() == Card.Ranks.X) {
                    countPoints += 10;
                } // counting valuable cards

                if (overtakes(playedCards.get(i))) {
                    takingPlayer = (i + startingPlayer) % 2;
                } // overtaking
            }

            points.set(takingPlayer, points.get(takingPlayer) + countPoints);
            startingPlayer = takingPlayer;
            for (int i = 0; i < playedCards.size(); i++) {
                if (drawPile.isEmpty()) break;
                for (int j = 0; j < 4; j++) { // players alternate in drawing cards
                    if (hand.get(i % 2).get(j) == null) {
                        hand.get(i % 2).set(j, drawPile.removeLast());
                        break;
                    }
                }
            }
            playedCards.clear();
            return true;
        }

        List<Card> curHand = hand.get(curPlayer);
        if (curHand.get(move.action()) == null) {
            return false; // invalid action
        }

        if (playedCards.isEmpty() || // starting player has to choose first card
                curPlayer != startingPlayer || // the other player has to play any card
                overtakes(curHand.get(move.action()))) { // if it is starting player's turn the only legal move is overtaking
            playedCards.add(curHand.get(move.action()));
            curHand.set(move.action(), null);
        } else {
            return false;
        }

        curPlayer = (curPlayer + 1) % 2;
        return true;
    }
}
