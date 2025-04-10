// A Generic AVL Tree implementation using a balance factor to account for 4 types of rotations: left, left right, right, and right left
// The height is recursively calculated for each node

public class AVLTree<T extends Comparable<T>> {

    private class Node{
        T key;
        int height;
        Node left, right;

        Node(T key){
            this.key = key;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public AVLTree(){
        root = null;
        size = 0;
    }

    private int height(Node node){
        return (node == null) ? 0 : node.height;
    }

    private void updateHeight(Node node){
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalanceFactor(Node node){
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private Node leftRotate(Node x){
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);
        
        return y;
    }

    private Node rightRotate(Node y){
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node balance(Node node){
        updateHeight(node);
        int balanceFactor = getBalanceFactor(node);

        // Left is unbalanced
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0){
            return rightRotate(node);
        }
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0){
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right is unbalanced
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0){
            return leftRotate(node);
        }
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0){
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
    
        return node;
    }

    private Node minValue(Node node){
        Node cur = node;
        while (cur.left != null){
            cur = cur.left;
        }
        return cur;
    }

    public int size(){
        return size;
    }

    public void insert(T key){
        root = insert(root, key);
        size++;
    }
    
    private Node insert(Node node, T key){
        if (node == null){
            return new Node(key);
        }

        int compare = key.compareTo(node.key);

        if (compare < 0){
            node.left = insert(node.left, key);
        }
        else if (compare > 0){
            node.right = insert(node.right, key);
        }
        else{
            size--;
            return node;
        }
        return balance(node);
    }

    public void delete(T key){
        root = delete(root, key);
        size--;
    }

    private Node delete(Node node, T key){
        if (node == null){
            return null;
        }

        int compare = key.compareTo(node.key);

        if (compare < 0){
            node.left = delete(node.left, key);
        }
        else if (compare > 0){
            node.right = delete(node.right, key);
        }
        else { // Node found
            size--;
            if (node.left == null || node.right == null){ // Only 1 child
                node = (node.left == null) ? node.right : node.left;
            }
            else{
                Node temp = minValue(node.right); // Inorder successor
                node.key = temp.key;
                node.right = delete(node.right, temp.key);
            }
        }

        if (node == null){
            return null;
        }
        
        return balance(node);
    }

    public boolean search(T key){
        return search(root, key) != null;
    }

    private Node search(Node node, T key){
        if (node == null || node.key.compareTo(key) == 0){
            return node;
        }

        return (key.compareTo(node.key) < 0) ? search(node.left, key) : search(node.right, key);
    }
}
