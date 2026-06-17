package mcts;

import sedma.GameState;

import java.util.Collection;
import java.util.Map;

public class FinalSelector implements SelectionPolicy {
    @Override
    public Node selectChild(Collection<Node> children, GameState gs) {
        int mostVisits = -1;
        Node mostVisited = null;
        for (Node node : children) {
            if (mostVisits <= node.getVisits()) {
                mostVisits = node.getVisits();
                mostVisited = node;
            } else if (mostVisits == node.getVisits() &&
                        mostVisited.getWins() < node.getWins()) {
                mostVisited = node;
            }
        }
        if (mostVisited == null) {
            System.out.println("Children size: " + children.size());
            System.out.println(gs);
        }
        return mostVisited;
    }
}
