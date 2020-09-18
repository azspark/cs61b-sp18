import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    Map<Vertex, Vertex> edgeTo;
    Map<Vertex, Double> distTo;
    SolverOutcome outcome;
    double expTime;
    int stateExplored;
    boolean solved;
    boolean exceedTime;
    Vertex endV;
    Vertex startV;
    List<Vertex> solutionList;


    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        stateExplored = 0;
        exceedTime = false;
        double startTime = System.currentTimeMillis();
        endV = end;
        startV = start;

        ArrayHeapMinPQ<Vertex> minPQ = new ArrayHeapMinPQ<>();
        minPQ.add(start, 0.0);
        distTo.put(start, 0.0);
        while (!minPQ.isEmpty() && !minPQ.getSmallest().equals(end)) {
            if (System.currentTimeMillis() - startTime > timeout * 1000) {
                exceedTime = true;
                break;
            }
            Vertex curFrom = minPQ.removeSmallest();
            stateExplored += 1;
            for (WeightedEdge e : input.neighbors(curFrom)) {

                Vertex curTo = (Vertex) e.to();
                double curCost =  distTo.get(curFrom) + e.weight();
                double curEstimate = input.estimatedDistanceToGoal(curTo, end);
                if (!distTo.containsKey(curTo)) {
                    distTo.put(curTo, curCost);
                    edgeTo.put(curTo, curFrom);
                    minPQ.add(curTo, curCost + curEstimate);
                    continue;
                }
                if (distTo.get(curTo) > curCost) {
                    distTo.put(curTo, curCost);
                    edgeTo.put(curTo, curFrom);
                    minPQ.changePriority(curTo, curCost + curEstimate);
                }
            }
        }
        expTime = ((System.currentTimeMillis() - startTime) / 1000);
        solved = !exceedTime && minPQ.getSmallest().equals(end);
    }
    public SolverOutcome outcome() {
        if (exceedTime) {
            return outcome.TIMEOUT;
        } else if (solved) {
            return outcome.SOLVED;
        } else {
            return outcome.UNSOLVABLE;
        }
    }
    public List<Vertex> solution() {
        if (!solved) {
            return new ArrayList<>();
        }
        List<Vertex> solution = new ArrayList<>();
        Vertex iterVertex = endV;
        solution.add(endV);
        while (true) {
            Vertex fromVertex = edgeTo.get(iterVertex);
            solution.add(fromVertex);
            if (fromVertex.equals(startV)) {
                break;
            }
            iterVertex = fromVertex;
        }
        Collections.reverse(solution);
        return solution;
    }

    public double solutionWeight() {
        if (!solved) {
            return 0;
        } else {
            return distTo.get(endV);
        }

    }

    public int numStatesExplored() {
        return stateExplored;
    }
    public double explorationTime() {
        return expTime;
    }
}
