import java.util.Arrays;

public class Algebra {
    private Algebra() {
    }

    public static class Complex {
        public double re;
        public double im;

        public Complex(double re, double im) {
            this.re = re;
            this.im = im;
        }

        public Complex add(Complex o) {
            return new Complex(re + o.re, im + o.im);
        }

        public Complex sub(Complex o) {
            return new Complex(re - o.re, im - o.im);
        }

        public Complex mul(Complex o) {
            return new Complex(re * o.re - im * o.im, re * o.im + im * o.re);
        }
    }

    public static void fft(Complex[] a, boolean invert) {
        int n = a.length;
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >> 1;
            for (; j >= bit; bit >>= 1) {
                j -= bit;
            }
            j += bit;
            if (i < j) {
                Complex tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
        }
        for (int len = 2; len <= n; len <<= 1) {
            double ang = 2 * Math.PI / len * (invert ? -1 : 1);
            Complex wlen = new Complex(Math.cos(ang), Math.sin(ang));
            for (int i = 0; i < n; i += len) {
                Complex w = new Complex(1, 0);
                for (int j = 0; j < len / 2; j++) {
                    Complex u = a[i + j];
                    Complex v = a[i + j + len / 2].mul(w);
                    a[i + j] = u.add(v);
                    a[i + j + len / 2] = u.sub(v);
                    w = w.mul(wlen);
                }
            }
        }
        if (invert) {
            for (int i = 0; i < n; i++) {
                a[i].re /= n;
                a[i].im /= n;
            }
        }
    }

