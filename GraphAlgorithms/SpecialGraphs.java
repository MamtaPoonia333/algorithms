import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SpecialGraphs {
    private SpecialGraphs() {
    }

    public static boolean isBipartite(int n, List<List<Integer>> adj) {
        int[] color = new int[n];
        Arrays.fill(color, -1);
        for (int i = 0; i < n; i++) {
            if (color[i] == -1) {
                if (!bfsColor(i, adj, color)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean bfsColor(int start, List<List<Integer>> adj, int[] color) {
        int[] queue = new int[adj.size()];
        int head = 0;
        int tail = 0;
        queue[tail++] = start;
        color[start] = 0;
        while (head < tail) {
            int u = queue[head++];
            for (int v : adj.get(u)) {
                if (color[v] == -1) {
                    color[v] = color[u] ^ 1;
                    queue[tail++] = v;
                } else if (color[v] == color[u]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int[] greedyColoring(int n, List<List<Integer>> adj) {
        int[] color = new int[n];
        Arrays.fill(color, -1);
        boolean[] used = new boolean[n];
        for (int u = 0; u < n; u++) {
            Arrays.fill(used, false);
            for (int v : adj.get(u)) {
                if (color[v] != -1) {
                    used[color[v]] = true;
                }
            }
            int c = 0;
            while (c < n && used[c]) {
                c++;
            }
            color[u] = c;
        }
        return color;
    }

    public static long travelingSalesman(int[][] dist) {
        int n = dist.length;
        int full = 1 << n;
        long[][] dp = new long[full][n];
        for (int i = 0; i < full; i++) {
            Arrays.fill(dp[i], Long.MAX_VALUE / 4);
        }
        dp[1][0] = 0;
        for (int mask = 1; mask < full; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0) {
                    continue;
                }
                long cur = dp[mask][u];
                if (cur >= Long.MAX_VALUE / 8) {
                    continue;
                }
                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0) {
                        int nmask = mask | (1 << v);
                        dp[nmask][v] = Math.min(dp[nmask][v], cur + dist[u][v]);
                    }
                }
            }
        }
        long best = Long.MAX_VALUE / 4;
        for (int u = 1; u < n; u++) {
            best = Math.min(best, dp[full - 1][u] + dist[u][0]);
        }
        return best;
    }

    public static class WeightedEdge {
        public final int u;
        public final int v;
        public final long w;

        public WeightedEdge(int u, int v, long w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    public static long chinesePostmanUndirected(int n, List<WeightedEdge> edges) {
        long total = 0;
        int[] degree = new int[n];
        long[][] dist = new long[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Long.MAX_VALUE / 4);
            dist[i][i] = 0;
        }
        for (WeightedEdge e : edges) {
            total += e.w;
            degree[e.u]++;
            degree[e.v]++;
            dist[e.u][e.v] = Math.min(dist[e.u][e.v], e.w);
            dist[e.v][e.u] = Math.min(dist[e.v][e.u], e.w);
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (dist[i][k] >= Long.MAX_VALUE / 8) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    if (dist[k][j] >= Long.MAX_VALUE / 8) {
                        continue;
                    }
                    long nd = dist[i][k] + dist[k][j];
                    if (nd < dist[i][j]) {
                        dist[i][j] = nd;
                    }
                }
            }
        }
        List<Integer> odd = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] % 2 == 1) {
                odd.add(i);
            }
        }
        int m = odd.size();
        if (m == 0) {
            return total;
        }
        int full = 1 << m;
        long[] dp = new long[full];
        Arrays.fill(dp, Long.MAX_VALUE / 4);
        dp[0] = 0;
        for (int mask = 0; mask < full; mask++) {
            if (dp[mask] >= Long.MAX_VALUE / 8) {
                continue;
            }
            int first = -1;
            for (int i = 0; i < m; i++) {
                if ((mask & (1 << i)) == 0) {
                    first = i;
                    break;
                }
            }
            if (first == -1) {
                continue;
            }
            for (int j = first + 1; j < m; j++) {
                if ((mask & (1 << j)) == 0) {
                    int nmask = mask | (1 << first) | (1 << j);
                    long cost = dist[odd.get(first)][odd.get(j)];
                    dp[nmask] = Math.min(dp[nmask], dp[mask] + cost);
                }
            }
        }
        return total + dp[full - 1];
    }

    public static List<int[]> aStarGrid(int[][] grid, int[] start, int[] goal) {
        int n = grid.length;
        int m = grid[0].length;
        int[][] gScore = new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(gScore[i], Integer.MAX_VALUE / 4);
        }
        int[][][] parent = new int[n][m][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                parent[i][j][0] = -1;
                parent[i][j][1] = -1;
            }
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        int sx = start[0];
        int sy = start[1];
        int gx = goal[0];
        int gy = goal[1];
        gScore[sx][sy] = 0;
        pq.add(new int[]{heuristic(sx, sy, gx, gy), sx, sy});
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int x = cur[1];
            int y = cur[2];
            if (x == gx && y == gy) {
                return reconstructPath(parent, gx, gy);
            }
            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (nx < 0 || nx >= n || ny < 0 || ny >= m || grid[nx][ny] == 1) {
                    continue;
                }
                int tentative = gScore[x][y] + 1;
                if (tentative < gScore[nx][ny]) {
                    gScore[nx][ny] = tentative;
                    parent[nx][ny][0] = x;
                    parent[nx][ny][1] = y;
                    int f = tentative + heuristic(nx, ny, gx, gy);
                    pq.add(new int[]{f, nx, ny});
                }
            }
        }
        return new ArrayList<>();
    }

    private static int heuristic(int x, int y, int gx, int gy) {
        return Math.abs(x - gx) + Math.abs(y - gy);
    }

    private static List<int[]> reconstructPath(int[][][] parent, int gx, int gy) {
        List<int[]> path = new ArrayList<>();
        int x = gx;
        int y = gy;
        while (x != -1 && y != -1) {
            path.add(new int[]{x, y});
            int px = parent[x][y][0];
            int py = parent[x][y][1];
            x = px;
            y = py;
        }
        List<int[]> result = new ArrayList<>();
        for (int i = path.size() - 1; i >= 0; i--) {
            result.add(path.get(i));
        }
        return result;
    }
}
