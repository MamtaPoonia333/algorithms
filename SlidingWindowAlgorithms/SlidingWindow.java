import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SlidingWindow {
    private SlidingWindow() {
    }

    public static int[] maxInWindow(int[] nums, int k) {
        if (k <= 0 || nums.length == 0) {
            return new int[0];
        }
        int n = nums.length;
        int[] res = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && dq.peekFirst() <= i - k) {
                dq.pollFirst();
            }
            while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i]) {
                dq.pollLast();
            }
            dq.addLast(i);
            if (i >= k - 1) {
                res[i - k + 1] = nums[dq.peekFirst()];
            }
        }
        return res;
    }

    public static int[] minInWindow(int[] nums, int k) {
        if (k <= 0 || nums.length == 0) {
            return new int[0];
        }
        int n = nums.length;
        int[] res = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && dq.peekFirst() <= i - k) {
                dq.pollFirst();
            }
            while (!dq.isEmpty() && nums[dq.peekLast()] >= nums[i]) {
                dq.pollLast();
            }
            dq.addLast(i);
            if (i >= k - 1) {
                res[i - k + 1] = nums[dq.peekFirst()];
            }
        }
        return res;
    }

    public static int longestSubstringWithoutRepeating(String s) {
        int[] last = new int[256];
        for (int i = 0; i < 256; i++) {
            last[i] = -1;
        }
        int start = 0;
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) & 0xFF;
            if (last[c] >= start) {
                start = last[c] + 1;
            }
            last[c] = i;
            best = Math.max(best, i - start + 1);
        }
        return best;
    }

    public static int longestSubstringWithKDistinct(String s, int k) {
        if (k <= 0) {
            return 0;
        }
        Map<Character, Integer> freq = new HashMap<>();
        int left = 0;
        int best = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            freq.put(c, freq.getOrDefault(c, 0) + 1);
            while (freq.size() > k) {
                char lc = s.charAt(left++);
                int count = freq.get(lc) - 1;
                if (count == 0) {
                    freq.remove(lc);
                } else {
                    freq.put(lc, count);
                }
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static String minWindowSubstring(String s, String t) {
        if (t.isEmpty() || s.isEmpty()) {
            return "";
        }
        int[] need = new int[256];
        int required = 0;
        for (char c : t.toCharArray()) {
            if (need[c] == 0) {
                required++;
            }
            need[c]++;
        }
        int[] have = new int[256];
        int formed = 0;
        int l = 0;
        int bestLen = Integer.MAX_VALUE;
        int bestL = 0;
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            have[c]++;
            if (have[c] == need[c] && need[c] > 0) {
                formed++;
            }
            while (l <= r && formed == required) {
                if (r - l + 1 < bestLen) {
                    bestLen = r - l + 1;
                    bestL = l;
                }
                char lc = s.charAt(l++);
                have[lc]--;
                if (need[lc] > 0 && have[lc] < need[lc]) {
                    formed--;
                }
            }
        }
        return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestL, bestL + bestLen);
    }

    public static int[] slidingWindowMaximum(int[] nums, int k) {
        return maxInWindow(nums, k);
    }

    public static int subarraySumEqualsK(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1);
        int sum = 0;
        int count = 0;
        for (int num : nums) {
            sum += num;
            count += freq.getOrDefault(sum - k, 0);
            freq.put(sum, freq.getOrDefault(sum, 0) + 1);
        }
        return count;
    }

    public static int countAnagrams(String s, String p) {
        if (p.length() > s.length()) {
            return 0;
        }
        int[] need = new int[26];
        int[] have = new int[26];
        for (char c : p.toCharArray()) {
            need[c - 'a']++;
        }
        int k = p.length();
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            have[s.charAt(i) - 'a']++;
            if (i >= k) {
                have[s.charAt(i - k) - 'a']--;
            }
            if (i >= k - 1 && matches(need, have)) {
                count++;
            }
        }
        return count;
    }

    private static boolean matches(int[] a, int[] b) {
        for (int i = 0; i < 26; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
}
