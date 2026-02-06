import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NumberTheory {
    private NumberTheory() {
    }

    public static boolean[] sieveOfEratosthenes(int n) {
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

    public static boolean[] sieveOfAtkin(int limit) {
        boolean[] sieve = new boolean[limit + 1];
        int sqrt = (int) Math.sqrt(limit);
        for (int x = 1; x <= sqrt; x++) {
            for (int y = 1; y <= sqrt; y++) {
                int n = 4 * x * x + y * y;
                if (n <= limit && (n % 12 == 1 || n % 12 == 5)) {
                    sieve[n] = !sieve[n];
                }
                n = 3 * x * x + y * y;
                if (n <= limit && n % 12 == 7) {
                    sieve[n] = !sieve[n];
                }
                n = 3 * x * x - y * y;
                if (x > y && n <= limit && n % 12 == 11) {
                    sieve[n] = !sieve[n];
                }
            }
        }
        if (limit >= 2) {
            sieve[2] = true;
        }
        if (limit >= 3) {
            sieve[3] = true;
        }
        for (int i = 5; i <= sqrt; i++) {
            if (sieve[i]) {
                int k = i * i;
                for (int j = k; j <= limit; j += k) {
                    sieve[j] = false;
                }
            }
        }
        return sieve;
    }

    public static List<Integer> segmentedSieve(int low, int high) {
        int limit = (int) Math.sqrt(high) + 1;
        boolean[] base = sieveOfEratosthenes(limit);
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (base[i]) {
                primes.add(i);
            }
        }
        boolean[] isPrime = new boolean[high - low + 1];
        Arrays.fill(isPrime, true);
        for (int p : primes) {
            int start = Math.max(p * p, ((low + p - 1) / p) * p);
            for (int j = start; j <= high; j += p) {
                isPrime[j - low] = false;
            }
        }
        List<Integer> result = new ArrayList<>();
        for (int i = Math.max(low, 2); i <= high; i++) {
            if (isPrime[i - low]) {
                result.add(i);
            }
        }
        return result;
    }

    public static List<Long> trialDivision(long n) {
        List<Long> factors = new ArrayList<>();
        for (long p = 2; p * p <= n; p++) {
            while (n % p == 0) {
                factors.add(p);
                n /= p;
            }
        }
        if (n > 1) {
            factors.add(n);
        }
        return factors;
    }

    public static List<Long> primeFactorization(long n) {
        List<Long> factors = new ArrayList<>();
        if (n <= 1) {
            return factors;
        }
        while (n % 2 == 0) {
            factors.add(2L);
            n /= 2;
        }
        for (long p = 3; p * p <= n; p += 2) {
            while (n % p == 0) {
                factors.add(p);
                n /= p;
            }
        }
        if (n > 1) {
            factors.add(n);
        }
        return factors;
    }

    public static long fermatFactor(long n) {
        if (n % 2 == 0) {
            return 2;
        }
        long a = (long) Math.ceil(Math.sqrt(n));
        long b2 = a * a - n;
        while (!isPerfectSquare(b2)) {
            a++;
            b2 = a * a - n;
        }
        long b = (long) Math.sqrt(b2);
        return a - b;
    }

    private static boolean isPerfectSquare(long x) {
        if (x < 0) {
            return false;
        }
        long r = (long) Math.sqrt(x);
        return r * r == x || (r + 1) * (r + 1) == x;
    }

    public static BigInteger pollardsRho(BigInteger n) {
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return BigInteger.TWO;
        }
        if (n.isProbablePrime(20)) {
            return n;
        }
        Random rng = new Random();
        while (true) {
            BigInteger c = new BigInteger(n.bitLength(), rng).mod(n.subtract(BigInteger.ONE)).add(BigInteger.ONE);
            BigInteger x = new BigInteger(n.bitLength(), rng).mod(n.subtract(BigInteger.ONE)).add(BigInteger.ONE);
            BigInteger y = x;
            BigInteger d = BigInteger.ONE;
            while (d.equals(BigInteger.ONE)) {
                x = f(x, c, n);
                y = f(f(y, c, n), c, n);
                d = x.subtract(y).abs().gcd(n);
            }
            if (!d.equals(n)) {
                return d;
            }
        }
    }

    private static BigInteger f(BigInteger x, BigInteger c, BigInteger mod) {
        return x.multiply(x).mod(mod).add(c).mod(mod);
    }
}
