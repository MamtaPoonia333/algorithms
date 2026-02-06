import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

public class ShortestPath {
    private ShortestPath() {
    }

    public static class Edge {
        public final int to;
        public final long weight;

        public Edge(int to, long weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public static class BellmanFordResult {
        public final long[] dist;
        public final boolean hasNegativeCycle;

        public BellmanFordResult(long[] dist, boolean hasNegativeCycle) {
            this.dist = dist;
            this.hasNegativeCycle = hasNegativeCycle;
        }
    }

    private static final long INF = Long.MAX_VALUE / 4;

    public static long[] dijkstra(int n, List<List<Edge>> adj, int source) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[source] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        pq.add(new long[]{0, source});
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1];
            if (d != dist[u]) {
                continue;
            }
            for (Edge e : adj.get(u)) {
                if (dist[u] + e.weight < dist[e.to]) {
                    dist[e.to] = dist[u] + e.weight;
                    pq.add(new long[]{dist[e.to], e.to});
                }
            }
        }
        return dist;
    }

    public static BellmanFordResult bellmanFord(int n, List<List<Edge>> adj, int source) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[source] = 0;
        List<int[]> edges = new ArrayList<>();
        List<Long> weights = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                edges.add(new int[]{u, e.to});
                weights.add(e.weight);
            }
        }
        for (int i = 0; i < n - 1; i++) {
            boolean changed = false;
            for (int idx = 0; idx < edges.size(); idx++) {
                int u = edges.get(idx)[0];
                int v = edges.get(idx)[1];
                long w = weights.get(idx);
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    changed = true;
                }
            }
            if (!changed) {
                break;
            }
        }
        boolean negCycle = false;
        for (int idx = 0; idx < edges.size(); idx++) {
            int u = edges.get(idx)[0];
            int v = edges.get(idx)[1];
            long w = weights.get(idx);
            if (dist[u] != INF && dist[u] + w < dist[v]) {
                negCycle = true;
                break;
            }
        }
        return new BellmanFordResult(dist, negCycle);
    }

    public static long[] shortestPathInDag(int n, List<List<Edge>> adj, int source) {
        int[] order = topologicalSort(n, adj);
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[source] = 0;
        for (int u : order) {
            if (dist[u] == INF) {
                continue;
            }
            for (Edge e : adj.get(u)) {
                if (dist[u] + e.weight < dist[e.to]) {
                    dist[e.to] = dist[u] + e.weight;
                }
            }
        }
        return dist;
    }

    private static int[] topologicalSort(int n, List<List<Edge>> adj) {
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                indeg[e.to]++;
            }
        }
        Deque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                dq.add(i);
            }
        }
        int[] order = new int[n];
        int idx = 0;
        while (!dq.isEmpty()) {
            int u = dq.poll();
            order[idx++] = u;
            for (Edge e : adj.get(u)) {
                indeg[e.to]--;
                if (indeg[e.to] == 0) {
                    dq.add(e.to);
                }
            }
        }
        return order;
    }

    public static long[] zeroOneBfs(int n, List<List<Edge>> adj, int source) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        Deque<Integer> dq = new ArrayDeque<>();
        dist[source] = 0;
        dq.add(source);
        while (!dq.isEmpty()) {
            int u = dq.pollFirst();
            for (Edge e : adj.get(u)) {
                long nd = dist[u] + e.weight;
                if (nd < dist[e.to]) {
                    dist[e.to] = nd;
                    if (e.weight == 0) {
                        dq.addFirst(e.to);
                    } else {
                        dq.addLast(e.to);
                    }
                }
            }
        }
        return dist;
    }

    public static long[][] floydWarshall(long[][] dist) {
        int n = dist.length;
        long[][] d = new long[n][n];
        for (int i = 0; i < n; i++) {
            d[i] = Arrays.copyOf(dist[i], n);
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (d[i][k] == INF) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    if (d[k][j] == INF) {
                        continue;
                    }
                    long nd = d[i][k] + d[k][j];
                    if (nd < d[i][j]) {
                        d[i][j] = nd;
                    }
                }
            }
        }
        return d;
    }

    public static long[][] johnsonAllPairs(int n, List<List<Edge>> adj) {
        List<List<Edge>> extended = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            extended.add(new ArrayList<>());
        }
        for (int u = 0; u < n; u++) {
            extended.get(u).addAll(adj.get(u));
        }
        for (int v = 0; v < n; v++) {
            extended.get(n).add(new Edge(v, 0));
        }
        BellmanFordResult bf = bellmanFord(n + 1, extended, n);
        if (bf.hasNegativeCycle) {
            throw new IllegalArgumentException("Graph contains a negative cycle");
        }
        long[] h = bf.dist;
        List<List<Edge>> reweighted = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            reweighted.add(new ArrayList<>());
        }
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                long nw = e.weight + h[u] - h[e.to];
                reweighted.get(u).add(new Edge(e.to, nw));
            }
        }
        long[][] result = new long[n][n];
        for (int u = 0; u < n; u++) {
            long[] d = dijkstra(n, reweighted, u);
            for (int v = 0; v < n; v++) {
                if (d[v] >= INF / 2) {
                    result[u][v] = INF;
                } else {
                    result[u][v] = d[v] - h[u] + h[v];
                }
            }
        }
        return result;
    }
}
