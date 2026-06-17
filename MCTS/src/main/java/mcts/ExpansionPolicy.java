package mcts;

import sedma.GameState;

public interface ExpansionPolicy {
    Node from(Node n, GameState gs);
}
