import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<PriorityNode> nodes;
    private HashMap<T, Integer> itemToIndex;

    public ArrayHeapMinPQ() {
        nodes = new ArrayList<>();
        nodes.add(new PriorityNode(null, -1));  // Add one empty node
        itemToIndex = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        nodes.add(new PriorityNode(item, priority));
        itemToIndex.put(item, size());
        swim(size());
    }

    @Override
    public boolean contains(T item) {
        return itemToIndex.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (size() > 0) {
            return nodes.get(1).getItem();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public T removeSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        T smallest = nodes.get(1).getItem();
        itemToIndex.remove(smallest);
        if (size() > 1) {
            nodes.set(1, nodes.remove(size()));
            itemToIndex.put(nodes.get(1).getItem(), 1);
            sink(1);
        } else {
            nodes.remove(size());
        }
        return smallest;
    }

    @Override
    public int size() {
        return nodes.size() - 1;  //First node is ignored
    }

    public boolean isEmpty() {return size() == 0;}

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int idx = itemToIndex.get(item);
        PriorityNode originNode = nodes.get(idx);
        double originNodePriority= originNode.getPriority();
        originNode.setPriority(priority);
        if (priority > originNodePriority) {
            sink(idx);
        } else {
            swim(idx);
        }
    }

    private void exch(int i, int j) {
        PriorityNode nodeI = nodes.get(i);
        PriorityNode nodeJ = nodes.get(j);
        nodes.set(j, nodeI);
        nodes.set(i, nodeJ);
        itemToIndex.put(nodeI.getItem(), j);
        itemToIndex.put(nodeJ.getItem(), i);
    }

    private boolean less(int i, int j) {
        return nodes.get(i).compareTo(nodes.get(j)) < 0;
    }

    private void sink(int k) {
        while (2 * k <= size()) {
            int j = 2 * k;
            if (j < size() && less(j + 1, j))
                j++;
            if (less(k, j))
                break;
            exch(k, j);
            k = j;
        }
    }

    private void swim(int k) {
        while (k > 1 && less(k, k/2)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private class PriorityNode implements Comparable<PriorityNode> {
        private T item;
        private double priority;

        PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            } else {
                return ((PriorityNode) o).getItem().equals(getItem());
            }
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }
    }
}

