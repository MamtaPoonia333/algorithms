public class GcdAndLcm {
    private GcdAndLcm() {
    }

    public static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    public static long[] extendedGcd(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0};
        }
        long[] res = extendedGcd(b, a % b);
        long g = res[0];
        long x = res[2];
        long y = res[1] - (a / b) * res[2];
        return new long[]{g, x, y};
    }

    public static long binaryGcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        int shift = Long.numberOfTrailingZeros(a | b);
        a >>= Long.numberOfTrailingZeros(a);
        while (b != 0) {
            b >>= Long.numberOfTrailingZeros(b);
            if (a > b) {
                long t = a;
                a = b;
                b = t;
            }
            b -= a;
        }
        return a << shift;
    }

    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        return Math.abs(a / gcd(a, b) * b);
    }
}
