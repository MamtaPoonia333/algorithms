import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ClassicProblems {
    private ClassicProblems() {
    }

    public static class Interval {
        public final int start;
        public final int end;
        public final int index;

        public Interval(int start, int end, int index) {
            this.start = start;
            this.end = end;
            this.index = index;
        }
    }

    public static List<Integer> activitySelection(int[] start, int[] end) {
        List<Interval> intervals = new ArrayList<>();
        for (int i = 0; i < start.length; i++) {
            intervals.add(new Interval(start[i], end[i], i));
        }
        intervals.sort(Comparator.comparingInt(a -> a.end));
        List<Integer> selected = new ArrayList<>();
        int lastEnd = Integer.MIN_VALUE;
        for (Interval it : intervals) {
            if (it.start >= lastEnd) {
                selected.add(it.index);
                lastEnd = it.end;
            }
        }
        return selected;
    }

    public static double fractionalKnapsack(int[] weights, int[] values, int capacity) {
        class Item {
            int w;
            int v;

            Item(int w, int v) {
                this.w = w;
                this.v = v;
            }
        }
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < weights.length; i++) {
            items.add(new Item(weights[i], values[i]));
        }
        items.sort((a, b) -> Double.compare((double) b.v / b.w, (double) a.v / a.w));
        double total = 0;
        int cap = capacity;
        for (Item it : items) {
            if (cap == 0) {
                break;
            }
            if (it.w <= cap) {
                total += it.v;
                cap -= it.w;
            } else {
                total += (double) it.v * cap / it.w;
                cap = 0;
            }
        }
        return total;
    }

    public static class HuffmanNode {
        char ch;
        int freq;
        HuffmanNode left;
        HuffmanNode right;

        HuffmanNode(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }
    }

    public static Map<Character, String> huffmanCoding(Map<Character, Integer> freq) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.freq));
        for (Map.Entry<Character, Integer> e : freq.entrySet()) {
            pq.add(new HuffmanNode(e.getKey(), e.getValue()));
        }
        if (pq.isEmpty()) {
            return new HashMap<>();
        }
        while (pq.size() > 1) {
            HuffmanNode a = pq.poll();
            HuffmanNode b = pq.poll();
            HuffmanNode parent = new HuffmanNode('\0', a.freq + b.freq);
            parent.left = a;
            parent.right = b;
            pq.add(parent);
        }
        HuffmanNode root = pq.poll();
        Map<Character, String> codes = new HashMap<>();
        buildCodes(root, "", codes);
        return codes;
    }

    private static void buildCodes(HuffmanNode node, String prefix, Map<Character, String> codes) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            codes.put(node.ch, prefix.isEmpty() ? "0" : prefix);
            return;
        }
        buildCodes(node.left, prefix + "0", codes);
        buildCodes(node.right, prefix + "1", codes);
    }

    public static class Job {
        public final String id;
        public final int deadline;
        public final int profit;

        public Job(String id, int deadline, int profit) {
            this.id = id;
            this.deadline = deadline;
            this.profit = profit;
        }
    }

    public static class JobScheduleResult {
        public final int totalProfit;
        public final List<Job> scheduledJobs;

        public JobScheduleResult(int totalProfit, List<Job> scheduledJobs) {
            this.totalProfit = totalProfit;
            this.scheduledJobs = scheduledJobs;
        }
    }

    public static JobScheduleResult jobSequencing(List<Job> jobs) {
        jobs.sort((a, b) -> Integer.compare(b.profit, a.profit));
        int maxDeadline = 0;
        for (Job job : jobs) {
            maxDeadline = Math.max(maxDeadline, job.deadline);
        }
        Job[] slots = new Job[maxDeadline + 1];
        int totalProfit = 0;
        for (Job job : jobs) {
            for (int t = job.deadline; t > 0; t--) {
                if (slots[t] == null) {
                    slots[t] = job;
                    totalProfit += job.profit;
                    break;
                }
            }
        }
        List<Job> scheduled = new ArrayList<>();
        for (int t = 1; t <= maxDeadline; t++) {
            if (slots[t] != null) {
                scheduled.add(slots[t]);
            }
        }
        return new JobScheduleResult(totalProfit, scheduled);
    }

    public static long kruskalMstTotalWeight(int n, List<MinimumSpanningTree.Edge> edges) {
        return MinimumSpanningTree.kruskal(n, edges).totalWeight;
    }

    public static long[] dijkstra(int n, List<List<ShortestPath.Edge>> adj, int source) {
        return ShortestPath.dijkstra(n, adj, source);
    }

    public static List<Integer> intervalScheduling(int[] start, int[] end) {
        return activitySelection(start, end);
    }

    public static List<String> egyptianFraction(int numerator, int denominator) {
        List<String> result = new ArrayList<>();
        int n = numerator;
        int d = denominator;
        while (n != 0) {
            int x = (d + n - 1) / n;
            result.add("1/" + x);
            n = n * x - d;
            d = d * x;
            int g = (int) GcdAndLcm.gcd(n, d);
            if (g != 0) {
                n /= g;
                d /= g;
            }
        }
        return result;
    }
}
