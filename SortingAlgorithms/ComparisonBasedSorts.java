import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ComparisonBasedSorts {
    private ComparisonBasedSorts() {
    }

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int t = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = t;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            int t = arr[i];
            arr[i] = arr[min];
            arr[min] = t;
        }
    }

    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static void gnomeSort(int[] arr) {
        int i = 0;
        while (i < arr.length) {
            if (i == 0 || arr[i] >= arr[i - 1]) {
                i++;
            } else {
                int t = arr[i];
                arr[i] = arr[i - 1];
                arr[i - 1] = t;
                i--;
            }
        }
    }

    public static void cocktailSort(int[] arr) {
        int start = 0;
        int end = arr.length - 1;
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = start; i < end; i++) {
                if (arr[i] > arr[i + 1]) {
                    int t = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = t;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
            swapped = false;
            end--;
            for (int i = end; i > start; i--) {
                if (arr[i - 1] > arr[i]) {
                    int t = arr[i];
                    arr[i] = arr[i - 1];
                    arr[i - 1] = t;
                    swapped = true;
                }
            }
            start++;
        }
    }

    public static void mergeSort(int[] arr) {
        if (arr.length <= 1) {
            return;
        }
        int[] temp = new int[arr.length];
        mergeSort(arr, temp, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int[] temp, int l, int r) {
        if (l >= r) {
            return;
        }
        int m = (l + r) >>> 1;
        mergeSort(arr, temp, l, m);
        mergeSort(arr, temp, m + 1, r);
        merge(arr, temp, l, m, r);
    }

    private static void merge(int[] arr, int[] temp, int l, int m, int r) {
        int i = l;
        int j = m + 1;
        int k = l;
        while (i <= m && j <= r) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        while (i <= m) {
            temp[k++] = arr[i++];
        }
        while (j <= r) {
            temp[k++] = arr[j++];
        }
        for (int idx = l; idx <= r; idx++) {
            arr[idx] = temp[idx];
        }
    }

    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1, new Random());
    }

    private static void quickSort(int[] arr, int l, int r, Random rng) {
        if (l >= r) {
            return;
        }
        int pivotIndex = l + rng.nextInt(r - l + 1);
        int pivot = arr[pivotIndex];
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
            quickSort(arr, l, j, rng);
        }
        if (i < r) {
            quickSort(arr, i, r, rng);
        }
    }

    public static void heapSort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }
        for (int i = n - 1; i >= 0; i--) {
            int t = arr[0];
            arr[0] = arr[i];
            arr[i] = t;
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        if (l < n && arr[l] > arr[largest]) {
            largest = l;
        }
        if (r < n && arr[r] > arr[largest]) {
            largest = r;
        }
        if (largest != i) {
            int t = arr[i];
            arr[i] = arr[largest];
            arr[largest] = t;
            heapify(arr, n, largest);
        }
    }

    public static void shellSort(int[] arr) {
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < arr.length; i++) {
                int temp = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
        }
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int v) {
            val = v;
        }
    }

    public static int[] treeSort(int[] arr) {
        TreeNode root = null;
        for (int x : arr) {
            root = insert(root, x);
        }
        List<Integer> res = new ArrayList<>();
        inorder(root, res);
        int[] out = new int[res.size()];
        for (int i = 0; i < res.size(); i++) {
            out[i] = res.get(i);
        }
        return out;
    }

    private static TreeNode insert(TreeNode root, int x) {
        if (root == null) {
            return new TreeNode(x);
        }
        if (x < root.val) {
            root.left = insert(root.left, x);
        } else {
            root.right = insert(root.right, x);
        }
        return root;
    }

    private static void inorder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        inorder(root.left, res);
        res.add(root.val);
        inorder(root.right, res);
    }

    public static void introSort(int[] arr) {
        int depth = 2 * (int) (Math.log(arr.length) / Math.log(2));
        introSort(arr, 0, arr.length - 1, depth);
    }

    private static void introSort(int[] arr, int l, int r, int depth) {
        if (l >= r) {
            return;
        }
        if (depth == 0) {
            int[] tmp = Arrays.copyOfRange(arr, l, r + 1);
            heapSort(tmp);
            for (int i = 0; i < tmp.length; i++) {
                arr[l + i] = tmp[i];
            }
            return;
        }
        int pivot = arr[(l + r) >>> 1];
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
            introSort(arr, l, j, depth - 1);
        }
        if (i < r) {
            introSort(arr, i, r, depth - 1);
        }
    }

    public static void timSort(int[] arr) {
        Arrays.sort(arr);
    }
}
