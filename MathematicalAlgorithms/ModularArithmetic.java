import java.math.BigInteger;
import java.util.Arrays;

public class ModularArithmetic {
    private ModularArithmetic() {
    }

    public static long modPow(long base, long exp, long mod) {
        long res = 1 % mod;
        long b = base % mod;
        long e = exp;
        while (e > 0) {
            if ((e & 1) == 1) {
                res = mulMod(res, b, mod);
            }
            b = mulMod(b, b, mod);
            e >>= 1;
        }
        return res;
    }

    private static long mulMod(long a, long b, long mod) {
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(mod)).longValue();
    }

    public static long modInverse(long a, long mod) {
        long[] eg = GcdAndLcm.extendedGcd(a, mod);
        if (eg[0] != 1 && eg[0] != -1) {
            throw new IllegalArgumentException("Inverse does not exist");
        }
        long x = eg[1] % mod;
        if (x < 0) {
            x += mod;
        }
        return x;
    }

    public static long modInverseFermat(long a, long primeMod) {
        return modPow(a, primeMod - 2, primeMod);
    }

    public static long eulerTotient(long n) {
        long result = n;
        for (long p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                while (n % p == 0) {
                    n /= p;
                }
                result -= result / p;
            }
        }
        if (n > 1) {
            result -= result / n;
        }
        return result;
    }

    public static long[] chineseRemainder(long[] r, long[] m) {
        long x = r[0];
        long mod = m[0];
        for (int i = 1; i < r.length; i++) {
            long a = x;
            long b = r[i];
            long m1 = mod;
            long m2 = m[i];
            long[] eg = GcdAndLcm.extendedGcd(m1, m2);
            long g = eg[0];
            if ((b - a) % g != 0) {
                return new long[]{0, -1};
            }
            long lcm = m1 / g * m2;
            long t = (b - a) / g;
            long inv = eg[1];
            long k = mulMod(t % (m2 / g), inv % (m2 / g), m2 / g);
            x = (a + mulMod(k, m1, lcm)) % lcm;
            if (x < 0) {
                x += lcm;
            }
            mod = lcm;
        }
        return new long[]{x, mod};
    }

    public static boolean wilsonTheorem(int p) {
        if (p < 2) {
            return false;
        }
        BigInteger mod = BigInteger.valueOf(p);
        BigInteger fact = BigInteger.ONE;
        for (int i = 2; i < p; i++) {
            fact = fact.multiply(BigInteger.valueOf(i)).mod(mod);
        }
        return fact.add(BigInteger.ONE).mod(mod).equals(BigInteger.ZERO);
    }

    public static boolean[] sievePrimes(int n) {
        boolean[] prime = new boolean[n + 1];
        Arrays.fill(prime, true);
        if (n >= 0) {
            prime[0] = false;
        }
        if (n >= 1) {
            prime[1] = false;
        }
        for (int i = 2; (long) i * i <= n; i++) {
            if (prime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    prime[j] = false;
                }
            }
        }
        return prime;
    }
}
