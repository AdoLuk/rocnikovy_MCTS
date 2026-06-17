package player;

import mcts.*;
import sedma.GameState;
import sedma.Move;

public class RoundBasedSedmaPlayer implements Player {
    private final int nOfTrees, nOfRounds;
    private Player rolloutPlayer;
    public RoundBasedSedmaPlayer(int nOfTrees, int nOfRounds, Player rolloutPlayer) {
        this.nOfTrees = nOfTrees;
        this.nOfRounds = nOfRounds;
        this.rolloutPlayer = rolloutPlayer;
    }

    @Override
    public void makeMove(GameState gs, long timeMillis) {
        PIMC gameTree = new PIMC(gs.clone(),
                new UCTSelection(),
                new ExpandNRounds(nOfRounds),
                new SedmaNRoundsSimulator(nOfRounds, rolloutPlayer),
                nOfTrees);
        gameTree.runTreeSearch(timeMillis);
        gs.applyMove(new Move(gs.getCurrentPlayer(), gameTree.bestAction()));
    }

    @Override
    public String getStrategyName() {
        return "RoundBasedSedmaPlayer-" + nOfTrees + "-" + nOfRounds + "-(" + rolloutPlayer.getStrategyName() + ")";
    }
}
