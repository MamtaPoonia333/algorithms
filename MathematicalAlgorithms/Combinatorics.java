import java.math.BigInteger;

public class Combinatorics {
    private Combinatorics() {
    }

    public static BigInteger factorial(int n) {
        BigInteger res = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            res = res.multiply(BigInteger.valueOf(i));
        }
        return res;
    }

    public static BigInteger nCr(int n, int r) {
        if (r < 0 || r > n) {
            return BigInteger.ZERO;
        }
        r = Math.min(r, n - r);
        BigInteger res = BigInteger.ONE;
        for (int i = 1; i <= r; i++) {
            res = res.multiply(BigInteger.valueOf(n - r + i));
            res = res.divide(BigInteger.valueOf(i));
        }
        return res;
    }

    public static BigInteger nPr(int n, int r) {
        if (r < 0 || r > n) {
            return BigInteger.ZERO;
        }
        BigInteger res = BigInteger.ONE;
        for (int i = 0; i < r; i++) {
            res = res.multiply(BigInteger.valueOf(n - i));
        }
        return res;
    }

    public static BigInteger catalanNumber(int n) {
        return nCr(2 * n, n).divide(BigInteger.valueOf(n + 1));
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

    public static long[][] pascalsTriangle(int rows) {
        long[][] tri = new long[rows][];
        for (int i = 0; i < rows; i++) {
            tri[i] = new long[i + 1];
            tri[i][0] = 1;
            tri[i][i] = 1;
            for (int j = 1; j < i; j++) {
                tri[i][j] = tri[i - 1][j - 1] + tri[i - 1][j];
            }
        }
        return tri;
    }

    public static BigInteger binomialCoefficient(int n, int k) {
        return nCr(n, k);
    }

    public static BigInteger starsAndBars(int items, int bins) {
        if (bins <= 0) {
            return BigInteger.ZERO;
        }
        return nCr(items + bins - 1, bins - 1);
    }

    public static long inclusionExclusionCount(long n, int[] nums) {
        int m = nums.length;
        long res = 0;
        for (int mask = 1; mask < (1 << m); mask++) {
            long lcm = 1;
            boolean overflow = false;
            for (int i = 0; i < m; i++) {
                if ((mask & (1 << i)) != 0) {
                    long g = GcdAndLcm.gcd(lcm, nums[i]);
                    long next = lcm / g * nums[i];
                    if (next > n) {
                        overflow = true;
                        break;
                    }
                    lcm = next;
                }
            }
            if (overflow || lcm == 0) {
                continue;
            }
            long count = n / lcm;
            if (Integer.bitCount(mask) % 2 == 1) {
                res += count;
            } else {
                res -= count;
            }
        }
        return res;
    }
}
