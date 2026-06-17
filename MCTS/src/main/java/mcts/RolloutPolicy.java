package mcts;

import sedma.GameState;

public interface RolloutPolicy {
    // Returns true if the player won this simulation
    void simulate(GameState gs, Node node);
}
