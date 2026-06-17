package mcts;

import sedma.GameState;
import sedma.Move;

import java.text.DecimalFormat;
import java.util.Collection;

public class UCTSelection implements SelectionPolicy {
    public Node selectChild(Collection<Node> children, GameState gs) {
        double bestScore = 0;
        Node bestChild = null;
//        System.out.print("Parent: " + children.iterator().next().getParent());
//        System.out.print(" Children: ");
//        DecimalFormat df = new DecimalFormat("#0.###");
        for (Node child : children) {
            double score = (double) child.getWins() / child.getVisits() +
                    0.2 * Math.sqrt(Math.log((double) (child.getParent().getVisits() + 1) / (child.getVisits() + 1)));
//            System.out.print(child + "_" + df.format(score) + " | ");
            if (score >= bestScore) {
                bestScore = score;
                bestChild = child;
            }
        }
//        System.out.println("\nChose: " + bestChild);
//        System.out.flush();
        if (bestChild == null) {
            System.out.println("children size: " + children.size());
            System.out.println(gs.toString());
            throw new IllegalStateException("couldn't find a best child");
        }
        if(!gs.applyMove(new Move(gs.getCurrentPlayer(), bestChild.getAction()))) {
            throw new IllegalStateException("Couldn't apply legal move for child selection.");
        }
        return bestChild;
    }
}
