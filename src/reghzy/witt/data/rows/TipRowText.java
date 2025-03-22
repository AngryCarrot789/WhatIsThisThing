package reghzy.witt.data.rows;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import net.minecraft.src.RenderHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import reghzy.witt.data.ToolTip;
import reghzy.witt.utils.Thickness;

import java.util.regex.Pattern;

/**
 * A text row that only draws text, supporting colour too
 */
public abstract class TipRowText extends TipRow {
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '\u00A7' + "[0-9A-FK-OR]");
    private final Minecraft mc;
    private Point size;
    private Thickness padding;

    public TipRowText() {
        this.mc = ModLoader.getMinecraftInstance();
    }

    public Thickness getPadding() {
        return this.padding;
    }

    public void setPadding(Thickness padding) {
        this.padding = padding;
        this.onSizeInvalidated();
    }

    /**
     * Gets this row's text which may have colour added, ready to be rendered
     */
    protected abstract String getColouredText();

    /**
     * Returns this row's text without any colour added. Used for size calculations
     */
    protected abstract String getTextNoColour();

    /**
     * Invalidates any size calculations. Should only be called if absolutely
     * necessary, since text size calculations may be expensive
     */
    protected void onSizeInvalidated() {
        this.size = null;
    }

    @Override
    public Point getSize() {
        if (this.size == null) {
            this.size = measureText(this.getTextNoColour(), this.padding, this.mc);
        }

        return this.size;
    }

    @Override
    public void onRender(ToolTip tip, Minecraft mc, int x, int y, int width) {
        super.onRender(tip, mc, x, y, width);

        drawText(this.getColouredText(), this.padding, mc, x, y);
    }

    public static Point measureText(String textNoColour, Thickness padding, Minecraft mc) {
        int width = textNoColour != null ? mc.fontRenderer.getStringWidth(textNoColour) : 0;
        Thickness pad = padding != null ? padding : Thickness.EMPTY;
        return new Point(width + pad.left + pad.right, (textNoColour == null ? 0 : 7) + pad.top + pad.bottom);
    }

    public static void drawText(String text, Thickness padding, Minecraft mc, int x, int y) {
        RenderHelper.enableGUIStandardItemLighting();

        GL11.glDisable(32826);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Thickness pad = padding != null ? padding : Thickness.EMPTY;
        mc.fontRenderer.drawString(text, x + pad.left, y + pad.top, 0xffffff);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(32826);

        RenderHelper.disableStandardItemLighting();
    }
}
