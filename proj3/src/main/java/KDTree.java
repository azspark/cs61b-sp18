import java.util.List;

public class KDTree implements PointSet {

    private TreeNode root;

    private class TreeNode {
        private Point p;
        public boolean vertical;
        public TreeNode left;

        public TreeNode right;

        public TreeNode(Point p, boolean vertical) {
            this.p = p;
            this.vertical = vertical;
        }

        public boolean isVertical() {
            return vertical;
        }

        public boolean lessInVertical(Point point) {
            return point.getX() < p.getX();
        }

        public boolean lessInHorizontal(Point point) {
            return point.getY() < p.getY();
        }

        public Point getPoint() {
            return p;
        }

        public double distanceTo(Point p) {
            return Point.distance(p, this.p);
        }

        public Long getIndex() {
            return p.getId();
        }
    }

    public KDTree(List<Node> nodes) {
        if (nodes.size() == 0) {
            return;
        }

        root = new TreeNode(new Point(nodes.get(0)), true);// Need some optimization

        for (Node n : nodes) {
            Point p = new Point(n);
            if (!p.equals(root.p)) {
                insert(p, root);
            }
        }
    }

    public void insert(Point p, TreeNode n) {
        if ((n.isVertical() && n.lessInVertical(p)) || (!n.isVertical() && n.lessInHorizontal(p))) {
            if (n.left != null) {
                insert(p, n.left);
            } else {
                n.left = new TreeNode(p, !n.vertical);
            }
        } else {
            if (n.right != null) {
                insert(p, n.right);
            } else {
                n.right = new TreeNode(p, !n.vertical);
            }
        }
    }

    @Override
    public Long nearest(double x, double y) {
        Point goal = new Point(x, y);
        TreeNode best = nearestRecur(root, goal, root);
        return best.getIndex();
    }

    private TreeNode nearestRecur(TreeNode n, Point goal, TreeNode best) {
        if (n == null) {
            return best;
        }

        double bestDistance = best.distanceTo(goal);
        if (n.distanceTo(goal) < bestDistance) {
            best = n;
        }
        TreeNode goodSide, badSide;
        if ((n.isVertical() && n.lessInVertical(goal)) || (!n.isVertical() && n.lessInHorizontal(goal))) {
            goodSide = n.left;
            badSide = n.right;
        } else {
            goodSide = n.right;
            badSide  = n.left;
        }

        best = nearestRecur(goodSide, goal, best);
        if (badSideUseful(n, goal, bestDistance)) {
            best = nearestRecur(badSide, goal, best);
        }
        return best;
    }

    private boolean badSideUseful(TreeNode curTreeNode, Point goal, double bestDistance) {
        if (curTreeNode.isVertical()) {
//            return bestDistance > (curTreeNode.getPoint().getX() - goal.getX()) * (curTreeNode.getPoint().getX() - goal.getX());
              return bestDistance > Point.distance(curTreeNode.getPoint().getX(), goal.getX(), goal.getY(), goal.getY());
        } else {
//            return bestDistance >  (curTreeNode.getPoint().getY() - goal.getY()) * (curTreeNode.getPoint().getY() - goal.getY());
              return bestDistance > Point.distance(goal.getX(), goal.getX(), curTreeNode.getPoint().getY(), goal.getY());
        }
    }

    public static void main(String[] args) {

    }
}
