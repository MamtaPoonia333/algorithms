import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class AdvancedDp {
    private AdvancedDp() {
    }

    public static long countNumbersWithDigitSum(long n, int targetSum) {
        char[] digits = Long.toString(n).toCharArray();
        Long[][][] memo = new Long[digits.length + 1][targetSum + 1][2];
        return digitDp(0, 0, 1, digits, targetSum, memo);
    }

    private static long digitDp(int pos, int sum, int tight, char[] digits, int targetSum, Long[][][] memo) {
        if (sum > targetSum) {
            return 0;
        }
        if (pos == digits.length) {
            return sum == targetSum ? 1 : 0;
        }
        if (memo[pos][sum][tight] != null) {
            return memo[pos][sum][tight];
        }
        int limit = tight == 1 ? digits[pos] - '0' : 9;
        long res = 0;
        for (int d = 0; d <= limit; d++) {
            res += digitDp(pos + 1, sum + d, tight == 1 && d == limit ? 1 : 0, digits, targetSum, memo);
        }
        memo[pos][sum][tight] = res;
        return res;
    }

    public static int longestPathInDag(int n, List<List<int[]>> adj) {
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (int[] e : adj.get(u)) {
                indeg[e[0]]++;
            }
        }
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                q.add(i);
            }
        }
        int[] dist = new int[n];
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int[] e : adj.get(u)) {
                int v = e[0];
                int w = e[1];
                dist[v] = Math.max(dist[v], dist[u] + w);
                indeg[v]--;
                if (indeg[v] == 0) {
                    q.add(v);
                }
            }
        }
        int best = 0;
        for (int d : dist) {
            best = Math.max(best, d);
        }
        return best;
    }

    public static double probabilityOfKHeads(int n, double p, int k) {
        double[][] dp = new double[n + 1][k + 1];
        dp[0][0] = 1.0;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                dp[i][j] = dp[i - 1][j] * (1 - p);
                if (j > 0) {
                    dp[i][j] += dp[i - 1][j - 1] * p;
                }
            }
        }
        return dp[n][k];
    }

    public static double expectedDiceThrowsToReachEnd(int n, int faces) {
        double[] dp = new double[n + 1];
        dp[n] = 0;
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            int cnt = 0;
            for (int d = 1; d <= faces && i + d <= n; d++) {
                sum += dp[i + d];
                cnt++;
            }
            dp[i] = 1 + sum / cnt;
        }
        return dp[0];
    }

    public static int[] grundyNumbers(int n, List<List<Integer>> adj) {
        int[] grundy = new int[n];
        boolean[] vis = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                grundyDfs(i, adj, grundy, vis);
            }
        }
        return grundy;
    }

    private static void grundyDfs(int u, List<List<Integer>> adj, int[] grundy, boolean[] vis) {
        vis[u] = true;
        List<Integer> next = adj.get(u);
        boolean[] seen = new boolean[next.size() + 2];
        for (int v : next) {
            if (!vis[v]) {
                grundyDfs(v, adj, grundy, vis);
            }
            int g = grundy[v];
            if (g < seen.length) {
                seen[g] = true;
            }
        }
        int mex = 0;
        while (mex < seen.length && seen[mex]) {
            mex++;
        }
        grundy[u] = mex;
    }

    public static boolean nimWinning(int[] piles) {
        int xor = 0;
        for (int p : piles) {
            xor ^= p;
        }
        return xor != 0;
    }

    public static long[] sosDp(long[] f) {
        int n = 0;
        while ((1 << n) < f.length) {
            n++;
        }
        long[] dp = Arrays.copyOf(f, f.length);
        for (int i = 0; i < n; i++) {
            for (int mask = 0; mask < (1 << n); mask++) {
                if ((mask & (1 << i)) != 0) {
                    dp[mask] += dp[mask ^ (1 << i)];
                }
            }
        }
        return dp;
    }

    public static int[][] buildBinaryLifting(int n, int root, List<List<Integer>> adj) {
        int LOG = 1;
        while ((1 << LOG) <= n) {
            LOG++;
        }
        int[][] up = new int[LOG][n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        Deque<Integer> q = new ArrayDeque<>();
        q.add(root);
        parent[root] = root;
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : adj.get(u)) {
                if (parent[v] == -1) {
                    parent[v] = u;
                    q.add(v);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            up[0][i] = parent[i];
        }
        for (int k = 1; k < LOG; k++) {
            for (int i = 0; i < n; i++) {
                up[k][i] = up[k - 1][up[k - 1][i]];
            }
        }
        return up;
    }

    public static int kthAncestor(int[][] up, int node, int k) {
        int LOG = up.length;
        for (int i = 0; i < LOG; i++) {
            if (((k >> i) & 1) == 1) {
                node = up[i][node];
            }
        }
        return node;
    }

    public static long countDominoTilings(int n, int m) {
        int maxMask = 1 << m;
        long[] dp = new long[maxMask];
        dp[0] = 1;
        for (int row = 0; row < n; row++) {
            long[] next = new long[maxMask];
            for (int mask = 0; mask < maxMask; mask++) {
                fillRow(0, m, mask, 0, dp[mask], next);
            }
            dp = next;
        }
        return dp[0];
    }

    private static void fillRow(int col, int m, int mask, int nextMask, long ways, long[] next) {
        if (col == m) {
            next[nextMask] += ways;
            return;
        }
        if ((mask & (1 << col)) != 0) {
            fillRow(col + 1, m, mask, nextMask, ways, next);
        } else {
            fillRow(col + 1, m, mask, nextMask | (1 << col), ways, next);
            if (col + 1 < m && (mask & (1 << (col + 1))) == 0) {
                fillRow(col + 2, m, mask, nextMask, ways, next);
            }
        }
    }

    public static int bitmaskDpMaxWeight(int[] weights) {
        int n = weights.length;
        int full = 1 << n;
        int[] dp = new int[full];
        for (int mask = 1; mask < full; mask++) {
            int lsb = Integer.numberOfTrailingZeros(mask);
            int prev = mask & (mask - 1);
            dp[mask] = dp[prev] + weights[lsb];
        }
        int best = 0;
        for (int val : dp) {
            best = Math.max(best, val);
        }
        return best;
    }
}
