package sedma;

import java.util.ArrayList;
import java.util.List;

public class Card {
    public enum Suits {
        hearts,
        diamonds,
        clubs,
        spades
    }

    public enum Ranks {
        VII,
        VIII,
        IX,
        X,
        Jack,
        Queen,
        King,
        Ace
    }

    private Suits suit;
    private Ranks rank;

    public Card(Suits suit, Ranks rank) {
        this.suit = suit;
        this.rank = rank;
    }

    Suits getSuit() { return suit; }
    Ranks getRank() { return rank; }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Card)) return false;
        Card otherCard = (Card) other;
        return (this.suit == otherCard.getSuit()) && (this.rank == otherCard.getRank());
    }

    public static List<Card> getDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suits s : Suits.values()) {
            for (Ranks r : Ranks.values()) {
                deck.add(new Card(s, r));
            }
        }
        return deck;
    }
}
