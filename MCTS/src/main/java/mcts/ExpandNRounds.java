package mcts;

import sedma.GameState;

public class ExpandNRounds implements ExpansionPolicy {
    private ExpandUnlessEnded exp = new ExpandUnlessEnded();
    private final int k;
    public ExpandNRounds(int n) {
        k = n;
    }

    @Override
    public Node from(Node n, GameState gs) {
        if (n.getNewRoundAncestors() >= k) return n;
        return exp.from(n, gs);
    }
}
