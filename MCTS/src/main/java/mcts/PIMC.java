package mcts;

import sedma.GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PIMC {
    private final int nOfTrees;
    private GameState gs;
    private List<Root> roots;

    public PIMC(GameState gs, SelectionPolicy selector, ExpansionPolicy expand, RolloutPolicy simulator, int nOfTrees) {
        this.gs = gs;
        this.roots = new ArrayList<>();
        this.nOfTrees = nOfTrees;

        for (int i = 0; i < nOfTrees; i++) {
            roots.add(new Root(gs.shuffleUnknowns(), selector, simulator, expand));
        }
    }

    public void runTreeSearch(long timeMillis) {
        long end = System.currentTimeMillis() + timeMillis;
        int iter = 0;
        while (System.currentTimeMillis() < end) {
            for (int i = 0; i < nOfTrees; i++) {
                roots.get(i).iterate();
//                System.out.println();
//                System.out.flush();
            }
            if (gs.legalMoves().size() == 1) return;
            iter++;
        }
//        System.out.println("Done " + iter + " iterations in " + (System.currentTimeMillis() - end + timeMillis) + "ms.");
//        System.out.flush();
    }

    public int bestAction() {
        SelectionPolicy finalSelector = new FinalSelector();
        Map<Integer, Integer> actions = new HashMap<>();
        int bestAction = -1, cnt = 0;
        for (Node root : roots) {
            int act = root.selectBestAction(finalSelector, gs);
            actions.put(act, actions.getOrDefault(act, 0) + 1);
            if (actions.get(act) > cnt) {
                cnt = actions.get(act);
                bestAction = act;
            }
        }
//        for (int key : actions.keySet()) {
//            System.out.println(key + "a: " + actions.get(key));
//        }
        return bestAction;
    }
}
