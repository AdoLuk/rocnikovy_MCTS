package mcts;

import sedma.GameState;

import java.util.Collection;

public interface SelectionPolicy {
    Node selectChild(Collection<Node> children, GameState gs);
}