    public static int[] convolution(int[] a, int[] b) {
        int n = 1;
        while (n < a.length + b.length) {
            n <<= 1;
        }
        Complex[] fa = new Complex[n];
        Complex[] fb = new Complex[n];
        for (int i = 0; i < n; i++) {
            fa[i] = new Complex(i < a.length ? a[i] : 0, 0);
            fb[i] = new Complex(i < b.length ? b[i] : 0, 0);
        }
        fft(fa, false);
        fft(fb, false);
        for (int i = 0; i < n; i++) {
            fa[i] = fa[i].mul(fb[i]);
        }
        fft(fa, true);
        int[] res = new int[a.length + b.length - 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) Math.round(fa[i].re);
        }
        return res;
    }

    public static long[] ntt(long[] a, boolean invert, long mod, long root) {
        int n = a.length;
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >> 1;
            for (; j >= bit; bit >>= 1) {
                j -= bit;
            }
            j += bit;
            if (i < j) {
                long tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
        }
        for (int len = 2; len <= n; len <<= 1) {
            long wlen = modPow(root, (mod - 1) / len, mod);
            if (invert) {
                wlen = modInverse(wlen, mod);
            }
            for (int i = 0; i < n; i += len) {
                long w = 1;
                for (int j = 0; j < len / 2; j++) {
                    long u = a[i + j];
                    long v = mulMod(a[i + j + len / 2], w, mod);
                    a[i + j] = (u + v) % mod;
                    a[i + j + len / 2] = (u - v + mod) % mod;
                    w = mulMod(w, wlen, mod);
                }
            }
        }
        if (invert) {
            long nInv = modInverse(n, mod);
            for (int i = 0; i < n; i++) {
                a[i] = mulMod(a[i], nInv, mod);
            }
        }
        return a;
    }

    public static long[] convolutionNtt(long[] a, long[] b, long mod, long root) {
        int n = 1;
        while (n < a.length + b.length) {
            n <<= 1;
        }
        long[] fa = Arrays.copyOf(a, n);
        long[] fb = Arrays.copyOf(b, n);
        ntt(fa, false, mod, root);
        ntt(fb, false, mod, root);
        for (int i = 0; i < n; i++) {
            fa[i] = mulMod(fa[i], fb[i], mod);
        }
        ntt(fa, true, mod, root);
        return Arrays.copyOf(fa, a.length + b.length - 1);
    }

    public static long[][] matrixMultiply(long[][] a, long[][] b, long mod) {
        int n = a.length;
        int m = b[0].length;
        int k = b.length;
        long[][] res = new long[n][m];
        for (int i = 0; i < n; i++) {
            for (int t = 0; t < k; t++) {
                long val = a[i][t];
                if (val == 0) {
                    continue;
                }
                for (int j = 0; j < m; j++) {
                    long add = val * b[t][j];
                    if (mod > 0) {
                        res[i][j] = (res[i][j] + add) % mod;
                    } else {
                        res[i][j] += add;
                    }
                }
            }
        }
        return res;
    }

    public static long[][] matrixPower(long[][] base, long exp, long mod) {
        int n = base.length;
        long[][] res = new long[n][n];
        for (int i = 0; i < n; i++) {
            res[i][i] = 1;
        }
        long[][] b = base;
        long e = exp;
        while (e > 0) {
            if ((e & 1) == 1) {
                res = matrixMultiply(res, b, mod);
            }
            b = matrixMultiply(b, b, mod);
            e >>= 1;
        }
        return res;
    }

    public static double[] gaussianElimination(double[][] a, double[] b) {
        int n = a.length;
        double[][] mat = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, mat[i], 0, n);
            mat[i][n] = b[i];
        }
        for (int col = 0, row = 0; col < n && row < n; col++, row++) {
            int sel = row;
            for (int i = row; i < n; i++) {
                if (Math.abs(mat[i][col]) > Math.abs(mat[sel][col])) {
                    sel = i;
                }
            }
            if (Math.abs(mat[sel][col]) < 1e-12) {
                continue;
            }
            double[] tmp = mat[sel];
            mat[sel] = mat[row];
            mat[row] = tmp;
            double div = mat[row][col];
            for (int j = col; j <= n; j++) {
                mat[row][j] /= div;
            }
            for (int i = 0; i < n; i++) {
                if (i != row) {
                    double factor = mat[i][col];
                    for (int j = col; j <= n; j++) {
                        mat[i][j] -= factor * mat[row][j];
                    }
                }
            }
        }
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = mat[i][n];
        }
        return x;
    }

    public static long[][] strassenMultiply(long[][] a, long[][] b) {
        int n = a.length;
        if (n <= 2 || n % 2 != 0) {
            return matrixMultiply(a, b, 0);
        }
        int m = n / 2;
        long[][] a11 = subMatrix(a, 0, 0, m);
        long[][] a12 = subMatrix(a, 0, m, m);
        long[][] a21 = subMatrix(a, m, 0, m);
        long[][] a22 = subMatrix(a, m, m, m);
        long[][] b11 = subMatrix(b, 0, 0, m);
        long[][] b12 = subMatrix(b, 0, m, m);
        long[][] b21 = subMatrix(b, m, 0, m);
        long[][] b22 = subMatrix(b, m, m, m);

        long[][] p1 = strassenMultiply(add(a11, a22), add(b11, b22));
        long[][] p2 = strassenMultiply(add(a21, a22), b11);
        long[][] p3 = strassenMultiply(a11, sub(b12, b22));
        long[][] p4 = strassenMultiply(a22, sub(b21, b11));
        long[][] p5 = strassenMultiply(add(a11, a12), b22);
        long[][] p6 = strassenMultiply(sub(a21, a11), add(b11, b12));
        long[][] p7 = strassenMultiply(sub(a12, a22), add(b21, b22));

        long[][] c11 = add(sub(add(p1, p4), p5), p7);
        long[][] c12 = add(p3, p5);
        long[][] c21 = add(p2, p4);
        long[][] c22 = add(sub(add(p1, p3), p2), p6);

        long[][] c = new long[n][n];
        join(c, c11, 0, 0);
        join(c, c12, 0, m);
        join(c, c21, m, 0);
        join(c, c22, m, m);
        return c;
    }

    private static long[][] subMatrix(long[][] a, int row, int col, int size) {
        long[][] res = new long[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(a[row + i], col, res[i], 0, size);
        }
        return res;
    }

    private static void join(long[][] target, long[][] src, int row, int col) {
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, target[row + i], col, src.length);
        }
    }

    private static long[][] add(long[][] a, long[][] b) {
        int n = a.length;
        long[][] res = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = a[i][j] + b[i][j];
            }
        }
        return res;
    }

    private static long[][] sub(long[][] a, long[][] b) {
        int n = a.length;
        long[][] res = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = a[i][j] - b[i][j];
            }
        }
        return res;
    }

    private static long modPow(long base, long exp, long mod) {
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

    private static long modInverse(long a, long mod) {
        long[] eg = GcdAndLcm.extendedGcd(a, mod);
        long x = eg[1] % mod;
        if (x < 0) {
            x += mod;
        }
        return x;
    }

    private static long mulMod(long a, long b, long mod) {
        return java.math.BigInteger.valueOf(a)
            .multiply(java.math.BigInteger.valueOf(b))
            .mod(java.math.BigInteger.valueOf(mod))
            .longValue();
    }
}
