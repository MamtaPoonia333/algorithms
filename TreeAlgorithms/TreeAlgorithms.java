import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class TreeAlgorithms {
    private TreeAlgorithms() {
    }

    public static class LcaBinaryLifting {
        public final int n;
        public final int LOG;
        public final int[][] up;
        public final int[] depth;

        public LcaBinaryLifting(int n, List<List<Integer>> adj, int root) {
            this.n = n;
            int log = 1;
            while ((1 << log) <= n) {
                log++;
            }
            this.LOG = log;
            this.up = new int[LOG][n];
            this.depth = new int[n];
            Arrays.fill(depth, -1);
            dfs(root, root, adj);
            for (int k = 1; k < LOG; k++) {
                for (int i = 0; i < n; i++) {
                    up[k][i] = up[k - 1][up[k - 1][i]];
                }
            }
        }

        private void dfs(int u, int p, List<List<Integer>> adj) {
            up[0][u] = p;
            depth[u] = p == u ? 0 : depth[p] + 1;
            for (int v : adj.get(u)) {
                if (v != p) {
                    dfs(v, u, adj);
                }
            }
        }

        public int lca(int a, int b) {
            if (depth[a] < depth[b]) {
                int t = a;
                a = b;
                b = t;
            }
            int diff = depth[a] - depth[b];
            for (int k = 0; k < LOG; k++) {
                if (((diff >> k) & 1) == 1) {
                    a = up[k][a];
                }
            }
            if (a == b) {
                return a;
            }
            for (int k = LOG - 1; k >= 0; k--) {
                if (up[k][a] != up[k][b]) {
                    a = up[k][a];
                    b = up[k][b];
                }
            }
            return up[0][a];
        }
    }

    public static class EulerTourRMQ {
        public final int[] euler;
        public final int[] depth;
        public final int[] first;
        private final int[][] st;
        private final int[] log;

        public EulerTourRMQ(int n, List<List<Integer>> adj, int root) {
            List<Integer> e = new ArrayList<>();
            List<Integer> d = new ArrayList<>();
            first = new int[n];
            Arrays.fill(first, -1);
            dfs(root, -1, 0, adj, e, d, first);
            euler = e.stream().mapToInt(i -> i).toArray();
            depth = d.stream().mapToInt(i -> i).toArray();
            int m = euler.length;
            log = new int[m + 1];
            for (int i = 2; i <= m; i++) {
                log[i] = log[i / 2] + 1;
            }
            int K = log[m] + 1;
            st = new int[K][m];
            for (int i = 0; i < m; i++) {
                st[0][i] = i;
            }
            for (int k = 1; k < K; k++) {
                for (int i = 0; i + (1 << k) <= m; i++) {
                    int x = st[k - 1][i];
                    int y = st[k - 1][i + (1 << (k - 1))];
                    st[k][i] = depth[x] <= depth[y] ? x : y;
                }
            }
        }

        private void dfs(int u, int p, int dep, List<List<Integer>> adj, List<Integer> e, List<Integer> d, int[] first) {
            if (first[u] == -1) {
                first[u] = e.size();
            }
            e.add(u);
            d.add(dep);
            for (int v : adj.get(u)) {
                if (v != p) {
                    dfs(v, u, dep + 1, adj, e, d, first);
                    e.add(u);
                    d.add(dep);
                }
            }
        }

        public int lca(int u, int v) {
            int l = first[u];
            int r = first[v];
            if (l > r) {
                int t = l;
                l = r;
                r = t;
            }
            int k = log[r - l + 1];
            int x = st[k][l];
            int y = st[k][r - (1 << k) + 1];
            return depth[x] <= depth[y] ? euler[x] : euler[y];
        }
    }

    public static class TarjanOfflineLca {
        private final int n;
        private final List<List<Integer>> adj;
        private final List<List<int[]>> queries;
        private final int[] parent;
        private final int[] ancestor;
        private final boolean[] visited;
        private final int[] result;

        public TarjanOfflineLca(int n, List<List<Integer>> adj, int queryCount) {
            this.n = n;
            this.adj = adj;
            this.queries = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                queries.add(new ArrayList<>());
            }
            this.parent = new int[n];
            this.ancestor = new int[n];
            this.visited = new boolean[n];
            this.result = new int[queryCount];
        }

        public void addQuery(int idx, int u, int v) {
            queries.get(u).add(new int[]{v, idx});
            queries.get(v).add(new int[]{u, idx});
        }

        public int[] compute(int root) {
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                ancestor[i] = i;
            }
            dfs(root, -1);
            return result;
        }

        private void dfs(int u, int p) {
            ancestor[u] = u;
            for (int v : adj.get(u)) {
                if (v != p) {
                    dfs(v, u);
                    union(u, v);
                    ancestor[find(u)] = u;
                }
            }
            visited[u] = true;
            for (int[] q : queries.get(u)) {
                int v = q[0];
                int idx = q[1];
                if (visited[v]) {
                    result[idx] = ancestor[find(v)];
                }
            }
        }

        private int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        private void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra != rb) {
                parent[rb] = ra;
            }
        }
    }

    public static int diameter(int n, List<List<Integer>> adj) {
        int[] first = farthest(0, adj);
        int[] second = farthest(first[0], adj);
        return second[1];
    }

    private static int[] farthest(int start, List<List<Integer>> adj) {
        int n = adj.size();
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        Deque<Integer> q = new ArrayDeque<>();
        q.add(start);
        dist[start] = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : adj.get(u)) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    q.add(v);
                }
            }
        }
        int best = start;
        for (int i = 0; i < n; i++) {
            if (dist[i] > dist[best]) {
                best = i;
            }
        }
        return new int[]{best, dist[best]};
    }

    public static int[] depths(int n, List<List<Integer>> adj, int root) {
        int[] depth = new int[n];
        Arrays.fill(depth, -1);
        Deque<Integer> q = new ArrayDeque<>();
        q.add(root);
        depth[root] = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : adj.get(u)) {
                if (depth[v] == -1) {
                    depth[v] = depth[u] + 1;
                    q.add(v);
                }
            }
        }
        return depth;
    }

    public static boolean isIsomorphic(int n, List<List<Integer>> adj1, List<List<Integer>> adj2) {
        List<Integer> c1 = centers(adj1);
        List<Integer> c2 = centers(adj2);
        List<String> enc1 = new ArrayList<>();
        for (int c : c1) {
            enc1.add(encode(c, -1, adj1));
        }
        for (int c : c2) {
            String e2 = encode(c, -1, adj2);
            for (String e1 : enc1) {
                if (e1.equals(e2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Integer> centers(List<List<Integer>> adj) {
        int n = adj.size();
        int[] deg = new int[n];
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            deg[i] = adj.get(i).size();
            if (deg[i] <= 1) {
                q.add(i);
            }
        }
        int removed = 0;
        while (removed < n - 2) {
            int size = q.size();
            removed += size;
            for (int i = 0; i < size; i++) {
                int u = q.poll();
                for (int v : adj.get(u)) {
                    deg[v]--;
                    if (deg[v] == 1) {
                        q.add(v);
                    }
                }
            }
        }
        List<Integer> centers = new ArrayList<>();
        while (!q.isEmpty()) {
            centers.add(q.poll());
        }
        return centers;
    }

    private static String encode(int u, int p, List<List<Integer>> adj) {
        List<String> labels = new ArrayList<>();
        for (int v : adj.get(u)) {
            if (v != p) {
                labels.add(encode(v, u, adj));
            }
        }
        labels.sort(String::compareTo);
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String s : labels) {
            sb.append(s);
        }
        sb.append(")");
        return sb.toString();
    }

    public static class HeavyLightDecomposition {
        public final int n;
        public final int[] parent;
        public final int[] depth;
        public final int[] heavy;
        public final int[] head;
        public final int[] pos;
        private int curPos;

        public HeavyLightDecomposition(int n, List<List<Integer>> adj, int root) {
            this.n = n;
            parent = new int[n];
            depth = new int[n];
            heavy = new int[n];
            head = new int[n];
            pos = new int[n];
            Arrays.fill(heavy, -1);
            dfs(root, -1, adj);
            curPos = 0;
            decompose(root, root, adj);
        }

        private int dfs(int u, int p, List<List<Integer>> adj) {
            parent[u] = p;
            int size = 1;
            int maxSub = 0;
            for (int v : adj.get(u)) {
                if (v != p) {
                    depth[v] = depth[u] + 1;
                    int sub = dfs(v, u, adj);
                    size += sub;
                    if (sub > maxSub) {
                        maxSub = sub;
                        heavy[u] = v;
                    }
                }
            }
            return size;
        }

        private void decompose(int u, int h, List<List<Integer>> adj) {
            head[u] = h;
            pos[u] = curPos++;
            if (heavy[u] != -1) {
                decompose(heavy[u], h, adj);
            }
            for (int v : adj.get(u)) {
                if (v != parent[u] && v != heavy[u]) {
                    decompose(v, v, adj);
                }
            }
        }

        public List<int[]> pathSegments(int a, int b) {
            List<int[]> segments = new ArrayList<>();
            while (head[a] != head[b]) {
                if (depth[head[a]] < depth[head[b]]) {
                    int t = a;
                    a = b;
                    b = t;
                }
                segments.add(new int[]{pos[head[a]], pos[a]});
                a = parent[head[a]];
            }
            if (depth[a] > depth[b]) {
                int t = a;
                a = b;
                b = t;
            }
            segments.add(new int[]{pos[a], pos[b]});
            return segments;
        }
    }

    public static class CentroidDecomposition {
        public final int n;
        public final List<List<Integer>> tree;
        public final int[] parent;
        private final boolean[] removed;
        private final int[] size;

        public CentroidDecomposition(int n, List<List<Integer>> adj) {
            this.n = n;
            this.tree = adj;
            this.parent = new int[n];
            Arrays.fill(parent, -1);
            this.removed = new boolean[n];
            this.size = new int[n];
            build(0, -1);
        }

        private void build(int entry, int p) {
            int total = dfsSize(entry, -1);
            int centroid = dfsCentroid(entry, -1, total);
            parent[centroid] = p;
            removed[centroid] = true;
            for (int v : tree.get(centroid)) {
                if (!removed[v]) {
                    build(v, centroid);
                }
            }
        }

        private int dfsSize(int u, int p) {
            size[u] = 1;
            for (int v : tree.get(u)) {
                if (v != p && !removed[v]) {
                    size[u] += dfsSize(v, u);
                }
            }
            return size[u];
        }

        private int dfsCentroid(int u, int p, int total) {
            for (int v : tree.get(u)) {
                if (v != p && !removed[v] && size[v] > total / 2) {
                    return dfsCentroid(v, u, total);
                }
            }
            return u;
        }
    }
}
