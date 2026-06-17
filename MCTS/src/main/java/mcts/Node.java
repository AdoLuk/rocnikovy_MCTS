package mcts;

import sedma.GameState;
import sedma.Move;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> children;
    private int visits, wins;
    private List<Move> legalMoves;
    private Node parent;
    private boolean isMyTurn;
    private int action;
    private int newRoundAncestors;

    public Node(List<Move> legalMoves, Node parent, int action) {
        this.parent = parent;
        this.legalMoves = legalMoves;
        children = new ArrayList<>();
        visits = 0;
        wins = 0;
        this.action = action;
        if (parent == null) { // only in root
            this.isMyTurn = true; //unimportant, Overridden in root
            this.newRoundAncestors = 0;
        } else {
            isMyTurn = !parent.isMyTurn();
            this.newRoundAncestors = parent.newRoundAncestors + (action == Move.FOLD ? 1 : 0);
        }

//        System.out.println("Creating Node: action=" + action
//                + ", parent=" + (parent == null ? "null" : "exists")
//                + ", isMyTurn=" + this.isMyTurn()
//                + ", legalMoves.size=" + (legalMoves == null ? 0 : legalMoves.size())
//                + ", children.size=" + children.size()
//                + ", visits=" + visits + ", wins=" + wins);
//
//        if (legalMoves != null && !legalMoves.isEmpty()) {
//            System.out.print(" legalMoves actions=[");
//            for (Move m : legalMoves) System.out.print(m.getAction() + ",");
//            System.out.println("]");
//        } else {
//            System.out.println(" no legalMoves actions");
//        }
    }

    public Node selectNode(SelectionPolicy strategy, GameState gs) {
        if (!legalMoves.isEmpty()) {
            return this;
        }
        if (children.isEmpty()) {
            return parent;
        }
        return strategy.selectChild(children, gs).selectNode(strategy, gs);
    }

    public int selectBestAction(SelectionPolicy finSel, GameState gs) {
        return finSel.selectChild(children, gs).getAction();
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<Move> getUnusedMoves() {
        return legalMoves;
    }

    public int getNewRoundAncestors() { return newRoundAncestors; }
    public int getAction() { return action; }
    public int getVisits() { return visits; }
    public int getWins() {
        if (parent.isMyTurn()) return wins; //my parent is asking for wins
        return visits - wins; // if he wants to make best move for the opponent
    }
    public Node getParent() { return parent; }
    public boolean isMyTurn() { return isMyTurn; }

    public void addVisits(int n) {
        visits += n;
        parent.addVisits(n);
    }
    public void addWins(int n) {
        wins += n;
        parent.addWins(n);
    }

    @Override
    public String toString() {
        return "<" + visits + "v|" + wins + "w|" + action + "a|" + newRoundAncestors + "r|" + isMyTurn + ">";
    }
}
