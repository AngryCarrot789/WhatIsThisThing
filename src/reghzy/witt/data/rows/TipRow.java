package reghzy.witt.data.rows;

import net.minecraft.client.Minecraft;
import org.lwjgl.util.Point;
import reghzy.witt.data.ToolTip;

/**
 * A row in the Witt tooltip
 */
public abstract class TipRow {
    /**
     * Gets the amount of space this row takes up
     */
    public abstract Point getSize();

    /**
     * Renders this row at the given X and Y coordinates relative to the tooltip box
     * @param tip   The tooltip that owns this row
     * @param mc    The minecraft instance, for easy access
     * @param x     The horizontal offset from the tooltip left, not the screen. This is provided
     *              instead of pushing a new matrix + translating OpenGL, but you may do that if you wish
     * @param y     The vertical offset from the tooltip top, not the screen. This is provided
     * @param width The width available for this row to draw into
     */
    public void onRender(ToolTip tip, Minecraft mc, int x, int y, int width) {

    }
}
