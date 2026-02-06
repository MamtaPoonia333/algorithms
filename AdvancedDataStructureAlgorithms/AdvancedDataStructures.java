import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AdvancedDataStructures {
    private AdvancedDataStructures() {
    }

    public static class DisjointSetUnion {
        private final int[] parent;
        private final int[] size;

        public DisjointSetUnion(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) {
                return false;
            }
            if (size[ra] < size[rb]) {
                int t = ra;
                ra = rb;
                rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
            return true;
        }

        public int size(int x) {
            return size[find(x)];
        }
    }

    public static class MoQuery {
        public final int l;
        public final int r;
        public final int idx;

        public MoQuery(int l, int r, int idx) {
            this.l = l;
            this.r = r;
            this.idx = idx;
        }
    }

    public static int[] mosAlgorithmDistinct(int[] arr, List<MoQuery> queries) {
        int n = arr.length;
        int block = (int) Math.sqrt(n) + 1;
        queries.sort((a, b) -> {
            int blockA = a.l / block;
            int blockB = b.l / block;
            if (blockA != blockB) {
                return Integer.compare(blockA, blockB);
            }
            return Integer.compare(a.r, b.r);
        });
        Map<Integer, Integer> freq = new HashMap<>();
        int curL = 0;
        int curR = -1;
        int distinct = 0;
        int[] ans = new int[queries.size()];
        for (MoQuery q : queries) {
            while (curL > q.l) {
                curL--;
                int val = arr[curL];
                int f = freq.getOrDefault(val, 0) + 1;
                if (f == 1) {
                    distinct++;
                }
                freq.put(val, f);
            }
            while (curR < q.r) {
                curR++;
                int val = arr[curR];
                int f = freq.getOrDefault(val, 0) + 1;
                if (f == 1) {
                    distinct++;
                }
                freq.put(val, f);
            }
            while (curL < q.l) {
                int val = arr[curL];
                int f = freq.get(val) - 1;
                if (f == 0) {
                    distinct--;
                    freq.remove(val);
                } else {
                    freq.put(val, f);
                }
                curL++;
            }
            while (curR > q.r) {
                int val = arr[curR];
                int f = freq.get(val) - 1;
                if (f == 0) {
                    distinct--;
                    freq.remove(val);
                } else {
                    freq.put(val, f);
                }
                curR--;
            }
            ans[q.idx] = distinct;
        }
        return ans;
    }

    public static class SqrtDecomposition {
        private final int n;
        private final int blockSize;
        private final int[] arr;
        private final int[] blockSum;

        public SqrtDecomposition(int[] arr) {
            this.arr = arr.clone();
            this.n = arr.length;
            this.blockSize = (int) Math.sqrt(n) + 1;
            this.blockSum = new int[blockSize + 1];
            for (int i = 0; i < n; i++) {
                blockSum[i / blockSize] += this.arr[i];
            }
        }

        public void update(int idx, int val) {
            int block = idx / blockSize;
            blockSum[block] += val - arr[idx];
            arr[idx] = val;
        }

        public int query(int l, int r) {
            int sum = 0;
            while (l <= r && l % blockSize != 0) {
                sum += arr[l++];
            }
            while (l + blockSize - 1 <= r) {
                sum += blockSum[l / blockSize];
                l += blockSize;
            }
            while (l <= r) {
                sum += arr[l++];
            }
            return sum;
        }
    }

    public static class SparseTable {
        private final int[][] st;
        private final int[] log;

        public SparseTable(int[] arr) {
            int n = arr.length;
            log = new int[n + 1];
            for (int i = 2; i <= n; i++) {
                log[i] = log[i / 2] + 1;
            }
            int K = log[n] + 1;
            st = new int[K][n];
            System.arraycopy(arr, 0, st[0], 0, n);
            for (int k = 1; k < K; k++) {
                for (int i = 0; i + (1 << k) <= n; i++) {
                    st[k][i] = Math.min(st[k - 1][i], st[k - 1][i + (1 << (k - 1))]);
                }
            }
        }

        public int queryMin(int l, int r) {
            int k = log[r - l + 1];
            return Math.min(st[k][l], st[k][r - (1 << k) + 1]);
        }
    }

    public static int[] nextGreaterElements(int[] arr) {
        int n = arr.length;
        int[] res = new int[n];
        Arrays.fill(res, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] < arr[i]) {
                res[stack.pop()] = arr[i];
            }
            stack.push(i);
        }
        return res;
    }

    public static int[] slidingWindowMax(int[] nums, int k) {
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

    public static class LruCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        public LruCache(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    public static class LfuCache<K, V> {
        private final int capacity;
        private int minFreq;
        private final Map<K, V> values;
        private final Map<K, Integer> freq;
        private final Map<Integer, Deque<K>> lists;

        public LfuCache(int capacity) {
            this.capacity = capacity;
            values = new HashMap<>();
            freq = new HashMap<>();
            lists = new HashMap<>();
            minFreq = 0;
        }

        public V get(K key) {
            if (!values.containsKey(key)) {
                return null;
            }
            int f = freq.get(key);
            freq.put(key, f + 1);
            lists.get(f).remove(key);
            if (lists.get(f).isEmpty() && f == minFreq) {
                minFreq++;
            }
            lists.computeIfAbsent(f + 1, k -> new ArrayDeque<>()).add(key);
            return values.get(key);
        }

        public void put(K key, V value) {
            if (capacity == 0) {
                return;
            }
            if (values.containsKey(key)) {
                values.put(key, value);
                get(key);
                return;
            }
            if (values.size() >= capacity) {
                Deque<K> list = lists.get(minFreq);
                K evict = list.poll();
                values.remove(evict);
                freq.remove(evict);
            }
            values.put(key, value);
            freq.put(key, 1);
            lists.computeIfAbsent(1, k -> new ArrayDeque<>()).add(key);
            minFreq = 1;
        }
    }
}
