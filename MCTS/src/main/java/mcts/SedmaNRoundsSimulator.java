package mcts;

import player.Player;
import player.RandomHeuristicSedmaPlayer;
import player.RandomPlayer;
import sedma.GameState;
import sedma.SedmaState;

import java.util.List;
import java.util.Random;

public class SedmaNRoundsSimulator implements RolloutPolicy {
    private Random rand = new Random();
    private final int k;
    private List<Integer> oldScores;
    private Player p;
    public SedmaNRoundsSimulator(int n, Player rolloutPlayer) {
        this.k = n;
        this.oldScores = List.of(0, 0);
        this.p = rolloutPlayer;
    }

    public void updateOldScores(int old0, int old1) {
        this.oldScores = List.of(old0, old1);
    }

    private int cnt = 0, x = 0, y = 0;
    @Override
    public void simulate(GameState gs, Node node) {
//        System.out.println("from node: " + node);
        if (!(gs instanceof SedmaState ss)) {
            throw new IllegalArgumentException("SedmaNRoundsSimulator can only simulate rounds in the game Sedma");
        }
        int me = (node.isMyTurn() ? ss.getCurrentPlayer() : 1 - ss.getCurrentPlayer());
        int mineOld = oldScores.get(me);
        int opponentsOld = oldScores.get(1 - me);
        int round = node.getNewRoundAncestors();
        while (round < k && !ss.ended()) {
//            if (cnt < y) {
//                System.out.println(ss);
//            }
            p.makeMove(ss, 0);
            if (ss.getPlayedCards().isEmpty()) round++;
        }
        node.addVisits(30);
        if (ss.getScore(me) > mineOld) {
//            if (cnt < x) {
//                System.out.println((22 + (ss.getScore(me) - mineOld) / 10) + "w/30v");
//            }
            node.addWins(22 + (ss.getScore(me) - mineOld) / 10);
        } else if (ss.getScore(1 - me) > opponentsOld) {
//            if (cnt < x) {
//                System.out.println((8 - (ss.getScore(1 - me) - opponentsOld) / 10) + "w/30v");
//            }
            node.addWins(8 - (ss.getScore(1 - me) - opponentsOld) / 10);
        } else {
//            if (cnt < x) {
//                System.out.println(15 + "w/30v");
//            }
            node.addWins(22);
        }
        cnt++;
        System.out.flush();
    }
}
