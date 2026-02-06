import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class FlowNetworks {
    private FlowNetworks() {
    }

    public static class FlowEdge {
        int to;
        int rev;
        long cap;

        FlowEdge(int to, int rev, long cap) {
            this.to = to;
            this.rev = rev;
            this.cap = cap;
        }
    }

    public static class FlowNetwork {
        final int n;
        final List<List<FlowEdge>> adj;

        public FlowNetwork(int n) {
            this.n = n;
            adj = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
            }
        }

        public void addEdge(int u, int v, long cap) {
            FlowEdge a = new FlowEdge(v, adj.get(v).size(), cap);
            FlowEdge b = new FlowEdge(u, adj.get(u).size(), 0);
            adj.get(u).add(a);
            adj.get(v).add(b);
        }
    }

    public static long fordFulkerson(FlowNetwork g, int s, int t) {
        long flow = 0;
        while (true) {
            boolean[] visited = new boolean[g.n];
            long pushed = dfsAugment(g, s, t, Long.MAX_VALUE, visited);
            if (pushed == 0) {
                break;
            }
            flow += pushed;
        }
        return flow;
    }

    private static long dfsAugment(FlowNetwork g, int u, int t, long f, boolean[] visited) {
        if (u == t) {
            return f;
        }
        visited[u] = true;
        for (FlowEdge e : g.adj.get(u)) {
            if (!visited[e.to] && e.cap > 0) {
                long pushed = dfsAugment(g, e.to, t, Math.min(f, e.cap), visited);
                if (pushed > 0) {
                    e.cap -= pushed;
                    g.adj.get(e.to).get(e.rev).cap += pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }

    public static long edmondsKarp(FlowNetwork g, int s, int t) {
        long flow = 0;
        int[] parentV = new int[g.n];
        int[] parentE = new int[g.n];
        while (true) {
            Arrays.fill(parentV, -1);
            Deque<Integer> q = new ArrayDeque<>();
            q.add(s);
            parentV[s] = s;
            while (!q.isEmpty() && parentV[t] == -1) {
                int u = q.poll();
                for (int i = 0; i < g.adj.get(u).size(); i++) {
                    FlowEdge e = g.adj.get(u).get(i);
                    if (parentV[e.to] == -1 && e.cap > 0) {
                        parentV[e.to] = u;
                        parentE[e.to] = i;
                        q.add(e.to);
                    }
                }
            }
            if (parentV[t] == -1) {
                break;
            }
            long aug = Long.MAX_VALUE;
            for (int v = t; v != s; v = parentV[v]) {
                FlowEdge e = g.adj.get(parentV[v]).get(parentE[v]);
                aug = Math.min(aug, e.cap);
            }
            for (int v = t; v != s; v = parentV[v]) {
                FlowEdge e = g.adj.get(parentV[v]).get(parentE[v]);
                e.cap -= aug;
                g.adj.get(e.to).get(e.rev).cap += aug;
            }
            flow += aug;
        }
        return flow;
    }

    public static long dinic(FlowNetwork g, int s, int t) {
        long flow = 0;
        int[] level = new int[g.n];
        while (bfsLevel(g, s, t, level)) {
            int[] it = new int[g.n];
            long pushed;
            while ((pushed = dfsDinic(g, s, t, Long.MAX_VALUE, level, it)) > 0) {
                flow += pushed;
            }
        }
        return flow;
    }

    private static boolean bfsLevel(FlowNetwork g, int s, int t, int[] level) {
        Arrays.fill(level, -1);
        Deque<Integer> q = new ArrayDeque<>();
        level[s] = 0;
        q.add(s);
        while (!q.isEmpty()) {
            int u = q.poll();
            for (FlowEdge e : g.adj.get(u)) {
                if (e.cap > 0 && level[e.to] == -1) {
                    level[e.to] = level[u] + 1;
                    q.add(e.to);
                }
            }
        }
        return level[t] != -1;
    }

    private static long dfsDinic(FlowNetwork g, int u, int t, long f, int[] level, int[] it) {
        if (u == t) {
            return f;
        }
        for (; it[u] < g.adj.get(u).size(); it[u]++) {
            FlowEdge e = g.adj.get(u).get(it[u]);
            if (e.cap > 0 && level[e.to] == level[u] + 1) {
                long pushed = dfsDinic(g, e.to, t, Math.min(f, e.cap), level, it);
                if (pushed > 0) {
                    e.cap -= pushed;
                    g.adj.get(e.to).get(e.rev).cap += pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }

    public static long pushRelabel(FlowNetwork g, int s, int t) {
        int n = g.n;
        long[] excess = new long[n];
        int[] height = new int[n];
        height[s] = n;
        for (FlowEdge e : g.adj.get(s)) {
            long cap = e.cap;
            if (cap > 0) {
                e.cap -= cap;
                g.adj.get(e.to).get(e.rev).cap += cap;
                excess[e.to] += cap;
            }
        }
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (i != s && i != t && excess[i] > 0) {
                q.add(i);
            }
        }
        while (!q.isEmpty()) {
            int u = q.poll();
            for (FlowEdge e : g.adj.get(u)) {
                if (e.cap > 0 && height[u] == height[e.to] + 1) {
                    long send = Math.min(excess[u], e.cap);
                    e.cap -= send;
                    g.adj.get(e.to).get(e.rev).cap += send;
                    excess[u] -= send;
                    excess[e.to] += send;
                    if (e.to != s && e.to != t && excess[e.to] == send) {
                        q.add(e.to);
                    }
                    if (excess[u] == 0) {
                        break;
                    }
                }
            }
            if (excess[u] > 0) {
                int minHeight = Integer.MAX_VALUE;
                for (FlowEdge e : g.adj.get(u)) {
                    if (e.cap > 0) {
                        minHeight = Math.min(minHeight, height[e.to]);
                    }
                }
                if (minHeight < Integer.MAX_VALUE) {
                    height[u] = minHeight + 1;
                }
                q.add(u);
            }
        }
        return excess[t];
    }

    public static boolean[] minCut(FlowNetwork g, int s) {
        boolean[] reachable = new boolean[g.n];
        Deque<Integer> q = new ArrayDeque<>();
        q.add(s);
        reachable[s] = true;
        while (!q.isEmpty()) {
            int u = q.poll();
            for (FlowEdge e : g.adj.get(u)) {
                if (e.cap > 0 && !reachable[e.to]) {
                    reachable[e.to] = true;
                    q.add(e.to);
                }
            }
        }
        return reachable;
    }

    public static class HungarianResult {
        public final int[] assignment;
        public final long minCost;

        public HungarianResult(int[] assignment, long minCost) {
            this.assignment = assignment;
            this.minCost = minCost;
        }
    }

    public static HungarianResult hungarian(long[][] cost) {
        int n = cost.length;
        int m = cost[0].length;
        int size = Math.max(n, m);
        long[][] a = new long[size + 1][size + 1];
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                if (i <= n && j <= m) {
                    a[i][j] = cost[i - 1][j - 1];
                } else {
                    a[i][j] = 0;
                }
            }
        }
        long[] u = new long[size + 1];
        long[] v = new long[size + 1];
        int[] p = new int[size + 1];
        int[] way = new int[size + 1];
        for (int i = 1; i <= size; i++) {
            p[0] = i;
            long[] minv = new long[size + 1];
            boolean[] used = new boolean[size + 1];
            Arrays.fill(minv, Long.MAX_VALUE / 4);
            Arrays.fill(used, false);
            int j0 = 0;
            do {
                used[j0] = true;
                int i0 = p[j0];
                long delta = Long.MAX_VALUE / 4;
                int j1 = 0;
                for (int j = 1; j <= size; j++) {
                    if (!used[j]) {
                        long cur = a[i0][j] - u[i0] - v[j];
                        if (cur < minv[j]) {
                            minv[j] = cur;
                            way[j] = j0;
                        }
                        if (minv[j] < delta) {
                            delta = minv[j];
                            j1 = j;
                        }
                    }
                }
                for (int j = 0; j <= size; j++) {
                    if (used[j]) {
                        u[p[j]] += delta;
                        v[j] -= delta;
                    } else {
                        minv[j] -= delta;
                    }
                }
                j0 = j1;
            } while (p[j0] != 0);
            do {
                int j1 = way[j0];
                p[j0] = p[j1];
                j0 = j1;
            } while (j0 != 0);
        }
        int[] assignment = new int[n];
        Arrays.fill(assignment, -1);
        for (int j = 1; j <= size; j++) {
            if (p[j] >= 1 && p[j] <= n && j <= m) {
                assignment[p[j] - 1] = j - 1;
            }
        }
        long minCost = 0;
        for (int i = 0; i < n; i++) {
            if (assignment[i] != -1) {
                minCost += cost[i][assignment[i]];
            }
        }
        return new HungarianResult(assignment, minCost);
    }
}
