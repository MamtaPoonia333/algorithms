import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BacktrackingProblems {
    private BacktrackingProblems() {
    }

    // N-Queens problem solve karta hai: har row me ek queen, attack na kare
    public static List<List<String>> nQueens(int n) {
        List<List<String>> res = new ArrayList<>(); // final solutions list
        int[] cols = new int[n]; // cols[row] = kis column me queen rakhi
        Arrays.fill(cols, -1);
        boolean[] usedCol = new boolean[n]; // column already used ya nahi
        boolean[] diag1 = new boolean[2 * n]; // main diagonal (row - col + n)
        boolean[] diag2 = new boolean[2 * n]; // anti-diagonal (row + col)
        nQueensDfs(0, n, cols, usedCol, diag1, diag2, res);
        return res;
    }

    // DFS/backtracking: row-by-row queens place karta hai
    private static void nQueensDfs(int row, int n, int[] cols, boolean[] usedCol, boolean[] diag1, boolean[] diag2,
                                   List<List<String>> res) {
        if (row == n) {
            List<String> board = new ArrayList<>(); // board ka string representation
            for (int r = 0; r < n; r++) {
                char[] line = new char[n]; // ek row ki line
                Arrays.fill(line, '.');
                line[cols[r]] = 'Q';
                board.add(new String(line));
            }
            res.add(board);
            return;
        }
        for (int c = 0; c < n; c++) {
            int d1 = row - c + n; // main diagonal index
            int d2 = row + c; // anti-diagonal index
            if (usedCol[c] || diag1[d1] || diag2[d2]) {
                continue;
            }
            usedCol[c] = diag1[d1] = diag2[d2] = true;
            cols[row] = c;
            nQueensDfs(row + 1, n, cols, usedCol, diag1, diag2, res);
            usedCol[c] = diag1[d1] = diag2[d2] = false;
            cols[row] = -1;
        }
    }

    // Sudoku solve karta hai (9x9) backtracking se
    public static boolean solveSudoku(char[][] board) {
        return sudokuDfs(board, 0, 0);
    }

    // Sudoku DFS: cell-by-cell fill karta hai
    private static boolean sudokuDfs(char[][] board, int row, int col) {
        if (row == 9) {
            return true;
        }
        int nextRow = col == 8 ? row + 1 : row; // next cell row
        int nextCol = col == 8 ? 0 : col + 1; // next cell col
        if (board[row][col] != '.') {
            return sudokuDfs(board, nextRow, nextCol);
        }
        for (char c = '1'; c <= '9'; c++) {
            if (isValidSudoku(board, row, col, c)) {
                board[row][col] = c;
                if (sudokuDfs(board, nextRow, nextCol)) {
                    return true;
                }
                board[row][col] = '.';
            }
        }
        return false;
    }

    // Check karta hai ki val r,c par valid hai ya nahi
    private static boolean isValidSudoku(char[][] board, int r, int c, char val) {
        for (int i = 0; i < 9; i++) {
            if (board[r][i] == val || board[i][c] == val) {
                return false;
            }
        }
        int br = (r / 3) * 3; // 3x3 box start row
        int bc = (c / 3) * 3; // 3x3 box start col
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[br + i][bc + j] == val) {
                    return false;
                }
            }
        }
        return true;
    }

    // Rat in a maze: saare possible paths return karta hai
    public static List<List<int[]>> ratInMaze(int[][] maze) {
        int n = maze.length; // grid size
        boolean[][] visited = new boolean[n][n]; // visited cells
        List<List<int[]>> paths = new ArrayList<>(); // all paths
        List<int[]> path = new ArrayList<>(); // current path
        ratDfs(0, 0, maze, visited, path, paths);
        return paths;
    }

    // DFS: maze me 4 directions me move karta hai
    private static void ratDfs(int r, int c, int[][] maze, boolean[][] visited, List<int[]> path, List<List<int[]>> paths) {
        int n = maze.length; // grid size
        if (r < 0 || c < 0 || r >= n || c >= n || maze[r][c] == 0 || visited[r][c]) {
            return;
        }
        visited[r][c] = true;
        path.add(new int[]{r, c}); // current cell add
        if (r == n - 1 && c == n - 1) {
            paths.add(new ArrayList<>(path));
        } else {
            ratDfs(r + 1, c, maze, visited, path, paths);
            ratDfs(r - 1, c, maze, visited, path, paths);
            ratDfs(r, c + 1, maze, visited, path, paths);
            ratDfs(r, c - 1, maze, visited, path, paths);
        }
        path.remove(path.size() - 1);
        visited[r][c] = false;
    }

    // Knight's Tour: knight ko board par sab squares visit karwata hai
    public static int[][] knightsTour(int n) {
        int[][] board = new int[n][n]; // move number grid
        for (int i = 0; i < n; i++) {
            Arrays.fill(board[i], -1);
        }
        int[] dr = {2, 1, -1, -2, -2, -1, 1, 2}; // row moves
        int[] dc = {1, 2, 2, 1, -1, -2, -2, -1}; // col moves
        board[0][0] = 0;
        if (knightDfs(0, 0, 1, board, dr, dc)) {
            return board;
        }
        return new int[0][0];
    }

    // DFS for knight's tour
    private static boolean knightDfs(int r, int c, int move, int[][] board, int[] dr, int[] dc) {
        int n = board.length; // board size
        if (move == n * n) {
            return true;
        }
        for (int k = 0; k < 8; k++) {
            int nr = r + dr[k]; // next row
            int nc = c + dc[k]; // next col
            if (nr >= 0 && nc >= 0 && nr < n && nc < n && board[nr][nc] == -1) {
                board[nr][nc] = move;
                if (knightDfs(nr, nc, move + 1, board, dr, dc)) {
                    return true;
                }
                board[nr][nc] = -1;
            }
        }
        return false;
    }

    // Hamiltonian Path: graph me ek path jisme har node ek baar aaye
    public static List<Integer> hamiltonianPath(boolean[][] adj) {
        int n = adj.length; // nodes count
        int[] path = new int[n]; // path order
        Arrays.fill(path, -1);
        for (int start = 0; start < n; start++) {
            boolean[] used = new boolean[n]; // visited nodes
            used[start] = true;
            path[0] = start;
            if (hamPathDfs(1, path, used, adj)) {
                List<Integer> res = new ArrayList<>(); // result path
                for (int v : path) {
                    res.add(v);
                }
                return res;
            }
        }
        return new ArrayList<>();
    }

    // Hamiltonian Cycle: path jo start par wapas aaye
    public static List<Integer> hamiltonianCycle(boolean[][] adj) {
        int n = adj.length; // nodes count
        int[] path = new int[n]; // path order
        Arrays.fill(path, -1);
        boolean[] used = new boolean[n]; // visited nodes
        int start = 0; // starting node
        path[0] = start;
        used[start] = true;
        if (hamCycleDfs(1, path, used, adj, start)) {
            List<Integer> res = new ArrayList<>(); // result cycle
            for (int v : path) {
                res.add(v);
            }
            res.add(start);
            return res;
        }
        return new ArrayList<>();
    }

    // DFS for Hamiltonian Path
    private static boolean hamPathDfs(int idx, int[] path, boolean[] used, boolean[][] adj) {
        int n = adj.length; // nodes count
        if (idx == n) {
            return true;
        }
        for (int v = 0; v < n; v++) {
            int prev = path[idx - 1]; // previous node
            if (!used[v] && adj[prev][v]) {
                used[v] = true;
                path[idx] = v;
                if (hamPathDfs(idx + 1, path, used, adj)) {
                    return true;
                }
                used[v] = false;
                path[idx] = -1;
            }
        }
        return false;
    }

    // DFS for Hamiltonian Cycle
    private static boolean hamCycleDfs(int idx, int[] path, boolean[] used, boolean[][] adj, int start) {
        int n = adj.length; // nodes count
        if (idx == n) {
            return adj[path[idx - 1]][start];
        }
        for (int v = 0; v < n; v++) {
            int prev = path[idx - 1]; // previous node
            if (!used[v] && adj[prev][v]) {
                used[v] = true;
                path[idx] = v;
                if (hamCycleDfs(idx + 1, path, used, adj, start)) {
                    return true;
                }
                used[v] = false;
                path[idx] = -1;
            }
        }
        return false;
    }

    // Graph coloring: m colors se color assignment return karta hai
    public static int[] graphColoring(boolean[][] adj, int m) {
        int n = adj.length; // nodes count
        int[] color = new int[n]; // color per node
        Arrays.fill(color, -1);
        if (colorDfs(0, adj, color, m)) {
            return color;
        }
        return new int[0];
    }

    // DFS: node-by-node color assign karta hai
    private static boolean colorDfs(int u, boolean[][] adj, int[] color, int m) {
        int n = adj.length; // nodes count
        if (u == n) {
            return true;
        }
        for (int c = 0; c < m; c++) {
            if (isSafeColor(u, adj, color, c)) {
                color[u] = c;
                if (colorDfs(u + 1, adj, color, m)) {
                    return true;
                }
                color[u] = -1;
            }
        }
        return false;
    }

    // Check karta hai ki color c u ke liye safe hai
    private static boolean isSafeColor(int u, boolean[][] adj, int[] color, int c) {
        for (int v = 0; v < adj.length; v++) {
            if (adj[u][v] && color[v] == c) {
                return false;
            }
        }
        return true;
    }

    // Subset sum: target sum possible hai ya nahi
    public static boolean subsetSum(int[] nums, int target) {
        return subsetSumDfs(nums, 0, target);
    }

    // DFS: include/exclude approach
    private static boolean subsetSumDfs(int[] nums, int idx, int target) {
        if (target == 0) {
            return true;
        }
        if (idx == nums.length || target < 0) {
            return false;
        }
        return subsetSumDfs(nums, idx + 1, target - nums[idx]) || subsetSumDfs(nums, idx + 1, target);
    }

    // Permutations: array ki saari permutations return karta hai
    public static List<List<Integer>> permutations(int[] nums) {
        List<List<Integer>> res = new ArrayList<>(); // result permutations
        permuteDfs(nums, 0, res);
        return res;
    }

    // DFS: swap based permutation
    private static void permuteDfs(int[] nums, int idx, List<List<Integer>> res) {
        if (idx == nums.length) {
            List<Integer> perm = new ArrayList<>(); // one permutation
            for (int x : nums) {
                perm.add(x);
            }
            res.add(perm);
            return;
        }
        for (int i = idx; i < nums.length; i++) {
            swap(nums, idx, i);
            permuteDfs(nums, idx + 1, res);
            swap(nums, idx, i);
        }
    }

    // Swap helper
    private static void swap(int[] nums, int i, int j) {
        int t = nums[i]; // temp value
        nums[i] = nums[j];
        nums[j] = t;
    }

    // Combinations: 1..n me se k size ke groups
    public static List<List<Integer>> combinations(int n, int k) {
        List<List<Integer>> res = new ArrayList<>(); // result combinations
        combineDfs(1, n, k, new ArrayList<>(), res);
        return res;
    }

    // DFS: start se choices pick karta hai
    private static void combineDfs(int start, int n, int k, List<Integer> cur, List<List<Integer>> res) {
        if (cur.size() == k) {
            res.add(new ArrayList<>(cur));
            return;
        }
        for (int i = start; i <= n; i++) {
            cur.add(i);
            combineDfs(i + 1, n, k, cur, res);
            cur.remove(cur.size() - 1);
        }
    }

    // Word break: dict se string break ho sakti hai ya nahi
    public static boolean wordBreak(String s, Set<String> dict) {
        return wordBreakDfs(s, 0, dict, new Boolean[s.length()]); // memo array
    }

    // DFS with memo: index se break possible hai ya nahi
    private static boolean wordBreakDfs(String s, int idx, Set<String> dict, Boolean[] memo) {
        if (idx == s.length()) {
            return true;
        }
        if (memo[idx] != null) {
            return memo[idx];
        }
        for (int end = idx + 1; end <= s.length(); end++) {
            if (dict.contains(s.substring(idx, end)) && wordBreakDfs(s, end, dict, memo)) {
                return memo[idx] = true;
            }
        }
        return memo[idx] = false;
    }

    // Palindrome partitioning: string ko palindromic parts me todta hai
    public static List<List<String>> palindromePartitioning(String s) {
        List<List<String>> res = new ArrayList<>(); // all partitions
        palPartDfs(s, 0, new ArrayList<>(), res);
        return res;
    }

    // DFS: har end tak palindrome check
    private static void palPartDfs(String s, int idx, List<String> cur, List<List<String>> res) {
        if (idx == s.length()) {
            res.add(new ArrayList<>(cur));
            return;
        }
        for (int end = idx + 1; end <= s.length(); end++) {
            if (isPalindrome(s, idx, end - 1)) {
                cur.add(s.substring(idx, end));
                palPartDfs(s, end, cur, res);
                cur.remove(cur.size() - 1);
            }
        }
    }

    // Palindrome check helper
    private static boolean isPalindrome(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l++) != s.charAt(r--)) {
                return false;
            }
        }
        return true;
    }

    // Cryptarithmetic solve karta hai: a + b = c digit mapping se
    public static Map<Character, Integer> cryptarithmetic(String a, String b, String c) {
        Set<Character> set = new HashSet<>(); // unique letters
        for (char ch : (a + b + c).toCharArray()) {
            set.add(ch);
        }
        List<Character> letters = new ArrayList<>(set); // letters list
        if (letters.size() > 10) {
            return new HashMap<>();
        }
        int[] assign = new int[letters.size()]; // letter -> digit assignment
        Arrays.fill(assign, -1);
        boolean[] used = new boolean[10]; // digit used ya nahi
        if (cryptoDfs(0, letters, assign, used, a, b, c)) {
            Map<Character, Integer> map = new HashMap<>(); // result mapping
            for (int i = 0; i < letters.size(); i++) {
                map.put(letters.get(i), assign[i]);
            }
            return map;
        }
        return new HashMap<>();
    }

    // DFS: har letter ko digit assign karta hai
    private static boolean cryptoDfs(int idx, List<Character> letters, int[] assign, boolean[] used,
                                     String a, String b, String c) {
        if (idx == letters.size()) {
            return evalCrypto(letters, assign, a, b, c);
        }
        for (int d = 0; d <= 9; d++) {
            if (!used[d]) {
                assign[idx] = d;
                used[d] = true;
                if (cryptoDfs(idx + 1, letters, assign, used, a, b, c)) {
                    return true;
                }
                used[d] = false;
                assign[idx] = -1;
            }
        }
        return false;
    }

    // Given assignment, a+b=c check karta hai
    private static boolean evalCrypto(List<Character> letters, int[] assign, String a, String b, String c) {
        Map<Character, Integer> map = new HashMap<>(); // letter->digit
        for (int i = 0; i < letters.size(); i++) {
            map.put(letters.get(i), assign[i]);
        }
        if (map.get(a.charAt(0)) == 0 || map.get(b.charAt(0)) == 0 || map.get(c.charAt(0)) == 0) {
            return false;
        }
        long va = wordToNum(a, map); // a numeric value
        long vb = wordToNum(b, map); // b numeric value
        long vc = wordToNum(c, map); // c numeric value
        return va + vb == vc;
    }

    // Word ko number me convert karta hai
    private static long wordToNum(String s, Map<Character, Integer> map) {
        long num = 0; // build number
        for (char ch : s.toCharArray()) {
            num = num * 10 + map.get(ch);
        }
        return num;
    }

    // Crossword solve karta hai: given words fit karata hai
    public static boolean crosswordSolve(char[][] board, String[] words) {
        return crosswordDfs(board, words, 0);
    }

    // DFS: words ko ek-ek karke place karta hai
    private static boolean crosswordDfs(char[][] board, String[] words, int idx) {
        if (idx == words.length) {
            return true;
        }
        String word = words[idx]; // current word
        int n = board.length; // rows
        int m = board[0].length; // cols
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (canPlaceHoriz(board, word, i, j)) {
                    boolean[] placed = placeHoriz(board, word, i, j); // placed positions
                    if (crosswordDfs(board, words, idx + 1)) {
                        return true;
                    }
                    unplaceHoriz(board, word, i, j, placed);
                }
                if (canPlaceVert(board, word, i, j)) {
                    boolean[] placed = placeVert(board, word, i, j); // placed positions
                    if (crosswordDfs(board, words, idx + 1)) {
                        return true;
                    }
                    unplaceVert(board, word, i, j, placed);
                }
            }
        }
        return false;
    }

    // Horizontal placement possible hai ya nahi
    private static boolean canPlaceHoriz(char[][] board, String word, int r, int c) {
        int m = board[0].length; // cols
        if (c + word.length() > m) {
            return false;
        }
        if (c > 0 && board[r][c - 1] != '+') {
            return false;
        }
        if (c + word.length() < m && board[r][c + word.length()] != '+') {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            if (board[r][c + i] != '-' && board[r][c + i] != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    // Horizontal place: letters fill karta hai
    private static boolean[] placeHoriz(char[][] board, String word, int r, int c) {
        boolean[] placed = new boolean[word.length()]; // newly placed flags
        for (int i = 0; i < word.length(); i++) {
            if (board[r][c + i] == '-') {
                board[r][c + i] = word.charAt(i);
                placed[i] = true;
            }
        }
        return placed;
    }

    // Horizontal unplace: jo letters place hue the unko hatao
    private static void unplaceHoriz(char[][] board, String word, int r, int c, boolean[] placed) {
        for (int i = 0; i < word.length(); i++) {
            if (placed[i]) {
                board[r][c + i] = '-';
            }
        }
    }

    // Vertical placement possible hai ya nahi
    private static boolean canPlaceVert(char[][] board, String word, int r, int c) {
        int n = board.length; // rows
        if (r + word.length() > n) {
            return false;
        }
        if (r > 0 && board[r - 1][c] != '+') {
            return false;
        }
        if (r + word.length() < n && board[r + word.length()][c] != '+') {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            if (board[r + i][c] != '-' && board[r + i][c] != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    // Vertical place: letters fill karta hai
    private static boolean[] placeVert(char[][] board, String word, int r, int c) {
        boolean[] placed = new boolean[word.length()]; // newly placed flags
        for (int i = 0; i < word.length(); i++) {
            if (board[r + i][c] == '-') {
                board[r + i][c] = word.charAt(i);
                placed[i] = true;
            }
        }
        return placed;
    }

    // Vertical unplace: jo letters place hue the unko hatao
    private static void unplaceVert(char[][] board, String word, int r, int c, boolean[] placed) {
        for (int i = 0; i < word.length(); i++) {
            if (placed[i]) {
                board[r + i][c] = '-';
            }
        }
    }

    // Tug of War: array ko do groups me almost equal sum
    public static List<Integer> tugOfWar(int[] nums) {
        int n = nums.length; // size
        boolean[] select = new boolean[n]; // current selection
        boolean[] best = new boolean[n]; // best selection
        int total = 0; // total sum
        for (int x : nums) {
            total += x;
        }
        int[] bestDiff = new int[]{Integer.MAX_VALUE}; // best diff holder
        tugDfs(0, 0, 0, total, nums, select, best, bestDiff);
        List<Integer> group = new ArrayList<>(); // best group values
        for (int i = 0; i < n; i++) {
            if (best[i]) {
                group.add(nums[i]);
            }
        }
        return group;
    }

    // DFS: elements select/unselect karke best diff dhoondta hai
    private static void tugDfs(int idx, int count, int sum, int total, int[] nums, boolean[] select, boolean[] best, int[] bestDiff) {
        int n = nums.length; // size
        if (idx == n) {
            if (count == n / 2) {
                int diff = Math.abs(total - 2 * sum); // difference of sums
                if (diff < bestDiff[0]) {
                    bestDiff[0] = diff;
                    System.arraycopy(select, 0, best, 0, n);
                }
            }
            return;
        }
        if (count > n / 2) {
            return;
        }
        select[idx] = true;
        tugDfs(idx + 1, count + 1, sum + nums[idx], total, nums, select, best, bestDiff);
        select[idx] = false;
        tugDfs(idx + 1, count, sum, total, nums, select, best, bestDiff);
    }
}
