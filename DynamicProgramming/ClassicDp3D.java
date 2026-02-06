import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClassicDp3D {
    private ClassicDp3D() {
    }

    public static class Box {
        public final int length;
        public final int width;
        public final int height;

        public Box(int length, int width, int height) {
            this.length = length;
            this.width = width;
            this.height = height;
        }
    }

    public static int maxStackHeight(Box[] boxes) {
        List<Box> all = new ArrayList<>();
        for (Box b : boxes) {
            all.add(new Box(Math.max(b.length, b.width), Math.min(b.length, b.width), b.height));
            all.add(new Box(Math.max(b.width, b.height), Math.min(b.width, b.height), b.length));
            all.add(new Box(Math.max(b.length, b.height), Math.min(b.length, b.height), b.width));
        }
        all.sort(Comparator.comparingInt((Box b) -> b.length * b.width).reversed());
        int n = all.size();
        int[] dp = new int[n];
        int best = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = all.get(i).height;
            for (int j = 0; j < i; j++) {
                if (all.get(j).length > all.get(i).length && all.get(j).width > all.get(i).width) {
                    dp[i] = Math.max(dp[i], dp[j] + all.get(i).height);
                }
            }
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    public static int buildingBridges(int[][] pairs) {
        Arrays.sort(pairs, (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));
        int n = pairs.length;
        int[] dp = new int[n];
        int best = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (pairs[j][1] <= pairs[i][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    public static int maxSumRectangle(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int best = Integer.MIN_VALUE;
        for (int left = 0; left < m; left++) {
            int[] temp = new int[n];
            for (int right = left; right < m; right++) {
                for (int i = 0; i < n; i++) {
                    temp[i] += matrix[i][right];
                }
                best = Math.max(best, kadane(temp));
            }
        }
        return best;
    }

    private static int kadane(int[] arr) {
        int best = arr[0];
        int cur = arr[0];
        for (int i = 1; i < arr.length; i++) {
            cur = Math.max(arr[i], cur + arr[i]);
            best = Math.max(best, cur);
        }
        return best;
    }
}
