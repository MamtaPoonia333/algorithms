import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MinimumSpanningTree {
    private MinimumSpanningTree() {
    }

    public static class Edge {
        public final int u;
        public final int v;
        public final long w;

        public Edge(int u, int v, long w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    public static class MSTResult {
        public final long totalWeight;
        public final List<Edge> edges;

        public MSTResult(long totalWeight, List<Edge> edges) {
            this.totalWeight = totalWeight;
            this.edges = edges;
        }
    }

    private static class DSU {
        private final int[] parent;
        private final int[] rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) {
                return false;
            }
            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
            return true;
        }
    }

    public static MSTResult kruskal(int n, List<Edge> edges) {
        edges.sort((a, b) -> Long.compare(a.w, b.w));
        DSU dsu = new DSU(n);
        List<Edge> mst = new ArrayList<>();
        long total = 0;
        for (Edge e : edges) {
            if (dsu.union(e.u, e.v)) {
                mst.add(e);
                total += e.w;
                if (mst.size() == n - 1) {
                    break;
                }
            }
        }
        return new MSTResult(total, mst);
    }

    public static MSTResult prim(int n, List<List<Edge>> adj, int start) {
        boolean[] inMst = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> Long.compare(a.w, b.w));
        List<Edge> mst = new ArrayList<>();
        long total = 0;
        inMst[start] = true;
        for (Edge e : adj.get(start)) {
            pq.add(e);
        }
        while (!pq.isEmpty() && mst.size() < n - 1) {
            Edge e = pq.poll();
            int next = inMst[e.u] ? e.v : e.u;
            if (inMst[next]) {
                continue;
            }
            inMst[next] = true;
            mst.add(e);
            total += e.w;
            for (Edge ne : adj.get(next)) {
                if (!inMst[ne.u] || !inMst[ne.v]) {
                    pq.add(ne);
                }
            }
        }
        return new MSTResult(total, mst);
    }

    public static MSTResult boruvka(int n, List<Edge> edges) {
        DSU dsu = new DSU(n);
        List<Edge> mst = new ArrayList<>();
        long total = 0;
        int components = n;
        while (components > 1) {
            Edge[] cheapest = new Edge[n];
            for (Edge e : edges) {
                int a = dsu.find(e.u);
                int b = dsu.find(e.v);
                if (a == b) {
                    continue;
                }
                if (cheapest[a] == null || e.w < cheapest[a].w) {
                    cheapest[a] = e;
                }
                if (cheapest[b] == null || e.w < cheapest[b].w) {
                    cheapest[b] = e;
                }
            }
            for (int i = 0; i < n; i++) {
                Edge e = cheapest[i];
                if (e != null) {
                    int a = dsu.find(e.u);
                    int b = dsu.find(e.v);
                    if (a != b && dsu.union(a, b)) {
                        mst.add(e);
                        total += e.w;
                        components--;
                    }
                }
            }
        }
        return new MSTResult(total, mst);
    }
}
