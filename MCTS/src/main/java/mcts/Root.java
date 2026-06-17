package mcts;

import sedma.GameOfSedma;
import sedma.GameState;

public class Root extends Node {
    private int visits, wins;
    private GameState gs;
    private SelectionPolicy selector;
    private RolloutPolicy simulator;
    private ExpansionPolicy expand;

    public Root(GameState gs, SelectionPolicy selector, RolloutPolicy simulator, ExpansionPolicy expand) {
        super(gs.legalMoves(), null, 42);
        this.gs = gs;
        this.selector = selector;
        this.expand = expand;
        this.simulator = simulator;
    }

    public void iterate() {
        GameState gsClone = gs.clone();
        Node n = this.selectNode(selector, gsClone);
        n = expand.from(n, gsClone);
        if (simulator instanceof SedmaNRoundsSimulator nRnds) {
            nRnds.updateOldScores(gs.getScore(0), gs.getScore(1));
        }
        simulator.simulate(gsClone, n);
    }

    @Override
    public int getVisits() { return visits; }
    @Override
    public int getWins() { return wins; }
    @Override
    public Node getParent() { throw new UnsupportedOperationException("Root does not have parent."); }
    @Override
    public boolean isMyTurn() { return true; }
    @Override
    public void addVisits(int n) { visits += n; }
    @Override
    public void addWins(int n) { wins += n; }
}
