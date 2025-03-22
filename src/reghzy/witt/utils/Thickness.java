package reghzy.witt.utils;

public class Thickness {
    public static final Thickness EMPTY = new Thickness(0);

    public final int left, top, right, bottom;

    public Thickness(int left, int y1, int x2, int y2) {
        this.left = left;
        this.top = y1;
        this.right = x2;
        this.bottom = y2;
    }

    public Thickness(int x, int y) {
        this(x, y, x, y);
    }

    public Thickness(int all) {
        this(all, all, all, all);
    }

    public int getLeft() {
        return this.left;
    }

    public int getTop() {
        return this.top;
    }

    public int getRight() {
        return this.right;
    }

    public int getBottom() {
        return this.bottom;
    }

    /**
     * Returns the total horizontal padding (left+right)
     */
    public int getHorizontal() {
        return this.left + this.right;
    }

    /**
     * Returns the total vertical padding (top+bottom)
     */
    public int getVertical() {
        return this.top + this.bottom;
    }
}
