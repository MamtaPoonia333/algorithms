import java.util.Arrays;

public class DpOptimizationTechniques {
    private DpOptimizationTechniques() {
    }

    public static int slidingWindowMaxSum(int[] nums, int k) {
        int n = nums.length;
        if (k <= 0 || k > n) {
            throw new IllegalArgumentException("Invalid window size");
        }
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }
        int best = sum;
        for (int i = k; i < n; i++) {
            sum += nums[i] - nums[i - k];
            best = Math.max(best, sum);
        }
        return best;
    }

    public interface CostFunction {
        long cost(int i, int j);
    }

    public static void divideAndConquerOptimize(long[] prev, long[] cur, int l, int r, int optL, int optR, CostFunction cost) {
        if (l > r) {
            return;
        }
        int mid = (l + r) >>> 1;
        long bestVal = Long.MAX_VALUE / 4;
        int bestIdx = -1;
        int start = Math.max(optL, 0);
        int end = Math.min(optR, mid);
        for (int k = start; k <= end; k++) {
            long val = prev[k] + cost.cost(k, mid);
            if (val < bestVal) {
                bestVal = val;
                bestIdx = k;
            }
        }
        cur[mid] = bestVal;
        divideAndConquerOptimize(prev, cur, l, mid - 1, optL, bestIdx, cost);
        divideAndConquerOptimize(prev, cur, mid + 1, r, bestIdx, optR, cost);
    }

    public static long[] computeDivideConquerDP(long[] prev, int n, CostFunction cost) {
        long[] cur = new long[n];
        divideAndConquerOptimize(prev, cur, 0, n - 1, 0, n - 1, cost);
        return cur;
    }

    public static int optimalBstKnuth(int[] keys, int[] freq) {
        int n = keys.length;
        int[][] dp = new int[n][n];
        int[][] opt = new int[n][n];
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + freq[i];
            dp[i][i] = freq[i];
            opt[i][i] = i;
        }
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                int start = opt[i][j - 1];
                int end = opt[i + 1][j];
                for (int k = start; k <= end; k++) {
                    int left = k > i ? dp[i][k - 1] : 0;
                    int right = k < j ? dp[k + 1][j] : 0;
                    int cost = left + right + sum(prefix, i, j);
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        opt[i][j] = k;
                    }
                }
            }
        }
        return dp[0][n - 1];
    }

    private static int sum(int[] prefix, int l, int r) {
        return prefix[r + 1] - prefix[l];
    }

    public static long fibonacciRolling(int n) {
        if (n <= 1) {
            return n;
        }
        long a = 0;
        long b = 1;
        for (int i = 2; i <= n; i++) {
            long c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    public static long fibonacciMemo(int n) {
        long[] memo = new long[n + 1];
        Arrays.fill(memo, -1);
        return fibMemo(n, memo);
    }

    private static long fibMemo(int n, long[] memo) {
        if (n <= 1) {
            return n;
        }
        if (memo[n] != -1) {
            return memo[n];
        }
        memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
        return memo[n];
    }

    public static long fibonacciTabulation(int n) {
        if (n <= 1) {
            return n;
        }
        long[] dp = new long[n + 1];
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    public static class Line {
        long m;
        long b;

        Line(long m, long b) {
            this.m = m;
            this.b = b;
        }

        long value(long x) {
            return m * x + b;
        }
    }

    public static class LiChaoTree {
        private final long minX;
        private final long maxX;
        private Node root;

        private static class Node {
            Line line;
            Node left;
            Node right;

            Node(Line line) {
                this.line = line;
            }
        }

        public LiChaoTree(long minX, long maxX) {
            this.minX = minX;
            this.maxX = maxX;
        }

        public void addLine(long m, long b) {
            Line line = new Line(m, b);
            root = insert(root, minX, maxX, line);
        }

        public long query(long x) {
            return query(root, minX, maxX, x);
        }

        private Node insert(Node node, long l, long r, Line line) {
            if (node == null) {
                return new Node(line);
            }
            long mid = (l + r) >>> 1;
            boolean left = line.value(l) < node.line.value(l);
            boolean middle = line.value(mid) < node.line.value(mid);
            if (middle) {
                Line tmp = node.line;
                node.line = line;
                line = tmp;
            }
            if (r - l == 0) {
                return node;
            } else if (left != middle) {
                node.left = insert(node.left, l, mid, line);
            } else {
                node.right = insert(node.right, mid + 1, r, line);
            }
            return node;
        }

        private long query(Node node, long l, long r, long x) {
            if (node == null) {
                return Long.MAX_VALUE / 4;
            }
            long res = node.line.value(x);
            if (l == r) {
                return res;
            }
            long mid = (l + r) >>> 1;
            if (x <= mid) {
                return Math.min(res, query(node.left, l, mid, x));
            }
            return Math.min(res, query(node.right, mid + 1, r, x));
        }
    }
}
