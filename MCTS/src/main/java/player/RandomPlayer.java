package player;

import sedma.GameState;
import sedma.Move;

import java.util.List;
import java.util.Random;

public class RandomPlayer implements Player {
    private Random rand;
    public RandomPlayer() {
        rand = new Random();
    }
    public RandomPlayer(long seed) {
        rand = new Random(seed);
    }

    public void makeMove(GameState gs, long timeMillis) {
        List<Move> legalMoves = gs.legalMoves();
        if (legalMoves.isEmpty()) {
            throw new IllegalStateException("No valid moves found.");
        }
        int index = rand.nextInt(legalMoves.size());
        if (!gs.applyMove(legalMoves.get(index))) {
            throw new IllegalStateException("Couldn't apply legal move.");
        }
    }

    @Override
    public String getStrategyName() {
        return "RandomPlayer";
    }
}
