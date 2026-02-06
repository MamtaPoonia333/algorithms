import java.util.ArrayList;
import java.util.List;

public class BinarySearchTreeOperations {
    private BinarySearchTreeOperations() {
    }

    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }

    public static Node insert(Node root, int key) {
        if (root == null) {
            return new Node(key);
        }
        if (key < root.val) {
            root.left = insert(root.left, key);
        } else if (key > root.val) {
            root.right = insert(root.right, key);
        }
        return root;
    }

    public static Node search(Node root, int key) {
        Node cur = root;
        while (cur != null) {
            if (key == cur.val) {
                return cur;
            }
            cur = key < cur.val ? cur.left : cur.right;
        }
        return null;
    }

    public static Node delete(Node root, int key) {
        if (root == null) {
            return null;
        }
        if (key < root.val) {
            root.left = delete(root.left, key);
        } else if (key > root.val) {
            root.right = delete(root.right, key);
        } else {
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            Node succ = minNode(root.right);
            root.val = succ.val;
            root.right = delete(root.right, succ.val);
        }
        return root;
    }

    public static Node minNode(Node root) {
        Node cur = root;
        while (cur != null && cur.left != null) {
            cur = cur.left;
        }
        return cur;
    }

    public static Node maxNode(Node root) {
        Node cur = root;
        while (cur != null && cur.right != null) {
            cur = cur.right;
        }
        return cur;
    }

    public static Node inorderSuccessor(Node root, int key) {
        Node succ = null;
        Node cur = root;
        while (cur != null) {
            if (key < cur.val) {
                succ = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return succ;
    }

    public static Node inorderPredecessor(Node root, int key) {
        Node pred = null;
        Node cur = root;
        while (cur != null) {
            if (key > cur.val) {
                pred = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return pred;
    }

    public static Integer kthSmallest(Node root, int k) {
        List<Integer> inorder = new ArrayList<>();
        inorder(root, inorder);
        if (k < 1 || k > inorder.size()) {
            return null;
        }
        return inorder.get(k - 1);
    }

    public static Integer kthLargest(Node root, int k) {
        List<Integer> inorder = new ArrayList<>();
        inorder(root, inorder);
        if (k < 1 || k > inorder.size()) {
            return null;
        }
        return inorder.get(inorder.size() - k);
    }

    private static void inorder(Node node, List<Integer> res) {
        if (node == null) {
            return;
        }
        inorder(node.left, res);
        res.add(node.val);
        inorder(node.right, res);
    }

    public static Integer floor(Node root, int key) {
        Integer res = null;
        Node cur = root;
        while (cur != null) {
            if (cur.val == key) {
                return key;
            }
            if (cur.val < key) {
                res = cur.val;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return res;
    }

    public static Integer ceil(Node root, int key) {
        Integer res = null;
        Node cur = root;
        while (cur != null) {
            if (cur.val == key) {
                return key;
            }
            if (cur.val > key) {
                res = cur.val;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return res;
    }
}
