package reghzy.witt.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import org.lwjgl.opengl.GL11;

public class GuiTextBlock extends Gui {
    public final FontRenderer renderer;
    public int posX, posY, width, height;
    public String text;

    public GuiTextBlock(FontRenderer fontRenderer, int x, int y, int width, int height) {
        this.renderer = fontRenderer;
        this.posX = x;
        this.posY = y;
        this.width = width;
        this.height = height;
    }

    public void drawText() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawString(this.renderer, this.text, this.posX, this.posY, 0xFFFFFF);
    }
}
