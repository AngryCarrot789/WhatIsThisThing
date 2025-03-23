package reghzy.witt.gui;

import java.awt.*;
import java.util.ArrayList;

public abstract class Panel {
    protected final ArrayList<Object> myElements;
    private int alignment;

    public Panel() {
        this.myElements = new ArrayList<Object>();
    }

    public int getAlignment() {
        return this.alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public abstract Point measure();

    public abstract Point arrange(GuiScreenAdvanced gui, int offsetX, int offsetY);
}
