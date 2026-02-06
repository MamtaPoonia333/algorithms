import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Traversal {
    private Traversal() {
    }

    public static List<Integer> dfsRecursive(int n, List<List<Integer>> adj, int start) {
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        dfsRec(start, adj, visited, order);
        return order;
    }

    private static void dfsRec(int u, List<List<Integer>> adj, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        order.add(u);
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                dfsRec(v, adj, visited, order);
            }
        }
    }

    public static List<Integer> dfsIterative(int n, List<List<Integer>> adj, int start) {
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (visited[u]) {
                continue;
            }
            visited[u] = true;
            order.add(u);
            List<Integer> neighbors = adj.get(u);
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                int v = neighbors.get(i);
                if (!visited[v]) {
                    stack.push(v);
                }
            }
        }
        return order;
    }

    public static List<Integer> bfs(int n, List<List<Integer>> adj, int start) {
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        Deque<Integer> queue = new ArrayDeque<>();
        visited[start] = true;
        queue.add(start);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    queue.add(v);
                }
            }
        }
        return order;
    }

    public static boolean iterativeDeepeningDfs(int n, List<List<Integer>> adj, int start, int target, int maxDepth) {
        for (int depth = 0; depth <= maxDepth; depth++) {
            boolean[] visited = new boolean[n];
            if (dls(start, target, depth, adj, visited)) {
                return true;
            }
        }
        return false;
    }

    private static boolean dls(int u, int target, int depth, List<List<Integer>> adj, boolean[] visited) {
        if (u == target) {
            return true;
        }
        if (depth == 0) {
            return false;
        }
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v] && dls(v, target, depth - 1, adj, visited)) {
                return true;
            }
        }
        return false;
    }
}
