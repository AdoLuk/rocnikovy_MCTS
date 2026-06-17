package mcts;

import sedma.GameState;
import sedma.Move;

import java.util.Collection;

public class ExploreSelection implements SelectionPolicy {
    public Node selectChild(Collection<Node> children, GameState gs) {
        int bestScore = 0;
        Node bestChild = null;
        for (Node child : children) {
            int score = child.getWins() / child.getVisits() +
                    (int) Math.sqrt(Math.log((child.getParent().getVisits() + 1) / (child.getVisits() + 1)));
            if (score >= bestScore) {
                bestScore = score;
                bestChild = child;
            }
        }
        if (bestChild == null) {
            System.out.println("children size: " + children.size());
            System.out.println(gs);
        }
        if(!gs.applyMove(new Move(gs.getCurrentPlayer(), bestChild.getAction()))) {
            throw new IllegalStateException("Couldn't apply legal move for child selection.");
        }
        return bestChild;
    }
}
