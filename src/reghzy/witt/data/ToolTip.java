package reghzy.witt.data;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import reghzy.witt.data.cols.TipColumn;
import reghzy.witt.data.rows.TipRow;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ToolTip {
    private final Minecraft mc;
    private ArrayList<TipColumn> columns1;
    private ArrayList<TipColumn> columns2;
    private ArrayList<TipRow> centreRows;
    private ArrayList<TipRow> headerRows;
    private ArrayList<TipRow> footerRows;

    public ToolTip() {
        this.mc = ModLoader.getMinecraftInstance();
    }

    public Minecraft getMc() {
        return this.mc;
    }

    public void addRow(TipRow row, RowPlacement placement) {
        switch (placement) {
            case CENTRE:
                if (this.centreRows == null)
                    this.centreRows = new ArrayList<TipRow>();
                this.centreRows.add(row);
                break;
            case HEADER:
                if (this.headerRows == null)
                    this.headerRows = new ArrayList<TipRow>();
                this.headerRows.add(row);
                break;
            case FOOTER:
                if (this.footerRows == null)
                    this.footerRows = new ArrayList<TipRow>();
                this.footerRows.add(row);
                break;
        }
    }

    public void addColumn(boolean left, TipColumn column) {
        if ((left ? this.columns1 : this.columns2) == null) {
            if (left)
                this.columns1 = new ArrayList<TipColumn>();
            else
                this.columns2 = new ArrayList<TipColumn>();
        }

        (left ? this.columns1 : this.columns2).add(column);
    }

    /**
     * Core render function for a tooltip
     * @param mc      Minecraft, for easy access
     * @param offX    Offset from the screen
     * @param offY    Offset from the screen
     * @param screenW Width of the screen
     * @param screenH Height of the screen
     */
    public void onRender(Minecraft mc, int offX, int offY, int screenW, int screenH) {
        int totalObjects = 0;
        int trcW = 0, trcH = 0;
        if (this.centreRows != null) {
            for (TipRow row : this.centreRows) {
                Point size = row.getSize();
                trcW = Math.max(trcW, size.getX());
                trcH += size.getY();
                totalObjects++;
            }
        }

        int trhW = 0, trhH = 0;
        if (this.headerRows != null) {
            for (TipRow row : this.headerRows) {
                Point size = row.getSize();
                trhW = Math.max(trhW, size.getX());
                trhH += size.getY();
                totalObjects++;
            }
        }

        int trfW = 0, trfH = 0;
        if (this.footerRows != null) {
            for (TipRow row : this.footerRows) {
                Point size = row.getSize();
                trfW = Math.max(trfW, size.getX());
                trfH += size.getY();
                totalObjects++;
            }
        }

        int totalCol1W = 0, totalCol1H = 0;
        if (this.columns1 != null) {
            for (TipColumn row : this.columns1) {
                Point size = row.getSize();
                totalCol1W += size.getX();
                totalCol1H = Math.max(totalCol1H, size.getY());
                totalObjects++;
            }
        }

        int totalCol2W = 0, totalCol2H = 0;
        if (this.columns2 != null) {
            for (TipColumn row : this.columns2) {
                Point size = row.getSize();
                totalCol2W += size.getX();
                totalCol2H = Math.max(totalCol2H, size.getY());
                totalObjects++;
            }
        }

        final int padAll = 0;
        final int padL = padAll, padT = padAll, padR = padAll, padB = padAll;
        final int posY = 100;
        final int totalColH = Math.max(Math.max(totalCol1H, trcH), totalCol2H);
        final int containerW = Math.max(totalCol1W + trcW + totalCol2W, Math.max(trhW, trfW)) + padL + padR;
        final int containerH = totalColH + trhH + trfH + padT + padB;

        // begin rendering

        GL11.glPushMatrix();
        if (offX != 0 || offY != 0) {
            GL11.glTranslatef(offX, offY, 0);
        }

        ScaledResolution vp = new ScaledResolution(mc.gameSettings, screenW, screenH);

        int realW = vp.getScaledWidth(), realH = vp.getScaledHeight();
        // int x = ((int) (mc.displayWidth / scale) - w - 1) * pos.x / 10000;
        int scrPosX = (realW / 2) - (containerW / 2);
        int scrPosY = (realH - containerH - 1) * posY / 10000 + 5;

        // draw background
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        {
            int w = containerW, h = containerH;
            int px = scrPosX, py = scrPosY;
            drawGradientRect(px, py - 2, px + w, py, Color.GRAY.getRGB(), Color.GRAY.getRGB());
            drawGradientRect(px, py + h, px + w, py + h + 2, Color.GRAY.getRGB(), Color.GRAY.getRGB());
            drawGradientRect(px - 2, py - 2, px, py + h + 2, Color.GRAY.getRGB(), Color.GRAY.getRGB());
            drawGradientRect(px + w, py - 2, px + w + 2, py + h + 2, Color.GRAY.getRGB(), Color.GRAY.getRGB());
            drawGradientRect(px, py, px + w, py + h, -1072689136, -804253680);
        }

        GL11.glTranslatef(scrPosX, scrPosY, 0.0F);

        // render columns1, rows, column2
        int offsetX = padL; // incremented by prev column size, includes total rows size too at some point
        int offsetY = padT; // incremented by rows height
        if (this.headerRows != null) {
            for (TipRow row : this.headerRows) {
                row.onRender(this, mc, padL, offsetY, trhW);
                offsetY += row.getSize().getY();
            }
        }

        if (this.columns1 != null) {
            for (TipColumn row : this.columns1) {
                row.onRender(this, mc, offsetX, padT, totalColH);
                offsetX += row.getSize().getX();
            }
        }

        if (this.centreRows != null) {
            for (TipRow row : this.centreRows) {
                row.onRender(this, mc, offsetX, offsetY, trcW);
                offsetY += row.getSize().getY();
            }
        }

        if (this.columns2 != null) {
            offsetX += totalCol1W + trcW;
            for (TipColumn row : this.columns2) {
                row.onRender(this, mc, offsetX, padT, totalColH);
                offsetX += row.getSize().getX();
            }
        }

        if (this.footerRows != null) {
            for (TipRow row : this.footerRows) {
                row.onRender(this, mc, padL, offsetY, trfW);
                offsetY += row.getSize().getY();
            }
        }

        GL11.glPopMatrix();
    }

    private static final int BLOCK_SIZE_PX = 24;

    public static void renderOverlay(ItemStack stack, List<String> lines, Point pos) {
        Minecraft mc = ModLoader.getMinecraftInstance();
        final int fixedLineGap = 12;
        final int textAreaHeight = fixedLineGap * lines.size();
        final int pad = 5;

        int box_w = 0, box_h = 0;
        for (int i = 0; i < lines.size(); i++) {
            box_w = Math.max(box_w, mc.fontRenderer.getStringWidth(lines.get(i)) + BLOCK_SIZE_PX);
            if (i != 0) {
                box_h = Math.max(BLOCK_SIZE_PX, box_h + fixedLineGap);
            }
        }

        box_w += pad;
        box_h += pad;

        ScaledResolution vp = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

        // int x = ((int) (mc.displayWidth / scale) - w - 1) * pos.x / 10000;
        int boxX = (vp.getScaledWidth() / 2) - (box_w / 2);
        int boxY = (vp.getScaledHeight() - box_h - 1) * 50 / 10000 + 5;


        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);

        drawGradientRect(boxX, boxY - 2, boxX + box_w, boxY, Color.GRAY.getRGB(), Color.GRAY.getRGB());
        drawGradientRect(boxX, boxY + box_h, boxX + box_w, boxY + box_h + 2, Color.GRAY.getRGB(), Color.GRAY.getRGB());
        drawGradientRect(boxX - 2, boxY - 2, boxX, boxY + box_h + 2, Color.GRAY.getRGB(), Color.GRAY.getRGB());
        drawGradientRect(boxX + box_w, boxY - 2, boxX + box_w + 2, boxY + box_h + 2, Color.GRAY.getRGB(), Color.GRAY.getRGB());
        drawGradientRect(boxX, boxY, boxX + box_w, boxY + box_h, -1072689136, -804253680);

        GL11.glDisable(32826);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int oY = (box_h - textAreaHeight) / 2;
        for (int i = 0; i < lines.size(); i++)
            mc.fontRenderer.drawString(lines.get(i), boxX + BLOCK_SIZE_PX, oY + boxY + (fixedLineGap * i) + 2, 0xffffff);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(32826);

        RenderHelper.enableGUIStandardItemLighting();
        if (stack.getItem() != null)
            drawItem2(boxX + 5, (boxY + (box_h / 2)) - 8, stack, mc);

        GL11.glPopMatrix();
    }

    private static void drawItem2(int x, int y, ItemStack item, Minecraft mc) {
        RenderItem render = (RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class);
        enable3DRender();
        render.zLevel = 200.0F;
        render.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, item, x, y);
        render.zLevel = 0.0F;
        enable2DRender();
    }

    public static void enable3DRender() {
        GL11.glEnable(2896);
        GL11.glEnable(2929);
    }

    public static void enable2DRender() {
        GL11.glDisable(2896);
        GL11.glDisable(2929);
    }

    protected static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        float var7 = (float) ((par5 >> 24) & 255) / 255.0F;
        float var8 = (float) ((par5 >> 16) & 255) / 255.0F;
        float var9 = (float) ((par5 >> 8) & 255) / 255.0F;
        float var10 = (float) (par5 & 255) / 255.0F;
        float var11 = (float) ((par6 >> 24) & 255) / 255.0F;
        float var12 = (float) ((par6 >> 16) & 255) / 255.0F;
        float var13 = (float) ((par6 >> 8) & 255) / 255.0F;
        float var14 = (float) (par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex(par3, par2, 100.0);
        var15.addVertex(par1, par2, 100.0);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex(par1, par4, 100.0);
        var15.addVertex(par3, par4, 100.0);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawTooltipBox(int x, int y, int w, int h, int bg, int grad1, int grad2) {
        drawGradientRect(x + 1, y, w - 1, 1, bg, bg);
        drawGradientRect(x + 1, y + h, w - 1, 1, bg, bg);
        drawGradientRect(x + 1, y + 1, w - 1, h - 1, bg, bg);
        drawGradientRect(x, y + 1, 1, h - 1, bg, bg);
        drawGradientRect(x + w, y + 1, 1, h - 1, bg, bg);
        drawGradientRect(x + 1, y + 2, 1, h - 3, grad1, grad2);
        drawGradientRect((x + w) - 1, y + 2, 1, h - 3, grad1, grad2);
        drawGradientRect(x + 1, y + 1, w - 1, 1, grad1, grad1);
        drawGradientRect(x + 1, (y + h) - 1, w - 1, 1, grad2, grad2);
    }
}
