package byog.Core;
import edu.princeton.cs.algs4.IndexMinPQ;
import java.util.Vector;

/**
 * Minimum Spanning Tree Modified from https://algs4.cs.princeton.edu/43mst/PrimMST.java.html
 */
public class MST {
    private double[][] distanceMatrix;
    private double[] distTo;
    private int[] edgeTo;
    private boolean[] marked;
    private IndexMinPQ<Double> pq;
    int vertexNumber;

    public MST (Vector<Room> worldRooms) {
        initDistanceMatrix(worldRooms);
        vertexNumber = worldRooms.size();
        distTo = new double[vertexNumber];
        edgeTo = new int[vertexNumber];
        marked = new boolean[vertexNumber];
        for (int v = 0; v < vertexNumber; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        pq = new IndexMinPQ<>(vertexNumber);
        distTo[0] = 0.0;
        pq.insert(0, 0.0);
        while (!pq.isEmpty()) {
            visit(pq.delMin());
        }
    }

    /**
     * Calculate the value of distance matrix with given rooms
     * @param worldRooms
     */
    private void initDistanceMatrix(Vector<Room> worldRooms) {
        distanceMatrix = new double[worldRooms.size()][worldRooms.size()];

        for (int i = 0; i < worldRooms.size(); i++) {
            for (int j = i; j < worldRooms.size(); j++) {
                if (i != j) {
                    double distance = worldRooms.get(i).distanceTo(worldRooms.get(j));
                    distanceMatrix[i][j] = distance;
                    distanceMatrix[j][i] = distance;
                } else {
                    distanceMatrix[i][j] = 0.0;
                }
            }
        }
    }

    private void visit(int v) {
        marked[v] = true;
        for (int i = 0; i < vertexNumber; i++) {
            if (i == v) continue;
            if (marked[i]) continue;
            if (distanceMatrix[v][i] < distTo[i]) {
                edgeTo[i] = v;
                distTo[i] = distanceMatrix[v][i];
                if (pq.contains(i)) {
                    pq.changeKey(i, distanceMatrix[v][i]);
                } else {
                    pq.insert(i, distanceMatrix[v][i]);
                }
            }
        }
    }

    public int[] getEdgeTo() {
        return edgeTo;
    }
}
