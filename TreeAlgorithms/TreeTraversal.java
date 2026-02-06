import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TreeTraversal {
    private TreeTraversal() {
    }

    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }

    public static List<Integer> inorderRecursive(Node root) {
        List<Integer> res = new ArrayList<>();
        inorderRec(root, res);
        return res;
    }

    private static void inorderRec(Node node, List<Integer> res) {
        if (node == null) {
            return;
        }
        inorderRec(node.left, res);
        res.add(node.val);
        inorderRec(node.right, res);
    }

    public static List<Integer> preorderRecursive(Node root) {
        List<Integer> res = new ArrayList<>();
        preorderRec(root, res);
        return res;
    }

    private static void preorderRec(Node node, List<Integer> res) {
        if (node == null) {
            return;
        }
        res.add(node.val);
        preorderRec(node.left, res);
        preorderRec(node.right, res);
    }

    public static List<Integer> postorderRecursive(Node root) {
        List<Integer> res = new ArrayList<>();
        postorderRec(root, res);
        return res;
    }

    private static void postorderRec(Node node, List<Integer> res) {
        if (node == null) {
            return;
        }
        postorderRec(node.left, res);
        postorderRec(node.right, res);
        res.add(node.val);
    }

    public static List<Integer> inorderIterative(Node root) {
        List<Integer> res = new ArrayList<>();
        Deque<Node> stack = new ArrayDeque<>();
        Node cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            res.add(cur.val);
            cur = cur.right;
        }
        return res;
    }

    public static List<Integer> preorderIterative(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            res.add(node.val);
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        return res;
    }

    public static List<Integer> postorderIterative(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<Node> stack = new ArrayDeque<>();
        Deque<Node> out = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            out.push(node);
            if (node.left != null) {
                stack.push(node.left);
            }
            if (node.right != null) {
                stack.push(node.right);
            }
        }
        while (!out.isEmpty()) {
            res.add(out.pop().val);
        }
        return res;
    }

    public static List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<Node> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Node node = q.poll();
                level.add(node.val);
                if (node.left != null) {
                    q.add(node.left);
                }
                if (node.right != null) {
                    q.add(node.right);
                }
            }
            res.add(level);
        }
        return res;
    }

    public static List<Integer> morrisInorder(Node root) {
        List<Integer> res = new ArrayList<>();
        Node cur = root;
        while (cur != null) {
            if (cur.left == null) {
                res.add(cur.val);
                cur = cur.right;
            } else {
                Node pre = cur.left;
                while (pre.right != null && pre.right != cur) {
                    pre = pre.right;
                }
                if (pre.right == null) {
                    pre.right = cur;
                    cur = cur.left;
                } else {
                    pre.right = null;
                    res.add(cur.val);
                    cur = cur.right;
                }
            }
        }
        return res;
    }

    public static Map<Integer, List<Integer>> verticalOrder(Node root) {
        Map<Integer, List<Integer>> map = new TreeMap<>();
        if (root == null) {
            return map;
        }
        Deque<Node> q = new ArrayDeque<>();
        Deque<Integer> col = new ArrayDeque<>();
        q.add(root);
        col.add(0);
        while (!q.isEmpty()) {
            Node node = q.poll();
            int c = col.poll();
            map.computeIfAbsent(c, k -> new ArrayList<>()).add(node.val);
            if (node.left != null) {
                q.add(node.left);
                col.add(c - 1);
            }
            if (node.right != null) {
                q.add(node.right);
                col.add(c + 1);
            }
        }
        return map;
    }

    public static List<List<Integer>> diagonalTraversal(Node root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Map<Integer, List<Integer>> map = new HashMap<>();
        diagonalDfs(root, 0, map);
        int d = 0;
        while (map.containsKey(d)) {
            res.add(map.get(d));
            d++;
        }
        return res;
    }

    private static void diagonalDfs(Node node, int diag, Map<Integer, List<Integer>> map) {
        if (node == null) {
            return;
        }
        map.computeIfAbsent(diag, k -> new ArrayList<>()).add(node.val);
        diagonalDfs(node.left, diag + 1, map);
        diagonalDfs(node.right, diag, map);
    }

    public static List<Integer> boundaryTraversal(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        res.add(root.val);
        addLeftBoundary(root.left, res);
        addLeaves(root.left, res);
        addLeaves(root.right, res);
        addRightBoundary(root.right, res);
        return res;
    }

    private static void addLeftBoundary(Node node, List<Integer> res) {
        while (node != null) {
            if (!isLeaf(node)) {
                res.add(node.val);
            }
            node = node.left != null ? node.left : node.right;
        }
    }

    private static void addRightBoundary(Node node, List<Integer> res) {
        List<Integer> temp = new ArrayList<>();
        while (node != null) {
            if (!isLeaf(node)) {
                temp.add(node.val);
            }
            node = node.right != null ? node.right : node.left;
        }
        for (int i = temp.size() - 1; i >= 0; i--) {
            res.add(temp.get(i));
        }
    }

    private static void addLeaves(Node node, List<Integer> res) {
        if (node == null) {
            return;
        }
        if (isLeaf(node)) {
            res.add(node.val);
            return;
        }
        addLeaves(node.left, res);
        addLeaves(node.right, res);
    }

    private static boolean isLeaf(Node node) {
        return node.left == null && node.right == null;
    }

    public static List<List<Integer>> zigzagLevelOrder(Node root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<Node> dq = new ArrayDeque<>();
        dq.add(root);
        boolean leftToRight = true;
        while (!dq.isEmpty()) {
            int size = dq.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (leftToRight) {
                    Node node = dq.pollFirst();
                    level.add(node.val);
                    if (node.left != null) {
                        dq.addLast(node.left);
                    }
                    if (node.right != null) {
                        dq.addLast(node.right);
                    }
                } else {
                    Node node = dq.pollLast();
                    level.add(node.val);
                    if (node.right != null) {
                        dq.addFirst(node.right);
                    }
                    if (node.left != null) {
                        dq.addFirst(node.left);
                    }
                }
            }
            res.add(level);
            leftToRight = !leftToRight;
        }
        return res;
    }
}
