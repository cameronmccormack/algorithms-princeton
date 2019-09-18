import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private static final double ERR = 0.00000001;
    private final ArrayList<LineSegment> segs = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        // check if whole array is null
        if (points == null) {
            throw new IllegalArgumentException();
        }
        
        // check if any points are null
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        // check for duplicates
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        // make copy of array so that sorting doesn't mutate original data
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i];
        }

        // sort array of points so that the line segment through any 4 points is always
        // between the first and last point in the array
        Arrays.sort(copy);

        // if slopes between 4 points are the same, add line segment between first and last point to list
        // nested for loops are set up to avoid repeating any segments
        double slope1, slope2, slope3;
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        slope1 = copy[i].slopeTo(copy[j]);
                        slope2 = copy[i].slopeTo(copy[k]);
                        slope3 = copy[i].slopeTo(copy[l]);
                        if (Math.abs(slope1 - slope2) < ERR && Math.abs(slope1 - slope3) < ERR) {
                            segs.add(new LineSegment(copy[i], copy[l]));
                        } else if (slope1 == Double.POSITIVE_INFINITY
                                   && slope2 == Double.POSITIVE_INFINITY
                                   && slope3 == Double.POSITIVE_INFINITY) {
                            segs.add(new LineSegment(copy[i], copy[l]));
                        }
                    }
                }
            }
        }
    }

    // return the number of line segments found
    public int numberOfSegments() {
        return segs.size();
    }

    // create array of line segments
    public LineSegment[] segments() {
        return segs.toArray(new LineSegment[segs.size()]);
    }
}
