import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private double err = 0.00000001;
    private ArrayList<LineSegment> segs = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {        
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

        // create copy of points that can be sorted, then sort it
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i];
        }
        
        for (int i = 0; i < copy.length - 3; i++) {
            // Sort the points according to the slopes they makes with p.
            // If any 3 (or more) adjacent points in the sorted order have
            // equal slopes with respect to p these points, together with
            // p, are collinear.

            Arrays.sort(copy, copy[i].slopeOrder());

            for (int p = 0, first = 1, last = 2; last < copy.length; last++) {
                // find last collinear to p point
                while (last < copy.length
                        && Double.compare(copy[p].slopeTo(copy[first]), copy[p].slopeTo(copy[last])) == 0) {
                    last++;
                }
                // if found at least 3 elements, make segment if it's unique
                if (last - first >= 3 && copy[p].compareTo(copy[first]) < 0) {
                    segs.add(new LineSegment(copy[p], copy[last - 1]));
                }
                // Try to find next
                first = last;
            }
        }
        // finds all line segments containing 4 or more points
    }

    // the number of line segments
    public int numberOfSegments() {
        return segs.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segs.toArray(new LineSegment[segs.size()]);
    }
}
