package reghzy.witt.gui;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiTextField;

import java.awt.*;

/**
 * A helper class for arranging controls in a stack panel
 */
public class StackPanel extends Panel {
    private final boolean isVertical;
    private final int spacing;
    private Point measuredSize;

    public StackPanel() {
        this(6, true);
    }

    public StackPanel(int spacing, boolean isVertical) {
        this.spacing = spacing;
        this.isVertical = isVertical;
    }

    @Override
    public Point measure() {
        if (this.measuredSize != null) {
            return this.measuredSize;
        }

        int totalW = 0, totalH = 0, i = 0;
        for (Object obj : this.myElements) {
            int w, h;
            if (obj instanceof StackPanel) {
                StackPanel panel = (StackPanel) obj;
                Point size = panel.measure();
                w = size.x;
                h = size.y;
            }
            else if (obj instanceof GuiWrapper) {
                GuiWrapper wrap = (GuiWrapper) obj;
                w = wrap.width + wrap.offX;
                h = wrap.height + wrap.offY;
            }
            else {
                continue; // ?????
            }

            if (this.isVertical) {
                totalW = Math.max(totalW, w);
                totalH += (i++ == 0 ? 0 : this.spacing) + h;
            }
            else {
                totalW += (i++ == 0 ? 0 : this.spacing) + w;
                totalH = Math.max(totalH, h);
            }
        }

        return this.measuredSize = new Point(totalW, totalH);
    }

    /**
     * Arranges the components of this panel recursively, creating the actual Gui
     * controls in the process and adding them to the sink list
     * @param gui The advanced GUI
     * @param offsetX The offset X
     * @param offsetY The offset Y
     * @return The size of this panel
     */
    @Override
    public Point arrange(GuiScreenAdvanced gui, int offsetX, int offsetY) {
        int totalW = 0, totalH = 0, i = 0;
        for (Object obj : this.myElements) {
            int w, h;
            if (obj instanceof StackPanel) {
                StackPanel panel = (StackPanel) obj;
                Point size = panel.arrange(gui, offsetX, offsetY);
                w = size.x;
                h = size.y;
            }
            else if (obj instanceof GuiWrapper) {
                GuiWrapper wrap = (GuiWrapper) obj;
                gui.addGui(wrap.createGui(offsetX, offsetY));
                w = wrap.width + wrap.offX;
                h = wrap.height + wrap.offY;
            }
            else if (obj instanceof EmptySpace) {
                EmptySpace s = (EmptySpace) obj;
                w = s.x;
                h = s.y;
            }
            else {
                continue; // ?????
            }

            if (this.isVertical) {
                totalW = Math.max(totalW, w);
                totalH += (i++ == 0 ? 0 : this.spacing) + h;
                offsetY += h + this.spacing;
            }
            else {
                totalW = (i++ == 0 ? 0 : this.spacing) + w;
                totalH += Math.max(totalH, h);
                offsetX += w + this.spacing;
            }
        }

        return new Point(totalW, totalH);
    }

    /**
     * Gets how many pixels of blank space there are between controls
     * @return
     */
    public int getSpacing() {
        return this.spacing;
    }

    public void addButton(int id, int offset, int width, int height) {
        this.myElements.add(new StackPanel.GuiButtonWrapper(id, this.isVertical ? offset : 0, this.isVertical ? 0 : offset, width, height));
    }

    public void addTextField(FontRenderer renderer, int offset, int width, int height) {
        this.myElements.add(new StackPanel.GuiTextFieldWrapper(renderer, this.isVertical ? offset : 0, this.isVertical ? 0 : offset, width, height));
    }

    public void addTextBlock(FontRenderer renderer, int offset, int width) {
        this.myElements.add(new StackPanel.GuiTextBlockWrapper(renderer, this.isVertical ? offset : 0, this.isVertical ? 0 : offset, width));
    }

    public void addPanel(StackPanel panel) {
        this.myElements.add(panel);
    }

    public void addEmptySpace(int space) {
        this.myElements.add(this.isVertical ? new EmptySpace(0, space) : new EmptySpace(space, 0));
    }

    protected static class EmptySpace {
        public final int x, y;

        public EmptySpace(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    protected static abstract class GuiWrapper {
        public final int offX, offY; // additional offsets. Pre-calculated based on stack panel orientation
        public final int width, height;

        public GuiWrapper(int offX, int offY, int width, int height) {
            this.offX = offX;
            this.offY = offY;
            this.width = width;
            this.height = height;
        }

        public abstract Gui createGui(int x, int y);
    }

    protected static class GuiButtonWrapper extends GuiWrapper {
        public final int id;

        public GuiButtonWrapper(int id, int offX, int offY, int width, int height) {
            super(offX, offY, width, height);
            this.id = id;
        }

        @Override
        public Gui createGui(int x, int y) {
            return new GuiButton(this.id, this.offX + x, this.offY + y, this.width, this.height, null);
        }
    }

    protected static class GuiTextFieldWrapper extends GuiWrapper {
        private final FontRenderer fontRenderer;

        public GuiTextFieldWrapper(FontRenderer fontRenderer, int offX, int offY, int width, int height) {
            super(offX, offY, width, height);
            this.fontRenderer = fontRenderer;
        }

        @Override
        public Gui createGui(int x, int y) {
            return new GuiTextField(this.fontRenderer, x, y, this.width, this.height);
        }
    }

    protected static class GuiTextBlockWrapper extends GuiWrapper {
        private final FontRenderer fontRenderer;

        public GuiTextBlockWrapper(FontRenderer fontRenderer, int offX, int offY, int width) {
            super(offX, offY, width, 7);
            this.fontRenderer = fontRenderer;
        }

        @Override
        public Gui createGui(int x, int y) {
            return new GuiTextBlock(this.fontRenderer, x, y, this.width, this.height);
        }
    }
}
