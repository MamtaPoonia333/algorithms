import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Randomized {
    private static final Random RNG = new Random();

    private Randomized() {
    }

    public static void randomizedQuickSort(int[] arr) {
        randomizedQuickSort(arr, 0, arr.length - 1);
    }

    private static void randomizedQuickSort(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        int pivotIdx = l + RNG.nextInt(r - l + 1);
        int pivot = arr[pivotIdx];
        int i = l;
        int j = r;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                int t = arr[i];
                arr[i] = arr[j];
                arr[j] = t;
                i++;
                j--;
            }
        }
        if (l < j) {
            randomizedQuickSort(arr, l, j);
        }
        if (i < r) {
            randomizedQuickSort(arr, i, r);
        }
    }

    public static int randomizedQuickSelect(int[] arr, int k) {
        if (k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("k out of bounds");
        }
        int l = 0;
        int r = arr.length - 1;
        while (l <= r) {
            int pivotIdx = l + RNG.nextInt(r - l + 1);
            int pivot = arr[pivotIdx];
            int i = l;
            int j = r;
            while (i <= j) {
                while (arr[i] < pivot) {
                    i++;
                }
                while (arr[j] > pivot) {
                    j--;
                }
                if (i <= j) {
                    int t = arr[i];
                    arr[i] = arr[j];
                    arr[j] = t;
                    i++;
                    j--;
                }
            }
            if (k <= j) {
                r = j;
            } else if (k >= i) {
                l = i;
            } else {
                return arr[k];
            }
        }
        return arr[k];
    }

    public static double monteCarloPi(int samples) {
        int inside = 0;
        for (int i = 0; i < samples; i++) {
            double x = RNG.nextDouble();
            double y = RNG.nextDouble();
            if (x * x + y * y <= 1.0) {
                inside++;
            }
        }
        return 4.0 * inside / samples;
    }

    public static boolean lasVegasProbablePrime(long n, int iterations) {
        if (n < 2) {
            return false;
        }
        if (n % 2 == 0) {
            return n == 2;
        }
        for (int i = 0; i < iterations; i++) {
            long a = 2 + Math.abs(RNG.nextLong()) % (n - 3);
            if (modPow(a, n - 1, n) != 1) {
                return false;
            }
        }
        return true;
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

    private static long mulMod(long a, long b, long mod) {
        return java.math.BigInteger.valueOf(a)
            .multiply(java.math.BigInteger.valueOf(b))
            .mod(java.math.BigInteger.valueOf(mod))
            .longValue();
    }

    public static int reservoirSample(int[] stream) {
        int result = stream[0];
        for (int i = 1; i < stream.length; i++) {
            int j = RNG.nextInt(i + 1);
            if (j == 0) {
                result = stream[i];
            }
        }
        return result;
    }

    public static void fisherYatesShuffle(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = RNG.nextInt(i + 1);
            int t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
        }
    }

    public static List<int[]> randomWalk2D(int steps) {
        int x = 0;
        int y = 0;
        List<int[]> path = new ArrayList<>();
        path.add(new int[]{x, y});
        for (int i = 0; i < steps; i++) {
            int dir = RNG.nextInt(4);
            if (dir == 0) {
                x++;
            } else if (dir == 1) {
                x--;
            } else if (dir == 2) {
                y++;
            } else {
                y--;
            }
            path.add(new int[]{x, y});
        }
        return path;
    }

    public static class SkipList {
        private static class Node {
            int key;
            Node[] next;

            Node(int key, int level) {
                this.key = key;
                this.next = new Node[level + 1];
            }
        }

        private static final int MAX_LEVEL = 16;
        private final Node head = new Node(Integer.MIN_VALUE, MAX_LEVEL);
        private int level = 0;

        public boolean contains(int key) {
            Node cur = head;
            for (int i = level; i >= 0; i--) {
                while (cur.next[i] != null && cur.next[i].key < key) {
                    cur = cur.next[i];
                }
            }
            cur = cur.next[0];
            return cur != null && cur.key == key;
        }

        public void insert(int key) {
            Node[] update = new Node[MAX_LEVEL + 1];
            Node cur = head;
            for (int i = level; i >= 0; i--) {
                while (cur.next[i] != null && cur.next[i].key < key) {
                    cur = cur.next[i];
                }
                update[i] = cur;
            }
            cur = cur.next[0];
            if (cur != null && cur.key == key) {
                return;
            }
            int lvl = randomLevel();
            if (lvl > level) {
                for (int i = level + 1; i <= lvl; i++) {
                    update[i] = head;
                }
                level = lvl;
            }
            Node node = new Node(key, lvl);
            for (int i = 0; i <= lvl; i++) {
                node.next[i] = update[i].next[i];
                update[i].next[i] = node;
            }
        }

        public void delete(int key) {
            Node[] update = new Node[MAX_LEVEL + 1];
            Node cur = head;
            for (int i = level; i >= 0; i--) {
                while (cur.next[i] != null && cur.next[i].key < key) {
                    cur = cur.next[i];
                }
                update[i] = cur;
            }
            cur = cur.next[0];
            if (cur == null || cur.key != key) {
                return;
            }
            for (int i = 0; i <= level; i++) {
                if (update[i].next[i] != cur) {
                    break;
                }
                update[i].next[i] = cur.next[i];
            }
            while (level > 0 && head.next[level] == null) {
                level--;
            }
        }

        private int randomLevel() {
            int lvl = 0;
            while (lvl < MAX_LEVEL && RNG.nextBoolean()) {
                lvl++;
            }
            return lvl;
        }
    }
}
