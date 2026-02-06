public class LinearSearchVariants {
    private LinearSearchVariants() {
    }

    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int sentinelLinearSearch(int[] arr, int target) {
        int n = arr.length;
        if (n == 0) {
            return -1;
        }
        int last = arr[n - 1];
        arr[n - 1] = target;
        int i = 0;
        while (arr[i] != target) {
            i++;
        }
        arr[n - 1] = last;
        if (i < n - 1 || arr[n - 1] == target) {
            return i;
        }
        return -1;
    }

    public static int jumpSearch(int[] arr, int target) {
        int n = arr.length;
        if (n == 0) {
            return -1;
        }
        int step = (int) Math.sqrt(n);
        int prev = 0;
        while (prev < n && arr[Math.min(n - 1, prev + step - 1)] < target) {
            prev += step;
        }
        int end = Math.min(n, prev + step);
        for (int i = prev; i < end; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }
}
