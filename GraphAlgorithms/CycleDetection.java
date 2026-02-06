import java.util.List;

public class CycleDetection {
    private CycleDetection() {
    }

    public static boolean hasCycleDirected(int n, List<List<Integer>> adj) {
        int[] state = new int[n];
        for (int i = 0; i < n; i++) {
            if (state[i] == 0 && dfsDirected(i, adj, state)) {
                return true;
            }
        }
        return false;
    }

    private static boolean dfsDirected(int u, List<List<Integer>> adj, int[] state) {
        state[u] = 1;
        for (int v : adj.get(u)) {
            if (state[v] == 1) {
                return true;
            }
            if (state[v] == 0 && dfsDirected(v, adj, state)) {
                return true;
            }
        }
        state[u] = 2;
        return false;
    }

    public static boolean hasCycleUndirected(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (!visited[i] && dfsUndirected(i, -1, adj, visited)) {
                return true;
            }
        }
        return false;
    }

    private static boolean dfsUndirected(int u, int parent, List<List<Integer>> adj, boolean[] visited) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                if (dfsUndirected(v, u, adj, visited)) {
                    return true;
                }
            } else if (v != parent) {
                return true;
            }
        }
        return false;
    }

    public static boolean floydCycleDetection(int[] next, int start) {
        int slow = start;
        int fast = start;
        while (true) {
            slow = step(next, slow);
            fast = step(next, step(next, fast));
            if (slow == -1 || fast == -1) {
                return false;
            }
            if (slow == fast) {
                return true;
            }
        }
    }

    private static int step(int[] next, int idx) {
        if (idx < 0 || idx >= next.length) {
            return -1;
        }
        int n = next[idx];
        if (n < 0 || n >= next.length) {
            return -1;
        }
        return n;
    }
}
