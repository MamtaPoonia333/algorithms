import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TopologicalSort {
    private TopologicalSort() {
    }

    public static List<Integer> kahn(int n, List<List<Integer>> adj) {
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                indeg[v]++;
            }
        }
        Deque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                dq.add(i);
            }
        }
        List<Integer> order = new ArrayList<>();
        while (!dq.isEmpty()) {
            int u = dq.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                indeg[v]--;
                if (indeg[v] == 0) {
                    dq.add(v);
                }
            }
        }
        return order;
    }

    public static List<Integer> dfsBased(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        boolean[] onStack = new boolean[n];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                if (!dfs(i, adj, visited, onStack, order)) {
                    return new ArrayList<>();
                }
            }
        }
        List<Integer> topo = new ArrayList<>();
        for (int i = order.size() - 1; i >= 0; i--) {
            topo.add(order.get(i));
        }
        return topo;
    }

    private static boolean dfs(int u, List<List<Integer>> adj, boolean[] visited, boolean[] onStack, List<Integer> order) {
        visited[u] = true;
        onStack[u] = true;
        for (int v : adj.get(u)) {
            if (onStack[v]) {
                return false;
            }
            if (!visited[v] && !dfs(v, adj, visited, onStack, order)) {
                return false;
            }
        }
        onStack[u] = false;
        order.add(u);
        return true;
    }
}
