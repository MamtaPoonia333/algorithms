import java.util.ArrayList;
import java.util.List;

public class DpOnTrees {
    private DpOnTrees() {
    }

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static int maxPathSumBinaryTree(TreeNode root) {
        int[] best = new int[]{Integer.MIN_VALUE};
        maxPathSumDfs(root, best);
        return best[0];
    }

    private static int maxPathSumDfs(TreeNode node, int[] best) {
        if (node == null) {
            return 0;
        }
        int left = Math.max(0, maxPathSumDfs(node.left, best));
        int right = Math.max(0, maxPathSumDfs(node.right, best));
        best[0] = Math.max(best[0], node.val + left + right);
        return node.val + Math.max(left, right);
    }

    public static int treeDiameter(int n, List<List<Integer>> adj) {
        int[] first = farthestNode(0, adj);
        int[] second = farthestNode(first[0], adj);
        return second[1];
    }

    private static int[] farthestNode(int start, List<List<Integer>> adj) {
        int n = adj.size();
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];
        List<Integer> queue = new ArrayList<>();
        queue.add(start);
        visited[start] = true;
        for (int i = 0; i < queue.size(); i++) {
            int u = queue.get(i);
            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    dist[v] = dist[u] + 1;
                    queue.add(v);
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

    public static int[] subtreeSizes(int n, List<List<Integer>> adj, int root) {
        int[] size = new int[n];
        dfsSubtree(root, -1, adj, size);
        return size;
    }

    private static void dfsSubtree(int u, int parent, List<List<Integer>> adj, int[] size) {
        size[u] = 1;
        for (int v : adj.get(u)) {
            if (v != parent) {
                dfsSubtree(v, u, adj, size);
                size[u] += size[v];
            }
        }
    }

    public static long[] sumOfDistancesInTree(int n, List<List<Integer>> adj, int root) {
        int[] size = new int[n];
        long[] dist = new long[n];
        dfsDist(root, -1, adj, size, dist);
        long[] ans = new long[n];
        ans[root] = dist[root];
        reroot(root, -1, adj, size, ans, n);
        return ans;
    }

    private static void dfsDist(int u, int parent, List<List<Integer>> adj, int[] size, long[] dist) {
        size[u] = 1;
        for (int v : adj.get(u)) {
            if (v != parent) {
                dfsDist(v, u, adj, size, dist);
                size[u] += size[v];
                dist[u] += dist[v] + size[v];
            }
        }
    }

    private static void reroot(int u, int parent, List<List<Integer>> adj, int[] size, long[] ans, int n) {
        for (int v : adj.get(u)) {
            if (v != parent) {
                ans[v] = ans[u] - size[v] + (n - size[v]);
                reroot(v, u, adj, size, ans, n);
            }
        }
    }
}
