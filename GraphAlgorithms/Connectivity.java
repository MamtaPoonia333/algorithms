import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class Connectivity {
    private Connectivity() {
    }

    public static List<List<Integer>> tarjanScc(int n, List<List<Integer>> adj) {
        int[] disc = new int[n];
        int[] low = new int[n];
        boolean[] inStack = new boolean[n];
        Arrays.fill(disc, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        List<List<Integer>> sccs = new ArrayList<>();
        int[] time = new int[]{0};
        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) {
                tarjanDfs(i, adj, disc, low, inStack, stack, time, sccs);
            }
        }
        return sccs;
    }

    private static void tarjanDfs(int u, List<List<Integer>> adj, int[] disc, int[] low, boolean[] inStack,
                                  Deque<Integer> stack, int[] time, List<List<Integer>> sccs) {
        disc[u] = low[u] = time[0]++;
        stack.push(u);
        inStack[u] = true;
        for (int v : adj.get(u)) {
            if (disc[v] == -1) {
                tarjanDfs(v, adj, disc, low, inStack, stack, time, sccs);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int v = stack.pop();
                inStack[v] = false;
                comp.add(v);
                if (v == u) {
                    break;
                }
            }
            sccs.add(comp);
        }
    }

    public static List<List<Integer>> kosarajuScc(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                kosarajuDfs1(i, adj, visited, order);
            }
        }
        List<List<Integer>> rev = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            rev.add(new ArrayList<>());
        }
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                rev.get(v).add(u);
            }
        }
        Arrays.fill(visited, false);
        List<List<Integer>> sccs = new ArrayList<>();
        for (int i = order.size() - 1; i >= 0; i--) {
            int v = order.get(i);
            if (!visited[v]) {
                List<Integer> comp = new ArrayList<>();
                kosarajuDfs2(v, rev, visited, comp);
                sccs.add(comp);
            }
        }
        return sccs;
    }

    private static void kosarajuDfs1(int u, List<List<Integer>> adj, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                kosarajuDfs1(v, adj, visited, order);
            }
        }
        order.add(u);
    }

    private static void kosarajuDfs2(int u, List<List<Integer>> rev, boolean[] visited, List<Integer> comp) {
        visited[u] = true;
        comp.add(u);
        for (int v : rev.get(u)) {
            if (!visited[v]) {
                kosarajuDfs2(v, rev, visited, comp);
            }
        }
    }

    public static class ArticulationBridgeResult {
        public final boolean[] articulationPoints;
        public final List<int[]> bridges;

        public ArticulationBridgeResult(boolean[] articulationPoints, List<int[]> bridges) {
            this.articulationPoints = articulationPoints;
            this.bridges = bridges;
        }
    }

    public static ArticulationBridgeResult articulationPointsAndBridges(int n, List<List<Integer>> adj) {
        int[] disc = new int[n];
        int[] low = new int[n];
        int[] parent = new int[n];
        Arrays.fill(disc, -1);
        Arrays.fill(parent, -1);
        boolean[] ap = new boolean[n];
        List<int[]> bridges = new ArrayList<>();
        int[] time = new int[]{0};
        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) {
                apDfs(i, adj, disc, low, parent, ap, bridges, time);
            }
        }
        return new ArticulationBridgeResult(ap, bridges);
    }

    private static void apDfs(int u, List<List<Integer>> adj, int[] disc, int[] low, int[] parent,
                              boolean[] ap, List<int[]> bridges, int[] time) {
        disc[u] = low[u] = time[0]++;
        int children = 0;
        for (int v : adj.get(u)) {
            if (disc[v] == -1) {
                parent[v] = u;
                children++;
                apDfs(v, adj, disc, low, parent, ap, bridges, time);
                low[u] = Math.min(low[u], low[v]);
                if (parent[u] == -1 && children > 1) {
                    ap[u] = true;
                }
                if (parent[u] != -1 && low[v] >= disc[u]) {
                    ap[u] = true;
                }
                if (low[v] > disc[u]) {
                    bridges.add(new int[]{u, v});
                }
            } else if (v != parent[u]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    public static List<List<int[]>> biconnectedComponents(int n, List<List<Integer>> adj) {
        int[] disc = new int[n];
        int[] low = new int[n];
        Arrays.fill(disc, -1);
        Deque<int[]> edgeStack = new ArrayDeque<>();
        List<List<int[]>> components = new ArrayList<>();
        int[] time = new int[]{0};
        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) {
                bccDfs(i, -1, adj, disc, low, edgeStack, components, time);
                if (!edgeStack.isEmpty()) {
                    List<int[]> comp = new ArrayList<>();
                    while (!edgeStack.isEmpty()) {
                        comp.add(edgeStack.pop());
                    }
                    components.add(comp);
                }
            }
        }
        return components;
    }

    private static void bccDfs(int u, int parent, List<List<Integer>> adj, int[] disc, int[] low,
                               Deque<int[]> edgeStack, List<List<int[]>> components, int[] time) {
        disc[u] = low[u] = time[0]++;
        int children = 0;
        for (int v : adj.get(u)) {
            if (disc[v] == -1) {
                children++;
                edgeStack.push(new int[]{u, v});
                bccDfs(v, u, adj, disc, low, edgeStack, components, time);
                low[u] = Math.min(low[u], low[v]);
                if ((parent == -1 && children > 1) || (parent != -1 && low[v] >= disc[u])) {
                    List<int[]> comp = new ArrayList<>();
                    while (!edgeStack.isEmpty()) {
                        int[] e = edgeStack.pop();
                        comp.add(e);
                        if ((e[0] == u && e[1] == v) || (e[0] == v && e[1] == u)) {
                            break;
                        }
                    }
                    components.add(comp);
                }
            } else if (v != parent && disc[v] < disc[u]) {
                low[u] = Math.min(low[u], disc[v]);
                edgeStack.push(new int[]{u, v});
            }
        }
    }

    public static List<Integer> eulerianPathUndirected(int n, List<int[]> edges) {
        int[] degree = new int[n];
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int i = 0; i < edges.size(); i++) {
            int u = edges.get(i)[0];
            int v = edges.get(i)[1];
            degree[u]++;
            degree[v]++;
            adj.get(u).add(new int[]{v, i});
            adj.get(v).add(new int[]{u, i});
        }
        int start = -1;
        int odd = 0;
        for (int i = 0; i < n; i++) {
            if (degree[i] % 2 == 1) {
                odd++;
                start = i;
            } else if (start == -1 && degree[i] > 0) {
                start = i;
            }
        }
        if (!(odd == 0 || odd == 2)) {
            return new ArrayList<>();
        }
        if (start == -1) {
            return new ArrayList<>();
        }
        boolean[] used = new boolean[edges.size()];
        Deque<Integer> stack = new ArrayDeque<>();
        List<Integer> path = new ArrayList<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            int u = stack.peek();
            while (!adj.get(u).isEmpty() && used[adj.get(u).get(adj.get(u).size() - 1)[1]]) {
                adj.get(u).remove(adj.get(u).size() - 1);
            }
            if (adj.get(u).isEmpty()) {
                path.add(u);
                stack.pop();
            } else {
                int[] e = adj.get(u).remove(adj.get(u).size() - 1);
                int v = e[0];
                int id = e[1];
                if (used[id]) {
                    continue;
                }
                used[id] = true;
                stack.push(v);
            }
        }
        if (path.size() != edges.size() + 1) {
            return new ArrayList<>();
        }
        List<Integer> result = new ArrayList<>();
        for (int i = path.size() - 1; i >= 0; i--) {
            result.add(path.get(i));
        }
        return result;
    }

    public static List<Integer> hamiltonianPath(boolean[][] adj) {
        int n = adj.length;
        int[] path = new int[n];
        Arrays.fill(path, -1);
        for (int start = 0; start < n; start++) {
            boolean[] used = new boolean[n];
            path[0] = start;
            used[start] = true;
            if (hamPathDfs(1, path, used, adj)) {
                List<Integer> result = new ArrayList<>();
                for (int v : path) {
                    result.add(v);
                }
                return result;
            }
        }
        return new ArrayList<>();
    }

    public static List<Integer> hamiltonianCycle(boolean[][] adj) {
        int n = adj.length;
        int[] path = new int[n];
        Arrays.fill(path, -1);
        int start = 0;
        boolean[] used = new boolean[n];
        path[0] = start;
        used[start] = true;
        if (hamCycleDfs(1, path, used, adj, start)) {
            List<Integer> result = new ArrayList<>();
            for (int v : path) {
                result.add(v);
            }
            result.add(start);
            return result;
        }
        return new ArrayList<>();
    }

    private static boolean hamPathDfs(int idx, int[] path, boolean[] used, boolean[][] adj) {
        int n = adj.length;
        if (idx == n) {
            return true;
        }
        for (int v = 0; v < n; v++) {
            int prev = path[idx - 1];
            if (!used[v] && adj[prev][v]) {
                used[v] = true;
                path[idx] = v;
                if (hamPathDfs(idx + 1, path, used, adj)) {
                    return true;
                }
                used[v] = false;
                path[idx] = -1;
            }
        }
        return false;
    }

    private static boolean hamCycleDfs(int idx, int[] path, boolean[] used, boolean[][] adj, int start) {
        int n = adj.length;
        if (idx == n) {
            return adj[path[idx - 1]][start];
        }
        for (int v = 0; v < n; v++) {
            int prev = path[idx - 1];
            if (!used[v] && adj[prev][v]) {
                used[v] = true;
                path[idx] = v;
                if (hamCycleDfs(idx + 1, path, used, adj, start)) {
                    return true;
                }
                used[v] = false;
                path[idx] = -1;
            }
        }
        return false;
    }
}
