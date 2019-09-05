import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class KdTree {

    private Node root;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
        // nothing required to create an empty set of points
    }

    private class Node {
        private double key;
        private Point2D value;
        private Node left, right;
        public Node(double key, Point2D value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node put(Node node, double key, Point2D value, int level) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        boolean isX = level % 2 == 0;
        double nextKey = isX ? value.y() : value.x();

        int compare = Double.compare(key, node.key);
        int nextLevel = level + 1;
        if (compare < 0) {
            node.left = put(node.left, nextKey, value, nextLevel);
        }
        if (compare > 0) {
            node.right = put(node.right, nextKey, value, nextLevel);
        }
        if (compare == 0) {
            if (!value.equals(node.value)) {
                node.left = put(node.left, nextKey, value, nextLevel);
            }
        }
        return node;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = put(root, p.x(), p, 0);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return isIn(root, p.x(), p, 0);
    }

    private boolean isIn(Node node, double key, Point2D value, int level) {
        if (node == null) {
            return false;
        }
        boolean isX = level % 2 == 0;
        double nextKey = isX ? value.y() : value.x();

        int compare = Double.compare(key, node.key);
        int nextLevel = level + 1;
        boolean contained = false;
        if (compare < 0) {
            contained = isIn(node.left, nextKey, value, nextLevel);
        }
        if (compare > 0) {
            contained = isIn(node.right, nextKey, value, nextLevel);
        }
        if (compare == 0) {
            if (value.equals(node.value)) {
                return true;
            } else {
                contained = isIn(node.left, nextKey, value, nextLevel);
            }
        }
        return contained;
    }

    // draw all points to standard draw
    public void draw() {
        traverseDraw(root);
    }

    private void traverseDraw(Node node) {
        if (node == null) {
            return;
        }
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.point(node.value.x(), node.value.y());
        traverseDraw(node.left);
        traverseDraw(node.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        LinkedList<Point2D> list = new LinkedList<Point2D>();
        range(root, rect, 0, list);
        return list;
    }

    private void range(Node node, RectHV rect, int level, LinkedList<Point2D> list) {
        if (node == null) {
            return;
        }
        int nextLevel = level + 1;
        boolean isX = level % 2 == 0;
        double min = isX ? rect.xmin() : rect.ymin();
        double max = isX ? rect.xmax() : rect.ymax();
        
        // does the rectangle contain the point?
        if (rect.contains(node.value)) {
            list.add(node.value);
            range(node.left, rect, nextLevel, list);
            range(node.right, rect, nextLevel, list);
        } else {
            // intersects?
            if (node.key > min && node.key < max) {
                range(node.left, rect, nextLevel, list);
                range(node.right, rect, nextLevel, list);
            }
            // lower or left?
            if (node.key > min && node.key >= max) {
                range(node.left, rect, nextLevel, list);
            }
            // higher or right?
            if (node.key <= min && node.key < max) {
                range(node.right, rect, nextLevel, list);
            }
        }
    }

    // a nearest neightbor in the set to point p
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        return nearest(root, p, 0, new Champion(root.value, Double.POSITIVE_INFINITY)).point;
    }

    private class Champion {
        private Point2D point;
        private double dist;
        public Champion(Point2D point, double dist) {
            this.point = point;
            this.dist = dist;
        }
    }

    private Champion nearest(Node node, Point2D p, int level, Champion champion) {
        if (node == null) return champion;
        int l = level + 1;
        boolean isX = level % 2 == 0;
        double key = isX ? p.x() : p.y();
        // check how close is the current point
        double currDist = p.distanceSquaredTo(node.value);
        Champion nextChampion = champion;
        if (currDist < nextChampion.dist) {
            nextChampion = new Champion(node.value, currDist);
        }
        boolean goLeft = (key <= node.key);
        if (goLeft) {
            nextChampion = nearest(node.left, p, l, nextChampion);
            double estDist = node.key - key;
            if (nextChampion.dist > estDist) {
                nextChampion = nearest(node.right, p, l, nextChampion);
            }
        } else {
            nextChampion = nearest(node.right, p, l, nextChampion);
            double estDist = node.key - key;
            if (nextChampion.dist > estDist) {
                nextChampion = nearest(node.left, p, l, nextChampion);
            }
        }
        return nextChampion;
    }

    // quick test
    public static void main(String[] args) {

    }
}
