package reghzy.witt.data.cols;

import net.minecraft.client.Minecraft;
import org.lwjgl.util.Point;
import reghzy.witt.data.ToolTip;

public abstract class TipColumn {
    /**
     * Gets the amount of space this row takes up. Do not modify the return value
     */
    public abstract Point getSize();

    /**
     * Renders this column at the given X and Y coordinates relative to the tooltip box
     * @param tip    The tooltip that owns this column
     * @param mc     The minecraft instance, for easy access
     * @param x      The horizontal offset from the tooltip left, not the screen.
     * @param y      The vertical offset from the tooltip top, not the screen.
     * @param height The available height to this column. Remains the same for all columns
     */
    public abstract void onRender(ToolTip tip, Minecraft mc, int x, int y, int height);
}
