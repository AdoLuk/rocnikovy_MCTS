package player;

import sedma.GameState;
import sedma.Move;

public interface Player {
    void makeMove(GameState gs, long timeMillis);
    String getStrategyName();
}
