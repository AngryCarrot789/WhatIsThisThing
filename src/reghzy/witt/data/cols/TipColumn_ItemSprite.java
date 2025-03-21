package reghzy.witt.data.cols;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.RenderManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import reghzy.witt.data.ToolTip;

public class TipColumn_ItemSprite extends TipColumn {
    private static final Point SIZE = new Point(24, 24);
    private static RenderItem render;
    private final ItemStack stack;

    public TipColumn_ItemSprite(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public Point getSize() {
        return SIZE;
    }

    @Override
    public void onRender(ToolTip tip, Minecraft mc, int x, int y, int height) {
        if (stack.getItem() != null) {
            drawItem(x + 5, (y + (height / 2)) - 8, stack, mc);
        }
    }

    private static void drawItem(int x, int y, ItemStack item, Minecraft mc) {
        if (render == null)
            render = (RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class);

        float oldLevel = render.zLevel;

        RenderHelper.enableGUIStandardItemLighting();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        render.zLevel = 200.0F;
        render.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, item, x, y);
        render.zLevel = oldLevel;

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderHelper.disableStandardItemLighting();
    }
}
