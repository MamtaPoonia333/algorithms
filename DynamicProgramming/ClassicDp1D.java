import java.util.Arrays;

public class ClassicDp1D {
    private ClassicDp1D() {
    }

    public static long fibonacci(int n) {
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

    public static long climbingStairs(int n) {
        if (n <= 2) {
            return n;
        }
        long a = 1;
        long b = 2;
        for (int i = 3; i <= n; i++) {
            long c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    public static int houseRobber(int[] nums) {
        int prev2 = 0;
        int prev1 = 0;
        for (int num : nums) {
            int cur = Math.max(prev1, prev2 + num);
            prev2 = prev1;
            prev1 = cur;
        }
        return prev1;
    }

    public static int decodeWays(String s) {
        int n = s.length();
        if (n == 0 || s.charAt(0) == '0') {
            return 0;
        }
        int prev2 = 1;
        int prev1 = 1;
        for (int i = 1; i < n; i++) {
            int cur = 0;
            char c = s.charAt(i);
            if (c != '0') {
                cur += prev1;
            }
            int two = (s.charAt(i - 1) - '0') * 10 + (c - '0');
            if (two >= 10 && two <= 26) {
                cur += prev2;
            }
            prev2 = prev1;
            prev1 = cur;
        }
        return prev1;
    }

    public static boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) {
                return false;
            }
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        return true;
    }

    public static int maxSubarray(int[] nums) {
        int best = nums[0];
        int cur = nums[0];
        for (int i = 1; i < nums.length; i++) {
            cur = Math.max(nums[i], cur + nums[i]);
            best = Math.max(best, cur);
        }
        return best;
    }

    public static int lisLength(int[] nums) {
        int[] tail = new int[nums.length];
        int size = 0;
        for (int num : nums) {
            int i = Arrays.binarySearch(tail, 0, size, num);
            if (i < 0) {
                i = -i - 1;
            }
            tail[i] = num;
            if (i == size) {
                size++;
            }
        }
        return size;
    }

    public static int ldsLength(int[] nums) {
        int[] neg = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            neg[i] = -nums[i];
        }
        return lisLength(neg);
    }

    public static int longestBitonicSubsequence(int[] nums) {
        int n = nums.length;
        int[] lis = new int[n];
        int[] lds = new int[n];
        for (int i = 0; i < n; i++) {
            lis[i] = 1;
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    lis[i] = Math.max(lis[i], lis[j] + 1);
                }
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            lds[i] = 1;
            for (int j = n - 1; j > i; j--) {
                if (nums[j] < nums[i]) {
                    lds[i] = Math.max(lds[i], lds[j] + 1);
                }
            }
        }
        int best = 0;
        for (int i = 0; i < n; i++) {
            best = Math.max(best, lis[i] + lds[i] - 1);
        }
        return best;
    }

    public static int maxSumIncreasingSubsequence(int[] nums) {
        int n = nums.length;
        int[] dp = Arrays.copyOf(nums, n);
        int best = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + nums[i]);
                }
            }
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    public static int coinChangeMinCoins(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    public static long coinChangeCountWays(int[] coins, int amount) {
        long[] dp = new long[amount + 1];
        dp[0] = 1;
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        return dp[amount];
    }

    public static int rodCutting(int[] prices) {
        int n = prices.length;
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int best = 0;
            for (int cut = 1; cut <= i; cut++) {
                best = Math.max(best, prices[cut - 1] + dp[i - cut]);
            }
            dp[i] = best;
        }
        return dp[n];
    }

    public static int minJumpsToEnd(int[] nums) {
        int n = nums.length;
        if (n <= 1) {
            return 0;
        }
        int jumps = 0;
        int curEnd = 0;
        int farthest = 0;
        for (int i = 0; i < n - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == curEnd) {
                jumps++;
                curEnd = farthest;
            }
        }
        return jumps;
    }

    public static int nthUglyNumber(int n) {
        int[] dp = new int[n];
        dp[0] = 1;
        int i2 = 0;
        int i3 = 0;
        int i5 = 0;
        for (int i = 1; i < n; i++) {
            int next = Math.min(dp[i2] * 2, Math.min(dp[i3] * 3, dp[i5] * 5));
            dp[i] = next;
            if (next == dp[i2] * 2) {
                i2++;
            }
            if (next == dp[i3] * 3) {
                i3++;
            }
            if (next == dp[i5] * 5) {
                i5++;
            }
        }
        return dp[n - 1];
    }

    public static long catalanNumber(int n) {
        long[] dp = new long[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            long sum = 0;
            for (int j = 0; j < i; j++) {
                sum += dp[j] * dp[i - 1 - j];
            }
            dp[i] = sum;
        }
        return dp[n];
    }
}
