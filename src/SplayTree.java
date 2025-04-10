// A Splay Tree implementation. For every operation (insert, delete, search) the destination node is splayed to the root of the tree
// This ensures frequently accessed nodes are easier to access

public class SplayTree<T extends Comparable<T>> {
    private class Node{
        T key;
        Node left, right , parent;

        Node(T key){
            this.key = key;
        }
    }

    private Node root;
    private int size;


    public SplayTree(){
        root = null;
        size = 0;
    }

    public int size(){
        return size;
    }

    private Node findMax(Node node){
        Node cur = node;
        while (cur.right != null){
            cur = cur.right;
        }
        return cur;
    }

    // Rotate to make node a child of its parent
    private void rotate(Node c){
        Node p = c.parent;
        if (p == null){
            return;
        }

        Node g = p.parent;

        if (g != null){
            if (g.left == p){
                g.left = c;
            }
            else{
                g.right = c;
            }
        }

        if (p.left == c){
            p.left = c.right;
            if (c.right != null){
                c.right.parent = p;
            }
            c.right = p;
        }
        else{
            p.right = c.left;
            if (c.left != null){
                c.left.parent = p;
            }
            c.left = p;
        }

        c.parent = g;
        p.parent = c;

    }

    // Bring the node to the root
    private void splay(Node node){
        while (node.parent != null){
            Node p = node.parent;
            Node g = p.parent;

            // Zig
            if (g == null){
                rotate(node);
            }
            // Zig-Zig
            else if ((g.left == p && p.left == node) || (g.right == p && p.right == node)){
                rotate(p);
                rotate(node);
            }
            // Zig-Zag
            else{
                rotate(node);
                rotate(node);
            }
        }
        root = node;
    }

    public void insert(T key){
        Node nNode = new Node(key);
        if (root == null){
            root = nNode;
            size++;
            return;
        }

        Node cur = root;
        Node p = null;

        // Basic BST insertion + splaying
        while (cur != null){
            p = cur;
            int compare = key.compareTo(cur.key);

            if (compare < 0){
                cur = cur.left;
            }
            else if (compare > 0){
                cur = cur.right;
            }
            else{
                splay(cur);
                return;
            }
        }

        nNode.parent = p;
        if (p != null) { // Bypass the possible null pointer dereference of p
            int compare = key.compareTo(p.key);
            if (compare < 0){
                p.left = nNode;
            }
            else{
                p.right = nNode;
            }
        }

        splay(nNode);
        size++;
    }

    // Uses BST searching 
    private Node findNode(T key){
        Node cur = root;
        while (cur != null){
            int compare = key.compareTo(cur.key);
            if (compare < 0){
                cur = cur.left;
            }
            else if (compare > 0){
                cur = cur.right;
            }
            else{
                return cur;
            }
        }
        return null; // Node not found
    }

    public boolean search(T key){
        Node node = findNode(key);
        if (node != null){
            splay(node);
            return true;
        }
        return false;
    }

    public void delete(T key){
        Node node = findNode(key);
        if (node == null){
            return;
        }

        splay(node);

        // If 1 child null
        if (node.left == null){
            root = node.right;
            if (root != null){
                root.parent = null;
            }
        }
        else if(node.right == null){
            root = node.left;
            if (root != null){
                root.parent = null;
            }
        }
        else{ // Both children are not null
            Node rightTree = node.right;
            Node leftTree = node.left;
            leftTree.parent = null;

            root = leftTree;
            Node maxLeft = findMax(leftTree); // Inorder predecessor 
            splay(maxLeft);

            maxLeft.right = rightTree;
            if (rightTree != null){
                rightTree.parent = maxLeft;
            }
        }

        size--;
    }
}
