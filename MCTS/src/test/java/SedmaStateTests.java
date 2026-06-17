import sedma.GameState;
import sedma.SedmaState;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SedmaStateTests {
    private SedmaState createState() {
        SedmaState s = new SedmaState();
        return s;
    }

    @Test
    void cloneProducesDistinctObject() {
        SedmaState original = createState();
        GameState gsCloneGeneric = original.clone();
        assertTrue(gsCloneGeneric instanceof SedmaState);
        SedmaState clone = (SedmaState) gsCloneGeneric;

        // objektová odlišnosť
        assertNotSame(original, clone);
    }

    @Test
    void allFeaturesAreTheSameInClone() {
        SedmaState original = createState();
        SedmaState clone = (SedmaState) original.clone();

        assertEquals(original.toString(), clone.toString(),
                "toString() is different for clone and the original");
        assertEquals(original.getCurHand(), clone.getCurHand(),
                "getCurHand() is different for clone and the original");
        assertEquals(original.getPlayedCards(), clone.getPlayedCards(),
                "getPlayedCards() is different for clone and the original");
        assertEquals(original.legalMoves().getFirst(), clone.legalMoves().getFirst(),
                "legalMoves() is different for clone and the original");
        assertEquals(original.ended(), clone.ended(),
                "ended() is different for clone and the original");
        assertEquals(original.getScore(0), clone.getScore(0),
                "getScore(0) is different for clone and the original");
        assertEquals(original.getScore(1), clone.getScore(1),
                "getScore(1) is different for clone and the original");
    }

    @Test
    void cloneAndOriginalDontAffectEachOther() {
        SedmaState original = createState();
        SedmaState clone = (SedmaState) original.clone();

        clone.applyMove(clone.legalMoves().getFirst());
        clone.applyMove(clone.legalMoves().getFirst());

        assertNotEquals(original.getCurHand(), clone.getCurHand(),
                "getCurHand() is the same for clone and the original");
        assertNotEquals(original.getPlayedCards(), clone.getPlayedCards(),
                "getPlayedCards() is the same for clone and the original");
        assertNotEquals(original.legalMoves(), clone.legalMoves(),
                "legalMoves() is the same for clone and the original");
        assertNotEquals(original.toString(), clone.toString(),
                "toString() is the same for clone and the original");
    }

    @Test
    void shuffleUnknownsProducesDistinctObject() {
        SedmaState original = createState();
        GameState gsCloneGeneric = original.shuffleUnknowns();
        assertTrue(gsCloneGeneric instanceof SedmaState);
        SedmaState clone = (SedmaState) gsCloneGeneric;

        // objektová odlišnosť
        assertNotSame(original, clone);
    }

    @Test
    void knownFeaturesAreTheSameInShuffleUnknowns() {
        SedmaState original = createState();
        SedmaState clone = (SedmaState) original.clone();

        assertEquals(original.toString(), clone.toString(),
                "toString() is different for shuffled and the original");
        assertEquals(original.getCurHand(), clone.getCurHand(),
                "getCurHand() is different for shuffled and the original");
        assertEquals(original.getPlayedCards(), clone.getPlayedCards(),
                "getPlayedCards() is different for shuffled and the original");
        assertEquals(original.legalMoves().getFirst(), clone.legalMoves().getFirst(),
                "legalMoves() is different for shuffled and the original");
        assertEquals(original.ended(), clone.ended(),
                "ended() is different for shuffled and the original");
        assertEquals(original.getScore(0), clone.getScore(0),
                "getScore(0) is different for shuffled and the original");
        assertEquals(original.getScore(1), clone.getScore(1),
                "getScore(1) is different for shuffled and the original");
    }

    @Test
    void shuffleUnknownsAndOriginalDontAffectEachOther() {
        SedmaState original = createState();
        SedmaState clone = (SedmaState) original.clone();

        clone.applyMove(clone.legalMoves().getFirst());

        assertNotEquals(original.getCurHand(), clone.getCurHand(),
                "getCurHand() is the same for shuffled and the original");
        assertNotEquals(original.getPlayedCards(), clone.getPlayedCards(),
                "getPlayedCards() is the same for shuffled and the original");
        assertNotEquals(original.legalMoves(), clone.legalMoves(),
                "legalMoves() is the same for shuffled and the original");
        assertNotEquals(original.toString(), clone.toString(),
                "toString() is the same for shuffled and the original");
    }
}
