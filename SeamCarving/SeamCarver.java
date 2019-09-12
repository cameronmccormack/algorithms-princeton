import java.lang.Math;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pic;
    private double[][] energy;
    private static final double EDGE_ENERGY = 1000;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        pic = picture;
        energy = new double[pic.width()][pic.height()];
        for (int x = 0; x < pic.width(); x++) {    
            for (int y = 0; y < pic.height(); y++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return pic;
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
        if ( x < 0 || x > pic.width() - 1 || y < 0 || y > pic.width() - 1) {
            throw new IllegalArgumentException();
        }
        if ( x == 0 || x == pic.width() - 1 || y == 0 || y == pic.width() - 1) {
            return EDGE_ENERGY;
        }
        
        xGradRed = pic.get(x-1, y).getRed() - pic.get(x+1, y).getRed();
        xGradBlue = pic.get(x-1, y).getBlue() - pic.get(x+1, y).getBlue();
        xGradGreen = pic.get(x-1, y).getGreen() - pic.get(x+1, y).getGreen();
        xGradSquared = xGradRed * xGradRed + xGradBlue * xGradBlue + xGradGreen * xGradGreen;

        yGradRed = pic.get(x, y-1).getRed() - pic.get(x, y+1).getRed();
        yGradBlue = pic.get(x, y-1).getBlue() - pic.get(x, y+1).getBlue();
        yGradGreen = pic.get(x, y-1).getGreen() - pic.get(x, y+1).getGreen();
        yGradSquared = yGradRed * yGradRed + yGradBlue * yGradBlue + yGradGreen * yGradGreen;

        return Math.sqrt(xGradSquared + yGradSquared);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        for (int point : seam) {
            if (point < 0 || point > pic.height() - 1) {
                throw new IllegalArgumentException();
            }
        }
        if (seam.length != pic.width()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length() - 1; i++) {
            if (Math.abs(seam[i] - seam[i+1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        Picture newPic = new Picture(pic.width(), pic.height()-1);
        int displacement = 0;
        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.width(); y++) {
                if (y == seam[x]) {
                    displacement = -1;
                } else {
                    newPic.set(x, y + displacement, pic.get(x, y));
                }
            }
            displacement = 0;
        }
        pic = newPic; 
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        for (int point : seam) {
            if (point < 0 || point > pic.width() - 1) {
                throw new IllegalArgumentException();
            }
        }
        if (seam.length != pic.height()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length() - 1; i++) {
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
    }

    // quick test
    public static void main(String[] args) {

    }

}
