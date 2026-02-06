import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringProcessing {
    private StringProcessing() {
    }

    public static String manacherLongestPalindrome(String s) {
        if (s.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("^");
        for (int i = 0; i < s.length(); i++) {
            sb.append('#').append(s.charAt(i));
        }
        sb.append("#$");
        String t = sb.toString();
        int n = t.length();
        int[] p = new int[n];
        int center = 0;
        int right = 0;
        for (int i = 1; i < n - 1; i++) {
            int mirror = 2 * center - i;
            if (i < right) {
                p[i] = Math.min(right - i, p[mirror]);
            }
            while (t.charAt(i + 1 + p[i]) == t.charAt(i - 1 - p[i])) {
                p[i]++;
            }
            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }
        }
        int maxLen = 0;
        int centerIndex = 0;
        for (int i = 1; i < n - 1; i++) {
            if (p[i] > maxLen) {
                maxLen = p[i];
                centerIndex = i;
            }
        }
        int start = (centerIndex - maxLen) / 2;
        return s.substring(start, start + maxLen);
    }

    public static int longestCommonSubstringLength(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        int best = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    best = Math.max(best, dp[i][j]);
                }
            }
        }
        return best;
    }

    public static int lcsLength(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }

    public static int editDistance(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[n][m];
    }

    public static class RollingHash {
        private final long mod = 1_000_000_007L;
        private final long base = 911382323L;
        private final long[] prefix;
        private final long[] power;

        public RollingHash(String s) {
            int n = s.length();
            prefix = new long[n + 1];
            power = new long[n + 1];
            power[0] = 1;
            for (int i = 0; i < n; i++) {
                prefix[i + 1] = (prefix[i] * base + s.charAt(i)) % mod;
                power[i + 1] = (power[i] * base) % mod;
            }
        }

        public long getHash(int l, int r) {
            long res = (prefix[r] - prefix[l] * power[r - l]) % mod;
            if (res < 0) {
                res += mod;
            }
            return res;
        }
    }

    public static class Trie {
        public static class Node {
            Node[] next = new Node[26];
            boolean end;
        }

        public final Node root = new Node();

        public void insert(String word) {
            Node cur = root;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (cur.next[idx] == null) {
                    cur.next[idx] = new Node();
                }
                cur = cur.next[idx];
            }
            cur.end = true;
        }

        public boolean search(String word) {
            Node cur = root;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (cur.next[idx] == null) {
                    return false;
                }
                cur = cur.next[idx];
            }
            return cur.end;
        }
    }

    public static class SuffixTree {
        public static class Node {
            java.util.Map<Character, Node> next = new java.util.HashMap<>();
        }

        public final Node root = new Node();

        public SuffixTree(String s) {
            for (int i = 0; i < s.length(); i++) {
                insertSuffix(s.substring(i));
            }
        }

        private void insertSuffix(String suffix) {
            Node cur = root;
            for (char c : suffix.toCharArray()) {
                cur.next.putIfAbsent(c, new Node());
                cur = cur.next.get(c);
            }
        }

        public boolean contains(String pattern) {
            Node cur = root;
            for (char c : pattern.toCharArray()) {
                if (!cur.next.containsKey(c)) {
                    return false;
                }
                cur = cur.next.get(c);
            }
            return true;
        }
    }

    public static int[] suffixArray(String s) {
        int n = s.length();
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> s.substring(a).compareTo(s.substring(b)));
        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = order[i];
        }
        return sa;
    }

    public static String burrowsWheelerTransform(String s) {
        int n = s.length();
        String doubled = s + s;
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx[i] = i;
        }
        Arrays.sort(idx, (a, b) -> doubled.substring(a, a + n).compareTo(doubled.substring(b, b + n)));
        StringBuilder res = new StringBuilder();
        for (int i : idx) {
            int pos = (i + n - 1) % n;
            res.append(s.charAt(pos));
        }
        return res.toString();
    }

    public static List<String> runLengthEncode(String s) {
        List<String> res = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            int j = i;
            while (j < s.length() && s.charAt(j) == s.charAt(i)) {
                j++;
            }
            res.add(s.charAt(i) + String.valueOf(j - i));
            i = j;
        }
        return res;
    }
}
