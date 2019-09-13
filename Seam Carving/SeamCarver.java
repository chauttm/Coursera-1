import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private static final long BORDER_ENERGY = 1000;
    private Picture picture;
    private double[][] e;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        validateNull(picture);
        this.picture = new Picture(picture);
        calculateEnergy();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateWidthIndex(x);
        validateHeightIndex(y);
        return e[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] f = new double[width()][height()];
        int[][] trace = new int[width()][height()];
        for (int y = 0; y < height(); ++y) f[width() - 1][y] = BORDER_ENERGY;
        for (int x = width() - 1; x >= 0; --x) {
            f[x][0] = BORDER_ENERGY * (width() - x);
            f[x][height() - 1] = f[x][0];
        }
        for (int x = width() - 2; x >= 0; --x)
            for (int y = 1; y < height() - 1; ++y) {
                f[x][y] = Double.POSITIVE_INFINITY;
                if (f[x][y] > f[x + 1][y]) {
                    f[x][y] = f[x + 1][y];
                    trace[x][y] = 0;
                }
                if (f[x][y] > f[x + 1][y - 1]) {
                    f[x][y] = f[x + 1][y - 1];
                    trace[x][y] = -1;
                }
                if (f[x][y] > f[x + 1][y + 1]) {
                    f[x][y] = f[x + 1][y + 1];
                    trace[x][y] = +1;
                }
                f[x][y] += energy(x, y);
            }

        int[] seam = new int[width()];
        double min = Double.POSITIVE_INFINITY;
        int start = 0;
        for (int y = 0; y < height(); ++y)
            if (f[0][y] < min) {
                min = f[0][y];
                start = y;
            }
        for (int x = 0; x < width(); ++x) {
            seam[x] = start;
            start += trace[x][start];
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] f = new double[width()][height()];
        int[][] trace = new int[width()][height()];
        for (int x = 0; x < width(); ++x) f[x][height() - 1] = BORDER_ENERGY;
        for (int y = height() - 1; y >= 0; --y) {
            f[0][y] = BORDER_ENERGY * (height() - y);
            f[width() - 1][y] = f[0][y];
        }
        for (int y = height() - 2; y >= 0; --y)
            for (int x = 1; x < width() - 1; ++x) {
                f[x][y] = Double.POSITIVE_INFINITY;
                if (f[x][y] > f[x][y + 1]) {
                    f[x][y] = f[x][y + 1];
                    trace[x][y] = 0;
                }
                if (f[x][y] > f[x - 1][y + 1]) {
                    f[x][y] = f[x - 1][y + 1];
                    trace[x][y] = -1;
                }
                if (f[x][y] > f[x + 1][y + 1]) {
                    f[x][y] = f[x + 1][y + 1];
                    trace[x][y] = +1;
                }
                f[x][y] += energy(x, y);
            }

        int[] seam = new int[height()];
        double min = Double.POSITIVE_INFINITY;
        int start = 0;
        for (int x = 0; x < width(); ++x)
            if (f[x][0] < min) {
                min = f[x][0];
                start = x;
            }
        for (int y = 0; y < height(); ++y) {
            seam[y] = start;
            start += trace[start][y];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateHorizontalSeam(seam);
        if (height() <= 1) throw new IllegalArgumentException();
        Picture newPicture = new Picture(width(), height() - 1);
        for (int x = 0; x < width(); ++x) {
            for (int y = 0; y < seam[x]; ++y) {
                newPicture.set(x, y, picture.get(x, y));
            }
            for (int y = seam[x]; y < height() - 1; ++y) {
                newPicture.set(x, y, picture.get(x, y + 1));
            }
        }
        picture = newPicture;
        calculateEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateVerticalSeam(seam);
        if (width() <= 1) throw new IllegalArgumentException();
        Picture newPicture = new Picture(width() - 1, height());
        for (int y = 0; y < height(); ++y) {
            for (int x = 0; x < seam[y]; ++x) {
                newPicture.set(x, y, picture.get(x, y));
            }
            for (int x = seam[y]; x < width() - 1; ++x) {
                newPicture.set(x, y, picture.get(x + 1, y));
            }
        }
        picture = newPicture;
        calculateEnergy();
    }

    //  unit testing
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver seamCarver = new SeamCarver(picture);
        int[] seam = seamCarver.findHorizontalSeam();
        System.out.print("Minimum energy horizontal seam: ");
        for (int i = 0; i < seam.length; ++i) System.out.print(seam[i] + " ");
        seam = seamCarver.findVerticalSeam();
        System.out.println();

        System.out.print("Minimum energy vertical seam: ");
        for (int i = 0; i < seam.length; ++i) System.out.print(seam[i] + " ");
        System.out.println();

        System.out.println("Removing minimum energy vertical seam...");
        seamCarver.removeVerticalSeam(seam);
        for (int y = 0; y < seamCarver.height(); ++y) {
            for (int x = 0; x < seamCarver.width(); ++x) System.out.print(String.format("%.2f ", seamCarver.energy(x, y)));
            System.out.println();
        }
    }

    private void validateNull(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }

    private void validateWidthIndex(int x) {
        if (x < 0 || x >= width()) throw new IllegalArgumentException("Out of range");
    }

    private void validateHeightIndex(int y) {
        if (y < 0 || y >= height()) throw new IllegalArgumentException("Out of range");
    }

    private void validateHorizontalSeam(int[] seam) {
        validateNull(seam);
        if (seam.length != width()) throw new IllegalArgumentException();
        for (int i = 0; i < width(); ++i) validateHeightIndex(seam[i]);
        for (int i = 1; i < width(); ++i)
            if (Math.abs(seam[i] - seam[i - 1]) > 1) throw new IllegalArgumentException();
    }

    private void validateVerticalSeam(int[] seam) {
        validateNull(seam);
        if (seam.length != height()) throw new IllegalArgumentException();
        for (int i = 0; i < height(); ++i) validateWidthIndex(seam[i]);
        for (int i = 1; i < height(); ++i)
            if (Math.abs(seam[i] - seam[i - 1]) > 1) throw new IllegalArgumentException();
    }

    private void calculateEnergy() {
        e = new double[width()][height()];
        for (int x = 0; x < width(); ++x) {
            e[x][0] = BORDER_ENERGY;
            e[x][height() - 1] = BORDER_ENERGY;
        }
        for (int y = 0; y < height(); ++y) {
            e[0][y] = BORDER_ENERGY;
            e[width() - 1][y] = BORDER_ENERGY;
        }
        for (int x = 1; x < width() - 1; ++x)
            for (int y = 1; y < height() - 1; ++y) {
                int comb = (1 << 8) - 1;
                int leftColor = picture.get(x - 1, y).getRGB();
                int rightColor = picture.get(x + 1, y).getRGB();
                int Rx = Math.abs(((leftColor & (comb << 16))- (rightColor & (comb << 16))) >> 16);
                int Gx = Math.abs(((leftColor & (comb << 8))- (rightColor & (comb << 8))) >> 8);
                int Bx = Math.abs((leftColor & comb) - (rightColor & comb));
                int deltaX = Rx * Rx + Gx * Gx + Bx * Bx;

                int upperColor = picture.get(x, y - 1).getRGB();
                int lowerColor = picture.get(x, y + 1).getRGB();
                int Ry = Math.abs(((upperColor & (comb << 16))- (lowerColor & (comb << 16))) >> 16);
                int Gy = Math.abs(((upperColor & (comb << 8))- (lowerColor & (comb << 8))) >> 8);
                int By = Math.abs((upperColor & comb) - (lowerColor & comb));
                int deltaY = Ry * Ry + Gy * Gy + By * By;
                e[x][y] = Math.sqrt(deltaX + deltaY);
            }
    }
}