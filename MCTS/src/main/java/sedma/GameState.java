package sedma;

import java.util.List;

public interface GameState extends Cloneable {
    GameState clone();
    GameState shuffleUnknowns();
    int getCurrentPlayer();
    List<Move> legalMoves();
    boolean applyMove(Move m);
    boolean ended();
    int getScore(int player);
}