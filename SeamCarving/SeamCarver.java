import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private double[][] energy;
    private static final double EDGE_ENERGY = 1000;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        pic = picture;
        energyMatrix();
    }

    // calculate the energies of each pixel
    private void energyMatrix() {
        energy = new double[pic.width()][pic.height()];
        for (int x = 0; x < pic.width(); x++) {    
            for (int y = 0; y < pic.height(); y++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture temp = new Picture(pic.width(), pic.height());
        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {
                temp.set(x, y, pic.get(x, y));
            }
        }
        return temp;
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        // check x and y are in range (note, in this function only counting starts at 1 not 0)
        if (x < 0 || x > pic.width() - 1 || y < 0 || y > pic.height() - 1) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || x == pic.width() - 1 || y == 0 || y == pic.height() - 1) {
            return EDGE_ENERGY;
        }

        // find colour gradients in x direction
        Color leftCol = pic.get(x-1, y);
        Color rightCol = pic.get(x+1, y);
        double xGradRed = leftCol.getRed() - rightCol.getRed();
        double xGradBlue = leftCol.getBlue() - rightCol.getBlue();
        double xGradGreen = leftCol.getGreen() - rightCol.getGreen();
        double xGradSquared = xGradRed * xGradRed + xGradBlue * xGradBlue + xGradGreen * xGradGreen;

        // find colour gradients in y direction
        Color upCol = pic.get(x, y-1);
        Color downCol = pic.get(x, y+1);
        double yGradRed = upCol.getRed() - downCol.getRed();
        double yGradBlue = upCol.getBlue() - downCol.getBlue();
        double yGradGreen = upCol.getGreen() - downCol.getGreen();
        double yGradSquared = yGradRed * yGradRed + yGradBlue * yGradBlue + yGradGreen * yGradGreen;

        return Math.sqrt(xGradSquared + yGradSquared);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[pic.width()];
        double[][] distTo = new double[pic.width()][pic.height()];
        int edgeTo[][] = new int[pic.width()][pic.height()];

        // initialise distTo all as infinity
        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        // virtual source, reference -1
        for (int y = 0; y < pic.height(); y++) {
            edgeTo[0][y] = -1;
            distTo[0][y] = energy[0][y];
        }

        // relax vertices in topological order
        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {
                
                // relax directly right
                if (x < pic.width() - 1) {
                    if (distTo[x+1][y] > distTo[x][y] + energy[x+1][y]) {
                        distTo[x+1][y] = distTo[x][y] + energy[x+1][y];
                        edgeTo[x+1][y] = y;
                    }
                }

                // relax right and up
                if (x < pic.width() - 1 && y > 0) {
                    if (distTo[x+1][y-1] > distTo[x][y] + energy[x+1][y-1]) {
                        distTo[x+1][y-1] = distTo[x][y] + energy[x+1][y-1];
                        edgeTo[x+1][y-1] = y;
                    }
                }

                // relax down and right
                if (x < pic.width() - 1 && y < pic.height() - 1) {
                    if (distTo[x+1][y+1] > distTo[x][y] + energy[x+1][y+1]) {
                        distTo[x+1][y+1] = distTo[x][y] + energy[x+1][y+1];
                        edgeTo[x+1][y+1] = y;
                    }
                }
            }
        }

        // relax cost to virtual sink
        double costToSink = Double.POSITIVE_INFINITY;
        int edgeToSink = -1;
        for (int y = 0; y < pic.height(); y++) {
            if (costToSink > distTo[pic.width()-1][y]) {
                costToSink = distTo[pic.width()-1][y];
                edgeToSink = y;
            }
        }

        // create seam
        seam[pic.width()-1] = edgeToSink;
        for (int x = pic.width()-2; x >= 0; x--) {
            seam[x] = edgeTo[x+1][seam[x+1]];
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[pic.height()];
        double[][] distTo = new double[pic.width()][pic.height()];
        int edgeTo[][] = new int[pic.width()][pic.height()];

        // initialise distTo all as infinity
        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        // virtual source, reference -1
        for (int x = 0; x < pic.width(); x++) {
            edgeTo[x][0] = -1;
            distTo[x][0] = energy[x][0];
        }

        // relax vertices in topological order
        for (int y = 0; y < pic.height(); y++) {
            for (int x = 0; x < pic.width(); x++) {
                
                // relax directly below
                if (y < pic.height() - 1) {
                    if (distTo[x][y+1] > distTo[x][y] + energy[x][y+1]) {
                        distTo[x][y+1] = distTo[x][y] + energy[x][y+1];
                        edgeTo[x][y+1] = x;
                    }
                }

                // relax down and left
                if (y < pic.height() - 1 && x > 0) {
                    if (distTo[x-1][y+1] > distTo[x][y] + energy[x-1][y+1]) {
                        distTo[x-1][y+1] = distTo[x][y] + energy[x-1][y+1];
                        edgeTo[x-1][y+1] = x;
                    }
                }

                // relax down and right
                if (y < pic.height() - 1 && x < pic.width() - 1) {
                    if (distTo[x+1][y+1] > distTo[x][y] + energy[x+1][y+1]) {
                        distTo[x+1][y+1] = distTo[x][y] + energy[x+1][y+1];
                        edgeTo[x+1][y+1] = x;
                    }
                }
            }
        }

        // relax cost to virtual sink
        double costToSink = Double.POSITIVE_INFINITY;
        int edgeToSink = -1;
        for (int x = 0; x < pic.width(); x++) {
            if (costToSink > distTo[x][pic.height()-1]) {
                costToSink = distTo[x][pic.height()-1];
                edgeToSink = x;
            }
        }

        // create seam
        seam[pic.height()-1] = edgeToSink;
        for (int y = pic.height()-2; y >= 0; y--) {
            seam[y] = edgeTo[seam[y+1]][y+1];
        }
        return seam; 
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        for (int point : seam) {
            if (point < 0 || point > pic.height() - 1) {
                throw new IllegalArgumentException();
            }
        }
        if (seam.length != pic.width()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i+1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        Picture newPic = new Picture(pic.width(), pic.height()-1);
        int displacement = 0;
        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {
                if (y == seam[x]) {
                    displacement = -1;
                } else {
                    newPic.set(x, y + displacement, pic.get(x, y));
                }
            }
            displacement = 0;
        }
        pic = newPic;
        energyMatrix();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        for (int point : seam) {
            if (point < 0 || point > pic.width() - 1) {
                throw new IllegalArgumentException();
            }
        }
        if (seam.length != pic.height()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i+1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        Picture newPic = new Picture(pic.width()-1, pic.height());
        int displacement = 0;
        for (int y = 0; y < pic.height(); y++) {
            for (int x = 0; x < pic.width(); x++) {
                if (x == seam[y]) {
                    displacement = -1;
                } else {
                    newPic.set(x + displacement, y, pic.get(x, y));
                }
            }
            displacement = 0;
        }
        pic = newPic;
        energyMatrix();
    }

    // quick test
    public static void main(String[] args) {

    }

}
