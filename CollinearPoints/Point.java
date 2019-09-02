import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    // initialises a new point
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draws the point
    public void draw() {
        StdDraw.point(x, y);
    }

    // draws the line segment
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // returns the slope between this point and the specified point
    public double slopeTo(Point that) {
        if (this.x == that.x) {
            if (this.y == that.y) {
                return Double.NEGATIVE_INFINITY;
            } else {
                return Double.POSITIVE_INFINITY;
            }
        } else if (this.y == that.y) { 
            return 0.0;
        } else {
            return (that.y - this.y) * 1.0 / (that.x - this.x);
        }
    }

    // compares two points by y-coordinate, breaking ties by x-coordinate
    public int compareTo(Point that) {
        if (that == null) {
            throw new NullPointerException();
        }
        if (this.x == that.x && this.y == that.y) {
            return 0;
        }
        if (this.y < that.y || (this.y == that.y && this.x < that.x)) {
            return -1;
        }
        return 1;
    }

    // compares two points by the slope they make with this point
    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
            public int compare(Point a, Point b) {
                double diff = slopeTo(a) - slopeTo(b);
                if (diff > 0) {
                    return 1;
                }
                if (diff < 0) {
                    return -1;
                }
                return 0;
            }
        };
    }


    // returns a string representation of the point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // quick test
    public static void main(String[] args) {
        Point a = new Point(1, 1);
        Point b = new Point(1, 2);
        System.out.println(a.slopeTo(b));
    }
}
