import java.util.Arrays;

public class DpWithBitmasks {
    private DpWithBitmasks() {
    }

    public static long tsp(int[][] dist) {
        int n = dist.length;
        int full = 1 << n;
        long[][] dp = new long[full][n];
        for (int i = 0; i < full; i++) {
            Arrays.fill(dp[i], Long.MAX_VALUE / 4);
        }
        dp[1][0] = 0;
        for (int mask = 1; mask < full; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0) {
                    continue;
                }
                long cur = dp[mask][u];
                if (cur >= Long.MAX_VALUE / 8) {
                    continue;
                }
                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0) {
                        int nmask = mask | (1 << v);
                        dp[nmask][v] = Math.min(dp[nmask][v], cur + dist[u][v]);
                    }
                }
            }
        }
        long best = Long.MAX_VALUE / 4;
        for (int u = 1; u < n; u++) {
            best = Math.min(best, dp[full - 1][u] + dist[u][0]);
        }
        return best;
    }

    public static long assignmentProblem(int[][] cost) {
        int n = cost.length;
        int full = 1 << n;
        long[] dp = new long[full];
        Arrays.fill(dp, Long.MAX_VALUE / 4);
        dp[0] = 0;
        for (int mask = 0; mask < full; mask++) {
            int i = Integer.bitCount(mask);
            if (i >= n) {
                continue;
            }
            for (int j = 0; j < n; j++) {
                if ((mask & (1 << j)) == 0) {
                    int nmask = mask | (1 << j);
                    dp[nmask] = Math.min(dp[nmask], dp[mask] + cost[i][j]);
                }
            }
        }
        return dp[full - 1];
    }

    public static boolean hamiltonianPath(boolean[][] adj) {
        int n = adj.length;
        int full = 1 << n;
        boolean[][] dp = new boolean[full][n];
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = true;
        }
        for (int mask = 0; mask < full; mask++) {
            for (int u = 0; u < n; u++) {
                if (!dp[mask][u]) {
                    continue;
                }
                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0 && adj[u][v]) {
                        dp[mask | (1 << v)][v] = true;
                    }
                }
            }
        }
        for (int u = 0; u < n; u++) {
            if (dp[full - 1][u]) {
                return true;
            }
        }
        return false;
    }

    public static int setCover(int universeSize, int[] sets) {
        int full = 1 << universeSize;
        int[] dp = new int[full];
        Arrays.fill(dp, Integer.MAX_VALUE / 4);
        dp[0] = 0;
        for (int mask = 0; mask < full; mask++) {
            if (dp[mask] == Integer.MAX_VALUE / 4) {
                continue;
            }
            for (int set : sets) {
                int nmask = mask | set;
                dp[nmask] = Math.min(dp[nmask], dp[mask] + 1);
            }
        }
        return dp[full - 1];
    }

    public static long subsetSumBitmask(int[] nums) {
        long bits = 1L;
        for (int num : nums) {
            if (num < 0 || num > 60) {
                throw new IllegalArgumentException("Only supports numbers 0..60");
            }
            bits |= (bits << num);
        }
        return bits;
    }
}
