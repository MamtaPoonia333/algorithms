import java.util.ArrayList;
import java.util.List;

public class NonComparisonSorts {
    private NonComparisonSorts() {
    }

    public static int[] countingSort(int[] arr, int maxValue) {
        int[] count = new int[maxValue + 1];
        for (int x : arr) {
            count[x]++;
        }
        int[] res = new int[arr.length];
        int idx = 0;
        for (int i = 0; i <= maxValue; i++) {
            while (count[i]-- > 0) {
                res[idx++] = i;
            }
        }
        return res;
    }

    public static void radixSort(int[] arr) {
        int max = 0;
        for (int x : arr) {
            max = Math.max(max, x);
        }
        int exp = 1;
        int n = arr.length;
        int[] tmp = new int[n];
        while (max / exp > 0) {
            int[] count = new int[10];
            for (int x : arr) {
                count[(x / exp) % 10]++;
            }
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
            for (int i = n - 1; i >= 0; i--) {
                int digit = (arr[i] / exp) % 10;
                tmp[--count[digit]] = arr[i];
            }
            System.arraycopy(tmp, 0, arr, 0, n);
            exp *= 10;
        }
    }

    public static double[] bucketSort(double[] arr, int bucketCount) {
        List<List<Double>> buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
        for (double v : arr) {
            int idx = (int) (v * bucketCount);
            if (idx == bucketCount) {
                idx--;
            }
            buckets.get(idx).add(v);
        }
        double[] res = new double[arr.length];
        int pos = 0;
        for (List<Double> bucket : buckets) {
            bucket.sort(Double::compareTo);
            for (double v : bucket) {
                res[pos++] = v;
            }
        }
        return res;
    }

    public static int[] pigeonholeSort(int[] arr) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int x : arr) {
            min = Math.min(min, x);
            max = Math.max(max, x);
        }
        int range = max - min + 1;
        int[] holes = new int[range];
        for (int x : arr) {
            holes[x - min]++;
        }
        int[] res = new int[arr.length];
        int idx = 0;
        for (int i = 0; i < range; i++) {
            while (holes[i]-- > 0) {
                res[idx++] = i + min;
            }
        }
        return res;
    }

    public static void flashSort(int[] arr) {
        int n = arr.length;
        if (n <= 1) {
            return;
        }
        int min = arr[0];
        int max = arr[0];
        int maxIdx = 0;
        for (int i = 1; i < n; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
            if (arr[i] > max) {
                max = arr[i];
                maxIdx = i;
            }
        }
        if (min == max) {
            return;
        }
        int m = (int) (0.45 * n) + 1;
        int[] count = new int[m];
        double c = (m - 1.0) / (max - min);
        for (int x : arr) {
            int k = (int) (c * (x - min));
            count[k]++;
        }
        for (int i = 1; i < m; i++) {
            count[i] += count[i - 1];
        }
        int temp = arr[maxIdx];
        arr[maxIdx] = arr[0];
        arr[0] = temp;
        int move = 0;
        int j = 0;
        int k = m - 1;
        while (move < n - 1) {
            while (j >= count[k]) {
                j++;
                k = (int) (c * (arr[j] - min));
            }
            int flash = arr[j];
            while (j != count[k]) {
                k = (int) (c * (flash - min));
                int dst = --count[k];
                int hold = arr[dst];
                arr[dst] = flash;
                flash = hold;
                move++;
            }
        }
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j2 = i - 1;
            while (j2 >= 0 && arr[j2] > key) {
                arr[j2 + 1] = arr[j2];
                j2--;
            }
            arr[j2 + 1] = key;
        }
    }
}
