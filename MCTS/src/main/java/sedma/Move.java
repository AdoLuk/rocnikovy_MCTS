package sedma;

public class Move {
    private int player;
    private int cardIdx;

    public static final int FOLD = -1;

    public Move(int player, int action) {
        if (player != 0 && player != 1) {
            throw new IllegalArgumentException("Player must be 0 or 1");
        }
        if ((action < 0 || action > 3) && action != FOLD) {
            throw new IllegalArgumentException("Invalid action");
        }
        this.player = player;
        this.cardIdx = action;
    }

    public int getPlayer() {
        return player;
    }

    public int getAction() {
        return cardIdx;
    }

    @Override
    public String toString() {
        return "Move{pl=" + player + ", act=" + cardIdx + '}';
    }
}
