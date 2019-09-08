import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class KdTree {
    private static final double XMIN = 0.0;
    private static final double XMAX = 1.0;
    private static final double YMIN = 0.0;
    private static final double YMAX = 1.0;

    private int size;
    private Node root;

    private class Node {
        private Point2D p;
        private RectHV rect;
        private Node left;
        private Node right;

        private Node(Point2D value, RectHV inRect) {
            p = value;
            rect = inRect;

            left = null;
            right = null;
        }
    }

    // constuct an empty set of points
    public KdTree() {
        size = 0;
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
        root = insert(root, p, XMIN, YMIN, XMAX, YMAX, 0);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return (isIn(root, p, 0) != null);
    }
    
    private Point2D isIn(Node node, Point2D p, int level) {
        while (node != null) {
            int compare = compare(p, node.p, level);
            if (compare < 0) {
                return isIn(node.left, p, level + 1);
            } else if (compare > 0) {
                return isIn(node.right, p, level + 1);
            } else {
                return node.p;
            }
        }

        return null;
    }

    private int compare(Point2D a, Point2D b, int level) {
        if (level % 2 == 0) {
            // Compare x-coordinates
            int result = Double.compare(a.x(), b.x());

            if (result == 0) {
                return Double.compare(a.y(), b.y());
            } else {
                return result;
            }
        } else {
            // Compare y-coordinates
            int result = Double.compare(a.y(), b.y());

            if (result == 0) {
                return Double.compare(a.x(), b.x());
            } else {
                return result;
            }
        }
    }

    private Node insert(Node node, Point2D p, double xmin, double ymin, double xmax, double ymax, int level) {
        if (node == null) {
            size++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }
        int compare = compare(p, node.p, level);
        if (compare < 0) {
            if (level % 2 == 0) {
                node.left = insert(node.left, p, xmin, ymin, node.p.x(), ymax, level + 1);
            } else {
                node.left = insert(node.left, p, xmin, ymin, xmax, node.p.y(), level + 1);
            }
        } else if (compare > 0) {
            if (level % 2 == 0) {
                node.right = insert(node.right, p, node.p.x(), ymin, xmax, ymax, level + 1);
            } else {
                node.right = insert(node.right, p, xmin, node.p.y(), xmax, ymax, level + 1);
            }
        }
        return node;
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.clear();
        drawLine(root, 0);
    }

    private void drawLine(Node node, int level) {
        if (node != null) {
            drawLine(node.left, level + 1);
            StdDraw.setPenRadius();
            if (level % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            node.p.draw();
            drawLine(node.right, level + 1);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        LinkedList<Point2D> list = new LinkedList<Point2D>();
        rangeAdd(root, rect, list);
        return list;
    }

    private void rangeAdd(Node x, RectHV rect, LinkedList<Point2D> list) {
        if (x != null && rect.intersects(x.rect)) {
            if (rect.contains(x.p)) {
                list.add(x.p);
            }
            rangeAdd(x.left, rect, list);
            rangeAdd(x.right, rect, list);
        }
    }

    // a nearest neighbour in the set to point p
    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        } else {
            Point2D result = nearest(root, p, root.p);
            return result;
        }
    }

    private Point2D nearest(Node node, Point2D p, Point2D min) {
        if (node != null) {
            if (min == null) {
                min = node.p;
            }
            // If the current min point is closer to query than the current point
            if (min.distanceSquaredTo(p)
                    >= node.rect.distanceSquaredTo(p)) {
                if (node.p.distanceSquaredTo(p) < min.distanceSquaredTo(p)) {
                    min = node.p;
                }
                // Check in which order should we iterate
                if (node.right != null && node.right.rect.contains(p)) {
                    min = nearest(node.right, p, min);
                    min = nearest(node.left, p, min);
                } else {
                    min = nearest(node.left, p, min);
                    min = nearest(node.right, p, min);
                }
            }
        }
        return min;
    }
}
