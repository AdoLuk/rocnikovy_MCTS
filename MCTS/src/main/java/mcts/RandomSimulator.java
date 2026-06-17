package mcts;

import player.HeuristicSedmaPlayer;
import player.RandomPlayer;
import sedma.GameOfSedma;
import sedma.GameState;

public class RandomSimulator implements RolloutPolicy {
    public void simulate(GameState gs, Node node) {
        GameOfSedma game = new GameOfSedma(new RandomPlayer(), new RandomPlayer(), gs);
        int res = game.playGame(0);
        node.addVisits(1);
        boolean win = (node.isMyTurn() && res == gs.getCurrentPlayer() * -2 + 1) ||
                      (!node.isMyTurn() && res == gs.getCurrentPlayer() * 2 - 1);
        if (win) node.addWins(1);
    }
}
