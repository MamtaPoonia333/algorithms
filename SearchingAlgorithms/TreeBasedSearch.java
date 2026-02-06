import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreeBasedSearch {
    private TreeBasedSearch() {
    }

    public static List<Integer> dfs(int n, List<List<Integer>> adj, int start) {
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
        Deque<Integer> q = new ArrayDeque<>();
        visited[start] = true;
        q.add(start);
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    q.add(v);
                }
            }
        }
        return order;
    }

    public static boolean bidirectionalSearch(int n, List<List<Integer>> adj, int start, int target) {
        if (start == target) {
            return true;
        }
        Set<Integer> frontA = new HashSet<>();
        Set<Integer> frontB = new HashSet<>();
        boolean[] visitedA = new boolean[n];
        boolean[] visitedB = new boolean[n];
        frontA.add(start);
        frontB.add(target);
        visitedA[start] = true;
        visitedB[target] = true;
        while (!frontA.isEmpty() && !frontB.isEmpty()) {
            if (frontA.size() > frontB.size()) {
                Set<Integer> tmp = frontA;
                frontA = frontB;
                frontB = tmp;
                boolean[] tmpV = visitedA;
                visitedA = visitedB;
                visitedB = tmpV;
            }
            Set<Integer> next = new HashSet<>();
            for (int u : frontA) {
                for (int v : adj.get(u)) {
                    if (visitedB[v]) {
                        return true;
                    }
                    if (!visitedA[v]) {
                        visitedA[v] = true;
                        next.add(v);
                    }
                }
            }
            frontA = next;
        }
        return false;
    }
}
