package reghzy.witt.utils;

public class Thickness {
    public static final Thickness EMPTY = new Thickness(0);

    public final int x1, y1, x2, y2;

    public Thickness(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Thickness(int x, int y) {
        this(x, y, x, y);
    }

    public Thickness(int all) {
        this(all, all, all, all);
    }

    public int getX1() {
        return this.x1;
    }

    public int getY1() {
        return this.y1;
    }

    public int getX2() {
        return this.x2;
    }

    public int getY2() {
        return this.y2;
    }

    public int getWidth() {
        return this.x2 - this.x1;
    }

    public int getHeight() {
        return this.y2 - this.y1;
    }
}
