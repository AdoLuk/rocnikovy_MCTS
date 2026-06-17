package player;

import sedma.GameState;
import sedma.Move;

import java.util.List;
import java.util.Random;

public class RandomHeuristicSedmaPlayer implements Player {
    private Random rand;
    public RandomHeuristicSedmaPlayer() {
        rand = new Random();
    }
    public RandomHeuristicSedmaPlayer(long seed) {
        rand = new Random(seed);
    }

    public void makeMove(GameState gs, long timeMillis) {
//        System.out.println(gs);
        List<Move> legalMoves = gs.legalMoves();
        if (legalMoves.isEmpty()) {
            System.out.println(gs);
            throw new IllegalStateException("No valid moves found.");
        }

        Player pRand = new RandomPlayer();
        Player pHeur = new HeuristicSedmaPlayer();
        if (rand.nextBoolean()) {
            pRand.makeMove(gs, 0);
        } else {
            pHeur.makeMove(gs, 0);
        }
    }

    @Override
    public String getStrategyName() {
        return "RandomPlayer";
    }
}
