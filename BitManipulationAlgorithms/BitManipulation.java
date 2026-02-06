import java.util.ArrayList;
import java.util.List;

public class BitManipulation {
    private BitManipulation() {
    }

    public static boolean isPowerOfTwo(long n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    public static int countSetBits(int n) {
        int count = 0;
        int x = n;
        while (x != 0) {
            x &= (x - 1);
            count++;
        }
        return count;
    }

    public static int xor1ToN(int n) {
        int r = n & 3;
        if (r == 0) {
            return n;
        }
        if (r == 1) {
            return 1;
        }
        if (r == 2) {
            return n + 1;
        }
        return 0;
    }

    public static int singleNumber(int[] nums) {
        int x = 0;
        for (int n : nums) {
            x ^= n;
        }
        return x;
    }

    public static int[] twoSingleNumbers(int[] nums) {
        int xor = 0;
        for (int n : nums) {
            xor ^= n;
        }
        int diff = xor & -xor;
        int a = 0;
        int b = 0;
        for (int n : nums) {
            if ((n & diff) == 0) {
                a ^= n;
            } else {
                b ^= n;
            }
        }
        return new int[]{a, b};
    }

    public static List<List<Integer>> subsets(int[] nums) {
        int n = nums.length;
        int total = 1 << n;
        List<List<Integer>> res = new ArrayList<>();
        for (int mask = 0; mask < total; mask++) {
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    subset.add(nums[i]);
                }
            }
            res.add(subset);
        }
        return res;
    }

    public static boolean[] bitwiseSieve(int n) {
        boolean[] isPrime = new boolean[n + 1];
        if (n >= 2) {
            for (int i = 2; i <= n; i++) {
                isPrime[i] = true;
            }
        }
        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        return isPrime;
    }

    public static List<Integer> grayCode(int n) {
        List<Integer> res = new ArrayList<>();
        int total = 1 << n;
        for (int i = 0; i < total; i++) {
            res.add(i ^ (i >> 1));
        }
        return res;
    }

    public static int reverseBits(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            res = (res << 1) | (n & 1);
            n >>>= 1;
        }
        return res;
    }

    public static int swapBits(int n, int i, int j) {
        int bitI = (n >> i) & 1;
        int bitJ = (n >> j) & 1;
        if (bitI == bitJ) {
            return n;
        }
        int mask = (1 << i) | (1 << j);
        return n ^ mask;
    }

    public static int missingNumber(int[] nums) {
        int n = nums.length;
        int xor = 0;
        for (int i = 0; i <= n; i++) {
            xor ^= i;
        }
        for (int x : nums) {
            xor ^= x;
        }
        return xor;
    }

    public static List<Integer> powerSet(int n) {
        List<Integer> res = new ArrayList<>();
        int total = 1 << n;
        for (int mask = 0; mask < total; mask++) {
            res.add(mask);
        }
        return res;
    }
}
