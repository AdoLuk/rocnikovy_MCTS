package sedma;

import player.*;

import java.util.List;

public class GameOfSedma {
    private static void compare(Player p1, Player p2, int iterations, long timeMillis) {
        System.out.println("\nComparing: " + p1.getStrategyName() + " vs " + p2.getStrategyName() + " . . .");

        GameOfSedma g1 = new GameOfSedma(p1, p2);
        GameOfSedma g2 = new GameOfSedma(p2, p1);
        int p1wins = 0, p2wins = 0, ties = 0;
        for (int i = 0; i < iterations; i++) {
            switch (g1.playGame(timeMillis)) {
                case -1:
                    p2wins++;
                    break;
                case 0:
                    ties++;
                    break;
                case 1:
                    p1wins++;
                    break;
            }
            switch (g2.playGame(timeMillis)) {
                case -1:
                    p1wins++;
                    break;
                case 0:
                    ties++;
                    break;
                case 1:
                    p2wins++;
                    break;
            }
//            System.out.println(p1wins + " " + p2wins + " " + ties);
        }
        System.out.println("Games played: " + iterations * 2);
        System.out.println(p1.getStrategyName() + " wins: " + p1wins);
        System.out.println(p2.getStrategyName() + " wins: " + p2wins);
        System.out.println("Ties: " + ties);
    }

    private static void testFirstMoveAdvantage(Player p1, Player p2, int iterations, long timeMillis) {
        System.out.println("Testing first move advantage: " + p1.getStrategyName() + " vs " + p2.getStrategyName() + " . . .");

        GameOfSedma g1 = new GameOfSedma(p1, p2);
        GameOfSedma g2 = new GameOfSedma(p2, p1);
        int as1wins = 0, as2wins = 0;
        for (int i = 0; i < iterations * 2; i++) {
            if (g1.playGame(timeMillis) == 1) {
                as1wins++;
            }
            if (g2.playGame(timeMillis) == -1) {
                as2wins++;
            }
        }
        System.out.println("Games played: " + iterations * 2);
        System.out.println(p1.getStrategyName() + "'s wins over " + p2.getStrategyName() + ":");
        System.out.println("    - as 1st player: " + as1wins);
        System.out.println("    - as 2nd player: " + as2wins);
    }

    public static void main(String[] args) {
        Player p1 = new RandomPlayer();
        Player p2 = new HeuristicSedmaPlayer();
        compare(p1, p2, 50000, 0);
    }

    private List<Player> players;
    private GameState gs;
    public GameOfSedma(Player player0, Player player1) {
        players = List.of(player0, player1);
        gs = new SedmaState();
    }
    public GameOfSedma(Player player0, Player player1, GameState gs) {
        players = List.of(player0, player1);
        this.gs = gs;
    }

    public int playGame(long timeMillis) {
        GameState gs = new SedmaState();
        while (!gs.ended()) {
            players.get(gs.getCurrentPlayer()).makeMove(gs, timeMillis);
        }
        if (gs.getScore(0) > gs.getScore(1)) {
            return 1;
        } else if (gs.getScore(0) == gs.getScore(1)) {
            return 0;
        } else {
            return -1;
        }
    }
}
