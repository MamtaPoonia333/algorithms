import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApproximationAlgorithms {
    private ApproximationAlgorithms() {
    }

    public static Set<Integer> vertexCoverApprox(int n, List<int[]> edges) {
        Set<Integer> cover = new HashSet<>();
        boolean[] removed = new boolean[edges.size()];
        int remaining = edges.size();
        for (int i = 0; i < edges.size() && remaining > 0; i++) {
            if (removed[i]) {
                continue;
            }
            int u = edges.get(i)[0];
            int v = edges.get(i)[1];
            cover.add(u);
            cover.add(v);
            for (int j = 0; j < edges.size(); j++) {
                if (!removed[j]) {
                    int a = edges.get(j)[0];
                    int b = edges.get(j)[1];
                    if (a == u || a == v || b == u || b == v) {
                        removed[j] = true;
                        remaining--;
                    }
                }
            }
        }
        return cover;
    }

    public static List<Integer> setCoverApprox(Set<Integer> universe, List<Set<Integer>> sets) {
        Set<Integer> covered = new HashSet<>();
        List<Integer> chosen = new ArrayList<>();
        while (!covered.containsAll(universe)) {
            int bestIdx = -1;
            int bestGain = -1;
            for (int i = 0; i < sets.size(); i++) {
                int gain = 0;
                for (int x : sets.get(i)) {
                    if (universe.contains(x) && !covered.contains(x)) {
                        gain++;
                    }
                }
                if (gain > bestGain) {
                    bestGain = gain;
                    bestIdx = i;
                }
            }
            if (bestIdx == -1 || bestGain == 0) {
                break;
            }
            chosen.add(bestIdx);
            covered.addAll(sets.get(bestIdx));
        }
        return chosen;
    }

    public static class TspApproxResult {
        public final List<Integer> tour;
        public final double cost;

        public TspApproxResult(List<Integer> tour, double cost) {
            this.tour = tour;
            this.cost = cost;
        }
    }

    public static TspApproxResult tsp2Approx(double[][] dist) {
        int n = dist.length;
        int[] parent = primMst(dist);
        List<List<Integer>> tree = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            tree.add(new ArrayList<>());
        }
        for (int v = 1; v < n; v++) {
            int p = parent[v];
            tree.get(p).add(v);
            tree.get(v).add(p);
        }
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[n];
        dfsPreorder(0, -1, tree, visited, order);
        order.add(0);
        double cost = 0.0;
        for (int i = 0; i < order.size() - 1; i++) {
            cost += dist[order.get(i)][order.get(i + 1)];
        }
        return new TspApproxResult(order, cost);
    }

    private static int[] primMst(double[][] dist) {
        int n = dist.length;
        double[] key = new double[n];
        boolean[] inMst = new boolean[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            key[i] = Double.POSITIVE_INFINITY;
            parent[i] = -1;
        }
        key[0] = 0;
        for (int i = 0; i < n; i++) {
            int u = -1;
            for (int v = 0; v < n; v++) {
                if (!inMst[v] && (u == -1 || key[v] < key[u])) {
                    u = v;
                }
            }
            inMst[u] = true;
            for (int v = 0; v < n; v++) {
                if (!inMst[v] && dist[u][v] < key[v]) {
                    key[v] = dist[u][v];
                    parent[v] = u;
                }
            }
        }
        return parent;
    }

    private static void dfsPreorder(int u, int p, List<List<Integer>> tree, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        order.add(u);
        for (int v : tree.get(u)) {
            if (v != p) {
                dfsPreorder(v, u, tree, visited, order);
            }
        }
    }

    public static List<List<Integer>> binPackingFirstFit(int[] items, int capacity) {
        List<List<Integer>> bins = new ArrayList<>();
        List<Integer> remaining = new ArrayList<>();
        for (int item : items) {
            boolean placed = false;
            for (int i = 0; i < bins.size(); i++) {
                if (remaining.get(i) >= item) {
                    bins.get(i).add(item);
                    remaining.set(i, remaining.get(i) - item);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                List<Integer> bin = new ArrayList<>();
                bin.add(item);
                bins.add(bin);
                remaining.add(capacity - item);
            }
        }
        return bins;
    }

    public static class KnapsackApproxResult {
        public final int totalValue;
        public final int totalWeight;
        public final List<Integer> chosenIndices;

        public KnapsackApproxResult(int totalValue, int totalWeight, List<Integer> chosenIndices) {
            this.totalValue = totalValue;
            this.totalWeight = totalWeight;
            this.chosenIndices = chosenIndices;
        }
    }

    public static KnapsackApproxResult knapsackGreedyApprox(int[] weights, int[] values, int capacity) {
        class Item {
            int idx;
            int w;
            int v;

            Item(int idx, int w, int v) {
                this.idx = idx;
                this.w = w;
                this.v = v;
            }
        }
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < weights.length; i++) {
            items.add(new Item(i, weights[i], values[i]));
        }
        items.sort((a, b) -> Double.compare((double) b.v / b.w, (double) a.v / a.w));
        int totalW = 0;
        int totalV = 0;
        List<Integer> chosen = new ArrayList<>();
        for (Item it : items) {
            if (totalW + it.w <= capacity) {
                totalW += it.w;
                totalV += it.v;
                chosen.add(it.idx);
            }
        }
        return new KnapsackApproxResult(totalV, totalW, chosen);
    }
}
