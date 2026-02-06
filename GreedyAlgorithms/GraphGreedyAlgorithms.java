import java.util.Arrays;
import java.util.List;

public class GraphGreedyAlgorithms {
    private GraphGreedyAlgorithms() {
    }

    public static long mstKruskal(int n, List<MinimumSpanningTree.Edge> edges) {
        return MinimumSpanningTree.kruskal(n, edges).totalWeight;
    }

    public static long mstPrim(int n, List<List<MinimumSpanningTree.Edge>> adj, int start) {
        return MinimumSpanningTree.prim(n, adj, start).totalWeight;
    }

    public static long[] shortestPathDijkstra(int n, List<List<ShortestPath.Edge>> adj, int source) {
        return ShortestPath.dijkstra(n, adj, source);
    }

    public static int[] greedyColoring(int n, List<List<Integer>> adj) {
        int[] color = new int[n];
        Arrays.fill(color, -1);
        boolean[] used = new boolean[n];
        for (int u = 0; u < n; u++) {
            Arrays.fill(used, false);
            for (int v : adj.get(u)) {
                if (color[v] != -1) {
                    used[color[v]] = true;
                }
            }
            int c = 0;
            while (c < n && used[c]) {
                c++;
            }
            color[u] = c;
        }
        return color;
    }
}
