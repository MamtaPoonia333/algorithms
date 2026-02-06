import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class SpecialPurposeSorts {
    private SpecialPurposeSorts() {
    }

    public static List<Integer> topologicalSortKahn(int n, List<List<Integer>> adj) {
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                indeg[v]++;
            }
        }
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                q.add(i);
            }
        }
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                indeg[v]--;
                if (indeg[v] == 0) {
                    q.add(v);
                }
            }
        }
        return order;
    }

    public static void cycleSort(int[] arr) {
        int n = arr.length;
        for (int cycleStart = 0; cycleStart < n - 1; cycleStart++) {
            int item = arr[cycleStart];
            int pos = cycleStart;
            for (int i = cycleStart + 1; i < n; i++) {
                if (arr[i] < item) {
                    pos++;
                }
            }
            if (pos == cycleStart) {
                continue;
            }
            while (item == arr[pos]) {
                pos++;
            }
            int temp = arr[pos];
            arr[pos] = item;
            item = temp;
            while (pos != cycleStart) {
                pos = cycleStart;
                for (int i = cycleStart + 1; i < n; i++) {
                    if (arr[i] < item) {
                        pos++;
                    }
                }
                while (item == arr[pos]) {
                    pos++;
                }
                temp = arr[pos];
                arr[pos] = item;
                item = temp;
            }
        }
    }

    public static void pancakeSort(int[] arr) {
        for (int curr = arr.length; curr > 1; curr--) {
            int maxIdx = 0;
            for (int i = 1; i < curr; i++) {
                if (arr[i] > arr[maxIdx]) {
                    maxIdx = i;
                }
            }
            if (maxIdx == curr - 1) {
                continue;
            }
            flip(arr, maxIdx);
            flip(arr, curr - 1);
        }
    }

    private static void flip(int[] arr, int i) {
        int l = 0;
        int r = i;
        while (l < r) {
            int t = arr[l];
            arr[l] = arr[r];
            arr[r] = t;
            l++;
            r--;
        }
    }

    public static void bogoSort(int[] arr) {
        Random rng = new Random();
        while (!isSorted(arr)) {
            for (int i = arr.length - 1; i > 0; i--) {
                int j = rng.nextInt(i + 1);
                int t = arr[i];
                arr[i] = arr[j];
                arr[j] = t;
            }
        }
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
