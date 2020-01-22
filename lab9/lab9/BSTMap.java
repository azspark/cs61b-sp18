package lab9;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{

    private class Node {
        private K key;
        private V value;
        private int size;
        private Node left;
        private Node right;
        private Node (K k, V v, int s) {
            key = k;
            value = v;
            size = s;
        }
    }

    private Node root;

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key can't be null");
        }
        return getHelper(key, root);
    }

    private V getHelper(K key, Node node) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node.value;
        } else if (cmp < 0) {
            return getHelper(key, node.left);
        } else {
            return getHelper(key, node.right);
        }
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        } else {
            return node.size;
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key can't be null");
        }
        root = putHelper(key, value, root);
    }

    private Node putHelper(K key, V value, Node node) {
        if (node == null) {
            return new Node(key, value, 1);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putHelper(key, value, node.left);
        } else if (cmp > 0) {
            node.right = putHelper(key, value, node.right);
        } else {
            node.value = value;
        }
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    @Override
    public Set<K> keySet() {
        Set keySet = new HashSet<K>();

        for (Iterator<K> it = iterator(); it.hasNext(); ) {
            K k = it.next();
            keySet.add(k);
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    private class BSTMapIterator implements Iterator<K> {

        private Stack<Node> stack;
        public BSTMapIterator() {
            stack = new Stack<Node>();
            Node curNode = root;
            while (curNode != null) {
                stack.push(curNode);
                curNode = curNode.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.empty();
        }

        @Override
        public K next() {
            Node tempNode = stack.pop();
            K returnKey = tempNode.key;
            if (tempNode.right != null) {
                tempNode = tempNode.right;
                stack.push(tempNode);
                tempNode = tempNode.left;
                while (tempNode != null) {
                    stack.push(tempNode);
                    tempNode = tempNode.left;
                }
            }
            return returnKey;
        }
    }

    public void printInOrder() {

    }
}
