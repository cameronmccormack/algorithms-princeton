import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        if (!points.contains(p)) {
            points.add(p);
       } 
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        Point2D min = new Point2D(rect.xmin(), rect.ymin());
        Point2D max = new Point2D(rect.xmax(), rect.ymax());
        List<Point2D> contained = new LinkedList<>();
        for (Point2D p : points.subSet(min, true, max, true)) {
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax()) {
                contained.add(p);
            }
        }
    return contained;
    }

    // a nearest neighbour in the set to point p
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        if (isEmpty()) {
            return null;
        }
        Point2D next = points.ceiling(p);
        Point2D prev = points.floor(p);
        if (next == null && prev == null) {
            return null;
        }
        
        double distanceToNext = next == null ? Double.MAX_VALUE : p.distanceTo(next);
        double distanceToPrev = prev == null ? Double.MAX_VALUE : p.distanceTo(prev);
        double minDistance = Math.min(distanceToNext, distanceToPrev);

        Point2D min = new Point2D(p.x(), p.y() - minDistance);
        Point2D max = new Point2D(p.x(), p.y() + minDistance);
        Point2D nearest = next == null ? prev : next;

        for (Point2D testPoint : points.subSet(min, true, max, true)) {
            if (p.distanceTo(testPoint) < p.distanceTo(nearest)) {
                nearest = testPoint;
            }
        }
        return nearest;
    }

    // quick test
    public static void main(String[] args) {

    }
}
