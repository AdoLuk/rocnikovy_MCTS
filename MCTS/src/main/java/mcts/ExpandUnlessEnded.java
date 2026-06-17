package mcts;

import sedma.GameState;

public class ExpandUnlessEnded implements ExpansionPolicy {
    @Override
    public Node from(Node n, GameState gs) {
        if (gs.ended()) return n;
        if (n.getUnusedMoves().isEmpty()) {
            throw new IllegalStateException("Node is alredy fully expanded.");
        }
        if (!gs.applyMove(n.getUnusedMoves().getLast())) {
            throw new IllegalArgumentException("Wrong gamestate passed to the function.");
        }
        Node ch = new Node(gs.legalMoves(), n, n.getUnusedMoves().removeLast().getAction());
        n.getChildren().add(ch);
        return ch;
    }
}
