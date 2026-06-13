package player;

import sedma.Card;
import sedma.GameState;
import sedma.Move;
import sedma.SedmaState;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

public class HumanSedmaPlayer implements Player {
    Scanner sc = new Scanner(System.in);

    public void makeMove(GameState gs, long timeMillis) {
        if (!(gs instanceof SedmaState)) {
            throw new IllegalStateException("This player can only play a game of Sedma.");
        }
        SedmaState ss = (SedmaState) gs;
        List<Card> playedCards = ss.getPlayedCards();
        List<Card> myHand = ss.getCurHand();

        if (playedCards.size() <= 1) {
            System.out.println("-------------");
            System.out.println("| NEW ROUND |");
            System.out.println("-------------");
        }

        System.out.println("Played cards:");
        for (Card card : playedCards) {
            System.out.println(card);
        }
        System.out.print("\nYour cards:");
        for (Card card : myHand) {
            if (card == null) {
                System.out.print("        |");
            } else {
                System.out.print(" " + card + " |");
            }
        }
        System.out.print("\n\n");

        while (true) {
            System.out.println("Choose your move (type 1-4 for cards or " + (Move.FOLD + 1) + " for folding): ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid action.");
                sc.next();
                continue;
            }
            int action = sc.nextInt();
            action--;
            if (action != Move.FOLD && (action < 0 || action > 3)) {
                System.out.println("Invalid action.");
                continue;
            }

            Move move = new Move(ss.getCurrentPlayer(), action);
            if (!gs.applyMove(move)) {
                System.out.println("Invalid move!");
                System.out.print("Your legal moves are:");
                List<Move> legalMoves = ss.legalMoves();
                if (legalMoves.isEmpty()) {
                    System.out.print("/n");
                    throw new IllegalStateException("No valid moves found.");
                }
                for (Move m : legalMoves) {
                    System.out.print(" " + (m.getAction() + 1));
                }
                System.out.print("\n\n");
                continue;
            }
            break;
        }
    }

    @Override
    public String getStrategyName() {
        return "HumanPlayer";
    }
}
