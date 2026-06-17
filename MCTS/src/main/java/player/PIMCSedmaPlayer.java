package player;

import mcts.ExpandUnlessEnded;
import mcts.PIMC;
import mcts.HeuristicSimulator;
import mcts.UCTSelection;
import sedma.GameState;
import sedma.Move;

public class PIMCSedmaPlayer implements Player {
    private final int nOfTrees;
    public PIMCSedmaPlayer(int nOfTrees) {
        this.nOfTrees = nOfTrees;
    }

    @Override
    public void makeMove(GameState gs, long timeMillis) {
        PIMC gameTree = new PIMC(gs.clone(),
                new UCTSelection(),
                new ExpandUnlessEnded(),
                new HeuristicSimulator(),
                nOfTrees);
        gameTree.runTreeSearch(timeMillis);
        gs.applyMove(new Move(gs.getCurrentPlayer(), gameTree.bestAction()));
    }

    @Override
    public String getStrategyName() {
        return "PIMCSedmaPlayer-" + nOfTrees;
    }
}
