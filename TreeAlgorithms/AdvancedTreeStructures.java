import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AdvancedTreeStructures {
    private AdvancedTreeStructures() {
    }

    public static class AVLTree {
        public static class Node {
            int key;
            int height;
            Node left;
            Node right;

            Node(int key) {
                this.key = key;
                this.height = 1;
            }
        }

        public Node insert(Node node, int key) {
            if (node == null) {
                return new Node(key);
            }
            if (key < node.key) {
                node.left = insert(node.left, key);
            } else if (key > node.key) {
                node.right = insert(node.right, key);
            } else {
                return node;
            }
            node.height = 1 + Math.max(height(node.left), height(node.right));
            int balance = balance(node);
            if (balance > 1 && key < node.left.key) {
                return rotateRight(node);
            }
            if (balance < -1 && key > node.right.key) {
                return rotateLeft(node);
            }
            if (balance > 1 && key > node.left.key) {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
            if (balance < -1 && key < node.right.key) {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
            return node;
        }

        public Node delete(Node root, int key) {
            if (root == null) {
                return null;
            }
            if (key < root.key) {
                root.left = delete(root.left, key);
            } else if (key > root.key) {
                root.right = delete(root.right, key);
            } else {
                if (root.left == null || root.right == null) {
                    root = (root.left != null) ? root.left : root.right;
                } else {
                    Node succ = minValueNode(root.right);
                    root.key = succ.key;
                    root.right = delete(root.right, succ.key);
                }
            }
            if (root == null) {
                return null;
            }
            root.height = 1 + Math.max(height(root.left), height(root.right));
            int balance = balance(root);
            if (balance > 1 && balance(root.left) >= 0) {
                return rotateRight(root);
            }
            if (balance > 1 && balance(root.left) < 0) {
                root.left = rotateLeft(root.left);
                return rotateRight(root);
            }
            if (balance < -1 && balance(root.right) <= 0) {
                return rotateLeft(root);
            }
            if (balance < -1 && balance(root.right) > 0) {
                root.right = rotateRight(root.right);
                return rotateLeft(root);
            }
            return root;
        }

        private int height(Node node) {
            return node == null ? 0 : node.height;
        }

        private int balance(Node node) {
            return node == null ? 0 : height(node.left) - height(node.right);
        }

        private Node rotateRight(Node y) {
            Node x = y.left;
            Node t2 = x.right;
            x.right = y;
            y.left = t2;
            y.height = 1 + Math.max(height(y.left), height(y.right));
            x.height = 1 + Math.max(height(x.left), height(x.right));
            return x;
        }

        private Node rotateLeft(Node x) {
            Node y = x.right;
            Node t2 = y.left;
            y.left = x;
            x.right = t2;
            x.height = 1 + Math.max(height(x.left), height(x.right));
            y.height = 1 + Math.max(height(y.left), height(y.right));
            return y;
        }

        private Node minValueNode(Node node) {
            Node cur = node;
            while (cur.left != null) {
                cur = cur.left;
            }
            return cur;
        }
    }

    public static class RedBlackTree {
        private static final boolean RED = true;
        private static final boolean BLACK = false;

        public static class Node {
            int key;
            boolean color;
            Node left;
            Node right;
            Node parent;

            Node(int key, boolean color) {
                this.key = key;
                this.color = color;
            }
        }

        public Node root;

        public void insert(int key) {
            Node node = new Node(key, RED);
            Node y = null;
            Node x = root;
            while (x != null) {
                y = x;
                x = key < x.key ? x.left : x.right;
            }
            node.parent = y;
            if (y == null) {
                root = node;
            } else if (key < y.key) {
                y.left = node;
            } else {
                y.right = node;
            }
            insertFix(node);
        }

        private void insertFix(Node z) {
            while (z.parent != null && z.parent.color == RED) {
                if (z.parent == z.parent.parent.left) {
                    Node y = z.parent.parent.right;
                    if (y != null && y.color == RED) {
                        z.parent.color = BLACK;
                        y.color = BLACK;
                        z.parent.parent.color = RED;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.right) {
                            z = z.parent;
                            rotateLeft(z);
                        }
                        z.parent.color = BLACK;
                        z.parent.parent.color = RED;
                        rotateRight(z.parent.parent);
                    }
                } else {
                    Node y = z.parent.parent.left;
                    if (y != null && y.color == RED) {
                        z.parent.color = BLACK;
                        y.color = BLACK;
                        z.parent.parent.color = RED;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.left) {
                            z = z.parent;
                            rotateRight(z);
                        }
                        z.parent.color = BLACK;
                        z.parent.parent.color = RED;
                        rotateLeft(z.parent.parent);
                    }
                }
            }
            root.color = BLACK;
        }

        private void rotateLeft(Node x) {
            Node y = x.right;
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
            y.left = x;
            x.parent = y;
        }

        private void rotateRight(Node x) {
            Node y = x.left;
            x.left = y.right;
            if (y.right != null) {
                y.right.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.right) {
                x.parent.right = y;
            } else {
                x.parent.left = y;
            }
            y.right = x;
            x.parent = y;
        }
    }

    public static class BTree {
        private final int t;

        public static class Node {
            int n;
            boolean leaf;
            int[] keys;
            Node[] children;

            Node(int t, boolean leaf) {
                this.leaf = leaf;
                this.keys = new int[2 * t - 1];
                this.children = new Node[2 * t];
                this.n = 0;
            }
        }

        public Node root;

        public BTree(int t) {
            this.t = t;
            root = new Node(t, true);
        }

        public Node search(Node x, int key) {
            int i = 0;
            while (i < x.n && key > x.keys[i]) {
                i++;
            }
            if (i < x.n && key == x.keys[i]) {
                return x;
            }
            if (x.leaf) {
                return null;
            }
            return search(x.children[i], key);
        }

        public void insert(int key) {
            Node r = root;
            if (r.n == 2 * t - 1) {
                Node s = new Node(t, false);
                root = s;
                s.children[0] = r;
                splitChild(s, 0, r);
                insertNonFull(s, key);
            } else {
                insertNonFull(r, key);
            }
        }

        private void splitChild(Node x, int i, Node y) {
            Node z = new Node(t, y.leaf);
            z.n = t - 1;
            for (int j = 0; j < t - 1; j++) {
                z.keys[j] = y.keys[j + t];
            }
            if (!y.leaf) {
                for (int j = 0; j < t; j++) {
                    z.children[j] = y.children[j + t];
                }
            }
            y.n = t - 1;
            for (int j = x.n; j >= i + 1; j--) {
                x.children[j + 1] = x.children[j];
            }
            x.children[i + 1] = z;
            for (int j = x.n - 1; j >= i; j--) {
                x.keys[j + 1] = x.keys[j];
            }
            x.keys[i] = y.keys[t - 1];
            x.n++;
        }

        private void insertNonFull(Node x, int key) {
            int i = x.n - 1;
            if (x.leaf) {
                while (i >= 0 && key < x.keys[i]) {
                    x.keys[i + 1] = x.keys[i];
                    i--;
                }
                x.keys[i + 1] = key;
                x.n++;
            } else {
                while (i >= 0 && key < x.keys[i]) {
                    i--;
                }
                i++;
                if (x.children[i].n == 2 * t - 1) {
                    splitChild(x, i, x.children[i]);
                    if (key > x.keys[i]) {
                        i++;
                    }
                }
                insertNonFull(x.children[i], key);
            }
        }
    }

    public static class BPlusTree {
        private final int t;

        public abstract static class Node {
            int n;
            int[] keys;
            Node parent;

            Node(int t) {
                keys = new int[2 * t - 1];
            }

            abstract boolean isLeaf();
        }

        public static class InternalNode extends Node {
            Node[] children;

            InternalNode(int t) {
                super(t);
                children = new Node[2 * t];
            }

            @Override
            boolean isLeaf() {
                return false;
            }
        }

        public static class LeafNode extends Node {
            int[] values;
            LeafNode next;

            LeafNode(int t) {
                super(t);
                values = new int[2 * t - 1];
            }

            @Override
            boolean isLeaf() {
                return true;
            }
        }

        public Node root;

        public BPlusTree(int t) {
            this.t = t;
            root = new LeafNode(t);
        }

        public Integer search(int key) {
            LeafNode leaf = findLeaf(root, key);
            for (int i = 0; i < leaf.n; i++) {
                if (leaf.keys[i] == key) {
                    return leaf.values[i];
                }
            }
            return null;
        }

        public void insert(int key, int value) {
            LeafNode leaf = findLeaf(root, key);
            insertIntoLeaf(leaf, key, value);
            if (leaf.n == 2 * t - 1) {
                splitLeaf(leaf);
            }
        }

        private LeafNode findLeaf(Node node, int key) {
            if (node.isLeaf()) {
                return (LeafNode) node;
            }
            InternalNode in = (InternalNode) node;
            int i = 0;
            while (i < in.n && key >= in.keys[i]) {
                i++;
            }
            return findLeaf(in.children[i], key);
        }

        private void insertIntoLeaf(LeafNode leaf, int key, int value) {
            int i = leaf.n - 1;
            while (i >= 0 && key < leaf.keys[i]) {
                leaf.keys[i + 1] = leaf.keys[i];
                leaf.values[i + 1] = leaf.values[i];
                i--;
            }
            leaf.keys[i + 1] = key;
            leaf.values[i + 1] = value;
            leaf.n++;
        }

        private void splitLeaf(LeafNode leaf) {
            LeafNode newLeaf = new LeafNode(t);
            int mid = t;
            newLeaf.n = leaf.n - mid;
            for (int i = 0; i < newLeaf.n; i++) {
                newLeaf.keys[i] = leaf.keys[mid + i];
                newLeaf.values[i] = leaf.values[mid + i];
            }
            leaf.n = mid;
            newLeaf.next = leaf.next;
            leaf.next = newLeaf;
            if (leaf.parent == null) {
                InternalNode newRoot = new InternalNode(t);
                newRoot.keys[0] = newLeaf.keys[0];
                newRoot.children[0] = leaf;
                newRoot.children[1] = newLeaf;
                newRoot.n = 1;
                leaf.parent = newRoot;
                newLeaf.parent = newRoot;
                root = newRoot;
            } else {
                insertIntoParent(leaf, newLeaf.keys[0], newLeaf);
            }
        }

        private void insertIntoParent(Node left, int key, Node right) {
            InternalNode parent = (InternalNode) left.parent;
            int i = parent.n - 1;
            while (i >= 0 && key < parent.keys[i]) {
                parent.keys[i + 1] = parent.keys[i];
                parent.children[i + 2] = parent.children[i + 1];
                i--;
            }
            parent.keys[i + 1] = key;
            parent.children[i + 2] = right;
            right.parent = parent;
            parent.n++;
            if (parent.n == 2 * t - 1) {
                splitInternal(parent);
            }
        }

        private void splitInternal(InternalNode node) {
            int mid = t - 1;
            InternalNode newNode = new InternalNode(t);
            newNode.n = node.n - mid - 1;
            for (int i = 0; i < newNode.n; i++) {
                newNode.keys[i] = node.keys[mid + 1 + i];
            }
            for (int i = 0; i <= newNode.n; i++) {
                newNode.children[i] = node.children[mid + 1 + i];
                if (newNode.children[i] != null) {
                    newNode.children[i].parent = newNode;
                }
            }
            int upKey = node.keys[mid];
            node.n = mid;
            if (node.parent == null) {
                InternalNode newRoot = new InternalNode(t);
                newRoot.keys[0] = upKey;
                newRoot.children[0] = node;
                newRoot.children[1] = newNode;
                newRoot.n = 1;
                node.parent = newRoot;
                newNode.parent = newRoot;
                root = newRoot;
            } else {
                insertIntoParent(node, upKey, newNode);
            }
        }
    }

    public static class SplayTree {
        public static class Node {
            int key;
            Node left;
            Node right;
            Node parent;

            Node(int key) {
                this.key = key;
            }
        }

        public Node root;

        public Node search(int key) {
            Node cur = root;
            while (cur != null) {
                if (key == cur.key) {
                    splay(cur);
                    return cur;
                }
                cur = key < cur.key ? cur.left : cur.right;
            }
            return null;
        }

        public void insert(int key) {
            Node z = root;
            Node p = null;
            while (z != null) {
                p = z;
                if (key < z.key) {
                    z = z.left;
                } else if (key > z.key) {
                    z = z.right;
                } else {
                    splay(z);
                    return;
                }
            }
            Node node = new Node(key);
            node.parent = p;
            if (p == null) {
                root = node;
            } else if (key < p.key) {
                p.left = node;
            } else {
                p.right = node;
            }
            splay(node);
        }

        private void splay(Node x) {
            while (x.parent != null) {
                if (x.parent.parent == null) {
                    if (x.parent.left == x) {
                        rotateRight(x.parent);
                    } else {
                        rotateLeft(x.parent);
                    }
                } else if (x.parent.left == x && x.parent.parent.left == x.parent) {
                    rotateRight(x.parent.parent);
                    rotateRight(x.parent);
                } else if (x.parent.right == x && x.parent.parent.right == x.parent) {
                    rotateLeft(x.parent.parent);
                    rotateLeft(x.parent);
                } else if (x.parent.left == x && x.parent.parent.right == x.parent) {
                    rotateRight(x.parent);
                    rotateLeft(x.parent);
                } else {
                    rotateLeft(x.parent);
                    rotateRight(x.parent);
                }
            }
        }

        private void rotateLeft(Node x) {
            Node y = x.right;
            x.right = y.left;
            if (y.left != null) {
                y.left.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
            y.left = x;
            x.parent = y;
        }

        private void rotateRight(Node x) {
            Node y = x.left;
            x.left = y.right;
            if (y.right != null) {
                y.right.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.right) {
                x.parent.right = y;
            } else {
                x.parent.left = y;
            }
            y.right = x;
            x.parent = y;
        }
    }

    public static class Treap {
        public static class Node {
            int key;
            int priority;
            Node left;
            Node right;

            Node(int key, int priority) {
                this.key = key;
                this.priority = priority;
            }
        }

        private final Random rng = new Random();

        public Node insert(Node root, int key) {
            if (root == null) {
                return new Node(key, rng.nextInt());
            }
            if (key < root.key) {
                root.left = insert(root.left, key);
                if (root.left.priority > root.priority) {
                    root = rotateRight(root);
                }
            } else if (key > root.key) {
                root.right = insert(root.right, key);
                if (root.right.priority > root.priority) {
                    root = rotateLeft(root);
                }
            }
            return root;
        }

        public Node delete(Node root, int key) {
            if (root == null) {
                return null;
            }
            if (key < root.key) {
                root.left = delete(root.left, key);
            } else if (key > root.key) {
                root.right = delete(root.right, key);
            } else {
                if (root.left == null) {
                    return root.right;
                } else if (root.right == null) {
                    return root.left;
                } else {
                    if (root.left.priority > root.right.priority) {
                        root = rotateRight(root);
                        root.right = delete(root.right, key);
                    } else {
                        root = rotateLeft(root);
                        root.left = delete(root.left, key);
                    }
                }
            }
            return root;
        }

        private Node rotateLeft(Node x) {
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            return y;
        }

        private Node rotateRight(Node x) {
            Node y = x.left;
            x.left = y.right;
            y.right = x;
            return y;
        }
    }

    public static class SegmentTree {
        private final int n;
        private final long[] tree;

        public SegmentTree(long[] arr) {
            this.n = arr.length;
            this.tree = new long[4 * n];
            build(1, 0, n - 1, arr);
        }

        private void build(int node, int l, int r, long[] arr) {
            if (l == r) {
                tree[node] = arr[l];
                return;
            }
            int mid = (l + r) >>> 1;
            build(node * 2, l, mid, arr);
            build(node * 2 + 1, mid + 1, r, arr);
            tree[node] = tree[node * 2] + tree[node * 2 + 1];
        }

        public void update(int idx, long val) {
            update(1, 0, n - 1, idx, val);
        }

        private void update(int node, int l, int r, int idx, long val) {
            if (l == r) {
                tree[node] = val;
                return;
            }
            int mid = (l + r) >>> 1;
            if (idx <= mid) {
                update(node * 2, l, mid, idx, val);
            } else {
                update(node * 2 + 1, mid + 1, r, idx, val);
            }
            tree[node] = tree[node * 2] + tree[node * 2 + 1];
        }

        public long query(int ql, int qr) {
            return query(1, 0, n - 1, ql, qr);
        }

        private long query(int node, int l, int r, int ql, int qr) {
            if (ql > r || qr < l) {
                return 0;
            }
            if (ql <= l && r <= qr) {
                return tree[node];
            }
            int mid = (l + r) >>> 1;
            return query(node * 2, l, mid, ql, qr) + query(node * 2 + 1, mid + 1, r, ql, qr);
        }
    }

    public static class FenwickTree {
        private final long[] bit;

        public FenwickTree(int n) {
            bit = new long[n + 1];
        }

        public void add(int idx, long delta) {
            for (int i = idx + 1; i < bit.length; i += i & -i) {
                bit[i] += delta;
            }
        }

        public long sum(int idx) {
            long res = 0;
            for (int i = idx + 1; i > 0; i -= i & -i) {
                res += bit[i];
            }
            return res;
        }

        public long rangeSum(int l, int r) {
            if (l > r) {
                return 0;
            }
            return sum(r) - (l == 0 ? 0 : sum(l - 1));
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

        public boolean startsWith(String prefix) {
            Node cur = root;
            for (char c : prefix.toCharArray()) {
                int idx = c - 'a';
                if (cur.next[idx] == null) {
                    return false;
                }
                cur = cur.next[idx];
            }
            return true;
        }
    }

    public static class SuffixTree {
        public static class Node {
            Map<Character, Node> next = new HashMap<>();
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

    public static class TernarySearchTree {
        public static class Node {
            char c;
            boolean end;
            Node left;
            Node mid;
            Node right;

            Node(char c) {
                this.c = c;
            }
        }

        public Node root;

        public void insert(String word) {
            root = insert(root, word, 0);
        }

        private Node insert(Node node, String word, int idx) {
            char c = word.charAt(idx);
            if (node == null) {
                node = new Node(c);
            }
            if (c < node.c) {
                node.left = insert(node.left, word, idx);
            } else if (c > node.c) {
                node.right = insert(node.right, word, idx);
            } else {
                if (idx + 1 == word.length()) {
                    node.end = true;
                } else {
                    node.mid = insert(node.mid, word, idx + 1);
                }
            }
            return node;
        }

        public boolean search(String word) {
            Node node = search(root, word, 0);
            return node != null && node.end;
        }

        private Node search(Node node, String word, int idx) {
            if (node == null) {
                return null;
            }
            char c = word.charAt(idx);
            if (c < node.c) {
                return search(node.left, word, idx);
            }
            if (c > node.c) {
                return search(node.right, word, idx);
            }
            if (idx + 1 == word.length()) {
                return node;
            }
            return search(node.mid, word, idx + 1);
        }
    }
}
