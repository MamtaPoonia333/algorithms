import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternMatching {
    private PatternMatching() {
    }

    public static List<Integer> naiveSearch(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        for (int i = 0; i + m <= n; i++) {
            int j = 0;
            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }
            if (j == m) {
                res.add(i);
            }
        }
        return res;
    }

    public static List<Integer> kmpSearch(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            return res;
        }
        int[] lps = buildLps(pattern);
        int i = 0;
        int j = 0;
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    res.add(i - m);
                    j = lps[j - 1];
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return res;
    }

    private static int[] buildLps(String p) {
        int[] lps = new int[p.length()];
        int len = 0;
        for (int i = 1; i < p.length(); ) {
            if (p.charAt(i) == p.charAt(len)) {
                lps[i++] = ++len;
            } else if (len > 0) {
                len = lps[len - 1];
            } else {
                lps[i++] = 0;
            }
        }
        return lps;
    }

    public static List<Integer> rabinKarp(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0 || m > n) {
            return res;
        }
        long mod = 1_000_000_007L;
        long base = 911382323L;
        long pow = 1;
        for (int i = 1; i < m; i++) {
            pow = (pow * base) % mod;
        }
        long hashP = 0;
        long hashT = 0;
        for (int i = 0; i < m; i++) {
            hashP = (hashP * base + pattern.charAt(i)) % mod;
            hashT = (hashT * base + text.charAt(i)) % mod;
        }
        for (int i = 0; i + m <= n; i++) {
            if (hashP == hashT) {
                boolean ok = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    res.add(i);
                }
            }
            if (i + m < n) {
                hashT = (hashT - text.charAt(i) * pow) % mod;
                if (hashT < 0) {
                    hashT += mod;
                }
                hashT = (hashT * base + text.charAt(i + m)) % mod;
            }
        }
        return res;
    }

    public static List<Integer> boyerMoore(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            return res;
        }
        int[] bad = new int[256];
        for (int i = 0; i < 256; i++) {
            bad[i] = -1;
        }
        for (int i = 0; i < m; i++) {
            bad[pattern.charAt(i) & 0xFF] = i;
        }
        int s = 0;
        while (s <= n - m) {
            int j = m - 1;
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }
            if (j < 0) {
                res.add(s);
                s += (s + m < n) ? m - bad[text.charAt(s + m) & 0xFF] : 1;
            } else {
                s += Math.max(1, j - bad[text.charAt(s + j) & 0xFF]);
            }
        }
        return res;
    }

    public static int[] zAlgorithm(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0;
        int r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        return z;
    }

    public static class AhoCorasick {
        public static class Node {
            Map<Character, Node> next = new HashMap<>();
            Node link;
            List<String> out = new ArrayList<>();
        }

        private final Node root = new Node();

        public void addWord(String word) {
            Node cur = root;
            for (char c : word.toCharArray()) {
                cur.next.putIfAbsent(c, new Node());
                cur = cur.next.get(c);
            }
            cur.out.add(word);
        }

        public void build() {
            List<Node> queue = new ArrayList<>();
            for (Node child : root.next.values()) {
                child.link = root;
                queue.add(child);
            }
            for (int qi = 0; qi < queue.size(); qi++) {
                Node v = queue.get(qi);
                for (Map.Entry<Character, Node> e : v.next.entrySet()) {
                    char c = e.getKey();
                    Node u = e.getValue();
                    Node link = v.link;
                    while (link != null && !link.next.containsKey(c)) {
                        link = link.link;
                    }
                    u.link = (link == null) ? root : link.next.get(c);
                    u.out.addAll(u.link.out);
                    queue.add(u);
                }
            }
        }

        public Map<Integer, List<String>> search(String text) {
            Map<Integer, List<String>> res = new HashMap<>();
            Node cur = root;
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                while (cur != root && !cur.next.containsKey(c)) {
                    cur = cur.link;
                }
                if (cur.next.containsKey(c)) {
                    cur = cur.next.get(c);
                }
                if (!cur.out.isEmpty()) {
                    res.putIfAbsent(i, new ArrayList<>());
                    res.get(i).addAll(cur.out);
                }
            }
            return res;
        }
    }

    public static List<Integer> suffixArrayMatch(String text, String pattern, int[] suffixArray) {
        List<Integer> res = new ArrayList<>();
        int l = 0;
        int r = suffixArray.length - 1;
        while (l <= r) {
            int m = (l + r) >>> 1;
            int idx = suffixArray[m];
            int cmp = compareSub(text, idx, pattern);
            if (cmp >= 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        int start = l;
        l = 0;
        r = suffixArray.length - 1;
        while (l <= r) {
            int m = (l + r) >>> 1;
            int idx = suffixArray[m];
            int cmp = compareSub(text, idx, pattern);
            if (cmp > 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        int end = r;
        for (int i = start; i <= end; i++) {
            res.add(suffixArray[i]);
        }
        return res;
    }

    private static int compareSub(String text, int idx, String pattern) {
        int n = text.length();
        for (int i = 0; i < pattern.length(); i++) {
            if (idx + i >= n) {
                return -1;
            }
            char tc = text.charAt(idx + i);
            char pc = pattern.charAt(i);
            if (tc != pc) {
                return Character.compare(tc, pc);
            }
        }
        return 0;
    }
}
