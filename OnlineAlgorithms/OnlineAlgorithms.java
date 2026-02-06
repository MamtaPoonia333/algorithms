import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OnlineAlgorithms {
    private OnlineAlgorithms() {
    }

    public static double competitiveRatio(long onlineCost, long optimalCost) {
        if (optimalCost == 0) {
            return onlineCost == 0 ? 1.0 : Double.POSITIVE_INFINITY;
        }
        return (double) onlineCost / optimalCost;
    }

    public static long skiRentalDeterministic(int daysUsed, int rentCostPerDay, int buyCost) {
        if (daysUsed <= 0) {
            return 0;
        }
        int rentDays = Math.min(daysUsed, buyCost);
        long cost = (long) rentDays * rentCostPerDay;
        if (daysUsed > buyCost) {
            cost += buyCost;
        }
        return cost;
    }

    public static int[] onlineBipartiteMatching(int nLeft, int nRight, List<List<Integer>> edges) {
        int[] matchRight = new int[nRight];
        int[] matchLeft = new int[nLeft];
        for (int i = 0; i < nRight; i++) {
            matchRight[i] = -1;
        }
        for (int i = 0; i < nLeft; i++) {
            matchLeft[i] = -1;
            for (int v : edges.get(i)) {
                if (v >= 0 && v < nRight && matchRight[v] == -1) {
                    matchRight[v] = i;
                    matchLeft[i] = v;
                    break;
                }
            }
        }
        return matchLeft;
    }

    public static class KServerResult {
        public final long totalCost;
        public final int[] finalPositions;

        public KServerResult(long totalCost, int[] finalPositions) {
            this.totalCost = totalCost;
            this.finalPositions = finalPositions;
        }
    }

    public static KServerResult kServerGreedy(int[] serverPositions, int[] requests) {
        int k = serverPositions.length;
        int[] pos = new int[k];
        System.arraycopy(serverPositions, 0, pos, 0, k);
        long cost = 0;
        for (int req : requests) {
            int best = 0;
            int bestDist = Math.abs(pos[0] - req);
            for (int i = 1; i < k; i++) {
                int dist = Math.abs(pos[i] - req);
                if (dist < bestDist) {
                    bestDist = dist;
                    best = i;
                }
            }
            cost += bestDist;
            pos[best] = req;
        }
        return new KServerResult(cost, pos);
    }

    public static int pageFaultsLru(int[] pages, int capacity) {
        if (capacity <= 0) {
            return pages.length;
        }
        LinkedHashMap<Integer, Integer> cache = new LinkedHashMap<>(capacity, 0.75f, true);
        int faults = 0;
        for (int p : pages) {
            if (!cache.containsKey(p)) {
                faults++;
                if (cache.size() == capacity) {
                    Integer eldest = cache.entrySet().iterator().next().getKey();
                    cache.remove(eldest);
                }
            }
            cache.put(p, 1);
        }
        return faults;
    }

    public static int pageFaultsFifo(int[] pages, int capacity) {
        if (capacity <= 0) {
            return pages.length;
        }
        Set<Integer> set = new HashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();
        int faults = 0;
        for (int p : pages) {
            if (!set.contains(p)) {
                faults++;
                if (set.size() == capacity) {
                    int evict = queue.poll();
                    set.remove(evict);
                }
                set.add(p);
                queue.add(p);
            }
        }
        return faults;
    }

    public static int pageFaultsLfu(int[] pages, int capacity) {
        if (capacity <= 0) {
            return pages.length;
        }
        Map<Integer, Integer> freq = new HashMap<>();
        Map<Integer, Integer> time = new HashMap<>();
        Set<Integer> cache = new HashSet<>();
        int faults = 0;
        int tick = 0;
        for (int p : pages) {
            tick++;
            if (!cache.contains(p)) {
                faults++;
                if (cache.size() == capacity) {
                    int evict = -1;
                    int bestFreq = Integer.MAX_VALUE;
                    int bestTime = Integer.MAX_VALUE;
                    for (int key : cache) {
                        int f = freq.getOrDefault(key, 0);
                        int t = time.getOrDefault(key, 0);
                        if (f < bestFreq || (f == bestFreq && t < bestTime)) {
                            bestFreq = f;
                            bestTime = t;
                            evict = key;
                        }
                    }
                    cache.remove(evict);
                }
                cache.add(p);
                freq.put(p, 0);
            }
            freq.put(p, freq.getOrDefault(p, 0) + 1);
            time.put(p, tick);
        }
        return faults;
    }
}
