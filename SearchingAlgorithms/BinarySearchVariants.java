public class BinarySearchVariants {
    private BinarySearchVariants() {
    }

    public static int binarySearchIterative(int[] arr, int target) {
        int l = 0;
        int r = arr.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (arr[m] == target) {
                return m;
            }
            if (arr[m] < target) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return -1;
    }

    public static int binarySearchRecursive(int[] arr, int target) {
        return binarySearchRecursive(arr, target, 0, arr.length - 1);
    }

    private static int binarySearchRecursive(int[] arr, int target, int l, int r) {
        if (l > r) {
            return -1;
        }
        int m = l + (r - l) / 2;
        if (arr[m] == target) {
            return m;
        }
        if (arr[m] < target) {
            return binarySearchRecursive(arr, target, m + 1, r);
        }
        return binarySearchRecursive(arr, target, l, m - 1);
    }

    public static int exponentialSearch(int[] arr, int target) {
        if (arr.length == 0) {
            return -1;
        }
        if (arr[0] == target) {
            return 0;
        }
        int i = 1;
        while (i < arr.length && arr[i] <= target) {
            i *= 2;
        }
        return binarySearchRecursive(arr, target, i / 2, Math.min(i, arr.length - 1));
    }

    public static int interpolationSearch(int[] arr, int target) {
        int l = 0;
        int r = arr.length - 1;
        while (l <= r && target >= arr[l] && target <= arr[r]) {
            if (l == r) {
                return arr[l] == target ? l : -1;
            }
            int pos = l + (int) ((long) (target - arr[l]) * (r - l) / (arr[r] - arr[l]));
            if (arr[pos] == target) {
                return pos;
            }
            if (arr[pos] < target) {
                l = pos + 1;
            } else {
                r = pos - 1;
            }
        }
        return -1;
    }

    public static int ternarySearch(int[] arr, int target) {
        int l = 0;
        int r = arr.length - 1;
        while (l <= r) {
            int mid1 = l + (r - l) / 3;
            int mid2 = r - (r - l) / 3;
            if (arr[mid1] == target) {
                return mid1;
            }
            if (arr[mid2] == target) {
                return mid2;
            }
            if (target < arr[mid1]) {
                r = mid1 - 1;
            } else if (target > arr[mid2]) {
                l = mid2 + 1;
            } else {
                l = mid1 + 1;
                r = mid2 - 1;
            }
        }
        return -1;
    }

    public static int fibonacciSearch(int[] arr, int target) {
        int n = arr.length;
        int fibMm2 = 0;
        int fibMm1 = 1;
        int fibM = fibMm2 + fibMm1;
        while (fibM < n) {
            fibMm2 = fibMm1;
            fibMm1 = fibM;
            fibM = fibMm2 + fibMm1;
        }
        int offset = -1;
        while (fibM > 1) {
            int i = Math.min(offset + fibMm2, n - 1);
            if (arr[i] < target) {
                fibM = fibMm1;
                fibMm1 = fibMm2;
                fibMm2 = fibM - fibMm1;
                offset = i;
            } else if (arr[i] > target) {
                fibM = fibMm2;
                fibMm1 = fibMm1 - fibMm2;
                fibMm2 = fibM - fibMm1;
            } else {
                return i;
            }
        }
        if (fibMm1 == 1 && offset + 1 < n && arr[offset + 1] == target) {
            return offset + 1;
        }
        return -1;
    }

    public static int lowerBound(int[] arr, int target) {
        int l = 0;
        int r = arr.length;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (arr[m] < target) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        return l;
    }

    public static int upperBound(int[] arr, int target) {
        int l = 0;
        int r = arr.length;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (arr[m] <= target) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        return l;
    }
}
