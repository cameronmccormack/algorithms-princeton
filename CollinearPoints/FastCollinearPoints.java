import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segs = new ArrayList<>();

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
            // sort array of points so that the line segment through any 4 points is always
            // between the first and last point in the array
            Arrays.sort(copy);
 
            // Sort the points according to the slopes they make with p.
            // If any 3 (or more) adjacent points in the sorted order have
            // equal slopes with respect to p then these points, together with
            // p, are collinear.
            Arrays.sort(copy, copy[i].slopeOrder());
            
            int last = 2;
            // find last collinear to p point
            while (last < copy.length
                    && Double.compare(copy[0].slopeTo(copy[1]), copy[0].slopeTo(copy[last])) == 0) {
                last++;
            }
            // if found at least 3 elements, make segment if it's unique
            if (last >= 4 && copy[0].compareTo(copy[1]) < 0) {
                segs.add(new LineSegment(copy[0], copy[last - 1]));
            }
        }
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
