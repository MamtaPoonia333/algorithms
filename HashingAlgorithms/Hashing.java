package HashingAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Hashing {
    private Hashing() {
    }

    public static class ChainingHashTable<K, V> {
        private static class Entry<K, V> {
            K key;
            V value;

            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        private final List<List<Entry<K, V>>> table;
        private int size;

        public ChainingHashTable(int capacity) {
            table = new ArrayList<>();
            for (int i = 0; i < capacity; i++) {
                table.add(new ArrayList<>());
            }
        }

        private int index(K key) {
            return (key.hashCode() & 0x7fffffff) % table.size();
        }

        public void put(K key, V value) {
            int idx = index(key);
            for (Entry<K, V> e : table.get(idx)) {
                if (e.key.equals(key)) {
                    e.value = value;
                    return;
                }
            }
            table.get(idx).add(new Entry<>(key, value));
            size++;
        }

        public V get(K key) {
            int idx = index(key);
            for (Entry<K, V> e : table.get(idx)) {
                if (e.key.equals(key)) {
                    return e.value;
                }
            }
            return null;
        }

        public int size() {
            return size;
        }
    }

    public static class OpenAddressingHashTable {
        private static class Entry {
            int key;
            int value;
            boolean used;
            boolean deleted;
        }

        private final Entry[] table;

        public OpenAddressingHashTable(int capacity) {
            table = new Entry[capacity];
            for (int i = 0; i < capacity; i++) {
                table[i] = new Entry();
            }
        }

        public void putLinearProbing(int key, int value) {
            int n = table.length;
            int idx = (key & 0x7fffffff) % n;
            for (int i = 0; i < n; i++) {
                int pos = (idx + i) % n;
                Entry e = table[pos];
                if (!e.used || e.deleted || e.key == key) {
                    e.key = key;
                    e.value = value;
                    e.used = true;
                    e.deleted = false;
                    return;
                }
            }
        }

        public Integer getLinearProbing(int key) {
            int n = table.length;
            int idx = (key & 0x7fffffff) % n;
            for (int i = 0; i < n; i++) {
                int pos = (idx + i) % n;
                Entry e = table[pos];
                if (!e.used && !e.deleted) {
                    return null;
                }
                if (e.used && !e.deleted && e.key == key) {
                    return e.value;
                }
            }
            return null;
        }

        public void putQuadraticProbing(int key, int value) {
            int n = table.length;
            int idx = (key & 0x7fffffff) % n;
            for (int i = 0; i < n; i++) {
                int pos = (idx + i * i) % n;
                Entry e = table[pos];
                if (!e.used || e.deleted || e.key == key) {
                    e.key = key;
                    e.value = value;
                    e.used = true;
                    e.deleted = false;
                    return;
                }
            }
        }

        public void putDoubleHashing(int key, int value) {
            int n = table.length;
            int h1 = (key & 0x7fffffff) % n;
            int h2 = 1 + ((key & 0x7fffffff) % (n - 1));
            for (int i = 0; i < n; i++) {
                int pos = (h1 + i * h2) % n;
                Entry e = table[pos];
                if (!e.used || e.deleted || e.key == key) {
                    e.key = key;
                    e.value = value;
                    e.used = true;
                    e.deleted = false;
                    return;
                }
            }
        }
    }

    public static long rabinKarpHash(String s) {
        long mod = 1_000_000_007L;
        long base = 911382323L;
        long hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = (hash * base + s.charAt(i)) % mod;
        }
        return hash;
    }

    public static long polynomialHash(String s, long base, long mod) {
        long hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = (hash * base + s.charAt(i)) % mod;
        }
        return hash;
    }

    public static class UniversalHash {
        private final long a;
        private final long b;
        private final long p;
        private final int m;

        public UniversalHash(int m) {
            this.m = m;
            this.p = 1_000_000_007L;
            Random rng = new Random();
            this.a = 1 + Math.abs(rng.nextLong()) % (p - 1);
            this.b = Math.abs(rng.nextLong()) % p;
        }

        public int hash(int x) {
            long h = (a * x + b) % p;
            return (int) (h % m);
        }
    }

    public static class PerfectHashing {
        private final int[][] table;
        private final int[] sizes;
        private final UniversalHash[] hashes;

        public PerfectHashing(int[] keys) {
            int n = keys.length;
            sizes = new int[n];
            hashes = new UniversalHash[n];
            List<List<Integer>> buckets = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                buckets.add(new ArrayList<>());
            }
            UniversalHash h = new UniversalHash(n);
            for (int key : keys) {
                buckets.get(h.hash(key)).add(key);
            }
            table = new int[n][];
            for (int i = 0; i < n; i++) {
                int size = buckets.get(i).size();
                if (size == 0) {
                    table[i] = new int[0];
                    sizes[i] = 0;
                    hashes[i] = new UniversalHash(1);
                    continue;
                }
                int m = size * size;
                sizes[i] = m;
                boolean placed = false;
                while (!placed) {
                    UniversalHash hi = new UniversalHash(m);
                    int[] slot = new int[m];
                    Arrays.fill(slot, Integer.MIN_VALUE);
                    placed = true;
                    for (int key : buckets.get(i)) {
                        int pos = hi.hash(key);
                        if (slot[pos] != Integer.MIN_VALUE) {
                            placed = false;
                            break;
                        }
                        slot[pos] = key;
                    }
                    if (placed) {
                        table[i] = slot;
                        hashes[i] = hi;
                    }
                }
            }
        }

        public boolean contains(int key) {
            int i = key % table.length;
            if (i < 0) {
                i += table.length;
            }
            if (sizes[i] == 0) {
                return false;
            }
            int pos = hashes[i].hash(key);
            return table[i][pos] == key;
        }
    }

    public static class CuckooHashing {
        private final Integer[] table1;
        private final Integer[] table2;
        private final int capacity;

        public CuckooHashing(int capacity) {
            this.capacity = capacity;
            this.table1 = new Integer[capacity];
            this.table2 = new Integer[capacity];
        }

        private int h1(int key) {
            return (key & 0x7fffffff) % capacity;
        }

        private int h2(int key) {
            return ((key * 31) & 0x7fffffff) % capacity;
        }

        public void insert(int key) {
            int cur = key;
            boolean inFirst = true;
            for (int i = 0; i < capacity; i++) {
                if (inFirst) {
                    int pos = h1(cur);
                    if (table1[pos] == null) {
                        table1[pos] = cur;
                        return;
                    }
                    int temp = table1[pos];
                    table1[pos] = cur;
                    cur = temp;
                    inFirst = false;
                } else {
                    int pos = h2(cur);
                    if (table2[pos] == null) {
                        table2[pos] = cur;
                        return;
                    }
                    int temp = table2[pos];
                    table2[pos] = cur;
                    cur = temp;
                    inFirst = true;
                }
            }
            throw new IllegalStateException("Rehash needed");
        }

        public boolean contains(int key) {
            return table1[h1(key)] != null && table1[h1(key)] == key
                || table2[h2(key)] != null && table2[h2(key)] == key;
        }
    }

    public static class RobinHoodHashing {
        private static class Entry {
            int key;
            int dist;
            boolean used;
        }

        private final Entry[] table;

        public RobinHoodHashing(int capacity) {
            table = new Entry[capacity];
            for (int i = 0; i < capacity; i++) {
                table[i] = new Entry();
            }
        }

        public void insert(int key) {
            int n = table.length;
            int idx = (key & 0x7fffffff) % n;
            int dist = 0;
            while (true) {
                Entry e = table[idx];
                if (!e.used) {
                    e.used = true;
                    e.key = key;
                    e.dist = dist;
                    return;
                }
                if (e.dist < dist) {
                    int tmpKey = e.key;
                    int tmpDist = e.dist;
                    e.key = key;
                    e.dist = dist;
                    key = tmpKey;
                    dist = tmpDist;
                }
                idx = (idx + 1) % n;
                dist++;
            }
        }

        public boolean contains(int key) {
            int n = table.length;
            int idx = (key & 0x7fffffff) % n;
            int dist = 0;
            while (true) {
                Entry e = table[idx];
                if (!e.used || e.dist < dist) {
                    return false;
                }
                if (e.key == key) {
                    return true;
                }
                idx = (idx + 1) % n;
                dist++;
            }
        }
    }
}
