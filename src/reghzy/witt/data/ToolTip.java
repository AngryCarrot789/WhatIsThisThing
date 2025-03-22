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

/**
 * Contains all rows and columns to be rendered. The tooltip grid is arranged as such:
 * <br/><b>HEADER ROW SPANS ENTIRE LENGTH</b>
 * <br/><b>HEADER ROW SPANS ENTIRE LENGTH</b>
 *
 * <table border="1">
 * <tr><th>  </th><th>  </th><th>CENTRE ROW</th><th>  </th><th>  </th></tr>
 * <tr><th>LC</th><th>LC</th><th>CENTRE ROW</th><th>RC</th><th>RC</th></tr>
 * <tr><th>  </th><th>  </th><th>CENTRE ROW</th><th>  </th><th>  </th></tr>
 * </table>
 * <br/><b>FOOTER ROW SPANS ENTIRE LENGTH</b>
 * <br/><b>FOOTER ROW SPANS ENTIRE LENGTH</b>
 *
 * <p>
 *     LC stands for left column, RC stands for right column. Colums span the entire vertical height, Rows span the horizontal width.
 *     Header and footer rows' X positions start at the same as the left column, whereas centre rows start at the largest width value of any left column
 * </P>
 */
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
        addRow(row, size(placement), placement);
    }

    public void addRow(TipRow row, int index, RowPlacement placement) {
        switch (placement) {
            case CENTRE:
                if (this.centreRows == null)
                    this.centreRows = new ArrayList<TipRow>();
                this.centreRows.add(index, row);
                break;
            case HEADER:
                if (this.headerRows == null)
                    this.headerRows = new ArrayList<TipRow>();
                this.headerRows.add(index, row);
                break;
            case FOOTER:
                if (this.footerRows == null)
                    this.footerRows = new ArrayList<TipRow>();
                this.footerRows.add(index, row);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + placement);
        }
    }

    public void addColumn(ColumnPlacement placement, TipColumn column) {
        addColumn(placement, size(placement), column);
    }

    public void addColumn(ColumnPlacement placement, int index, TipColumn column) {
        switch (placement) {
            case LEFT:
                if (this.columns1 == null)
                    this.columns1 = new ArrayList<TipColumn>();
                this.columns1.add(index, column);
                break;
            case RIGHT:
                if (this.columns2 == null)
                    this.columns2 = new ArrayList<TipColumn>();
                this.columns2.add(index, column);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + placement);
        }
    }

    public int size(RowPlacement placement) {
        switch (placement) {
            case CENTRE:
                return this.centreRows != null ? this.centreRows.size() : 0;
            case HEADER:
                return this.headerRows != null ? this.headerRows.size() : 0;
            case FOOTER:
                return this.footerRows != null ? this.footerRows.size() : 0;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int size(ColumnPlacement placement) {
        switch (placement) {
            case LEFT:
                return this.columns1 != null ? this.columns1.size() : 0;
            case RIGHT:
                return this.columns2 != null ? this.columns2.size() : 0;
            default:
                throw new IllegalArgumentException();
        }
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
        int totalCentreRowsW = 0, totalCentreRowsH = 0;
        if (this.centreRows != null) {
            for (TipRow row : this.centreRows) {
                Point size = row.getSize();
                totalCentreRowsW = Math.max(totalCentreRowsW, size.getX());
                totalCentreRowsH += size.getY();
            }
        }

        int trhW = 0, trhH = 0;
        if (this.headerRows != null) {
            for (TipRow row : this.headerRows) {
                Point size = row.getSize();
                trhW = Math.max(trhW, size.getX());
                trhH += size.getY();
            }
        }

        int trfW = 0, trfH = 0;
        if (this.footerRows != null) {
            for (TipRow row : this.footerRows) {
                Point size = row.getSize();
                trfW = Math.max(trfW, size.getX());
                trfH += size.getY();
            }
        }

        int totalCol1W = 0, totalCol1H = 0;
        if (this.columns1 != null) {
            for (TipColumn column : this.columns1) {
                Point size = column.getSize();
                totalCol1W += size.getX();
                totalCol1H = Math.max(totalCol1H, size.getY());
            }
        }

        int totalCol2W = 0, totalCol2H = 0;
        if (this.columns2 != null) {
            for (TipColumn column : this.columns2) {
                Point size = column.getSize();
                totalCol2W += size.getX();
                totalCol2H = Math.max(totalCol2H, size.getY());
            }
        }

        final int padAll = 0;
        final int padL = padAll, padT = padAll, padR = padAll, padB = padAll;
        final int posY = 100;
        final int totalColH = Math.max(Math.max(totalCol1H, totalCentreRowsH), totalCol2H);
        final int containerW = Math.max(totalCol1W + totalCentreRowsW + totalCol2W, Math.max(trhW, trfW)) + padL + padR;
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
            int px = scrPosX, py = scrPosY;
            int w = containerW, h = containerH;
            drawGradientRect(px, py - 2, px + w, py, Color.GRAY.getRGB(), Color.GRAY.getRGB());
            drawGradientRect(px, py + h, px + w, py + h + 2, Color.GRAY.getRGB(), Color.GRAY.getRGB());
            drawGradientRect(px - 2, py - 2, px, py + h + 2, Color.GRAY.getRGB(), Color.GRAY.getRGB());
            drawGradientRect(px + w, py - 2, px + w + 2, py + h + 2, Color.GRAY.getRGB(), Color.GRAY.getRGB());
            drawGradientRect(px, py, px + w, py + h, -1072689136, -804253680);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
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
                row.onRender(this, mc, offsetX, offsetY, totalCentreRowsW);
                offsetY += row.getSize().getY();
            }
        }

        offsetX += totalCentreRowsW;
        if (this.columns2 != null) {
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

    protected static void drawGradientRect(int x1, int y1, int x2, int y2, int bgCol, int fgCol) {
        float bA = (float) ((bgCol >> 24) & 255) / 255.0F;
        float bR = (float) ((bgCol >> 16) & 255) / 255.0F;
        float bG = (float) ((bgCol >> 8) & 255) / 255.0F;
        float bB = (float) (bgCol & 255) / 255.0F;
        float fA = (float) ((fgCol >> 24) & 255) / 255.0F;
        float fR = (float) ((fgCol >> 16) & 255) / 255.0F;
        float fG = (float) ((fgCol >> 8) & 255) / 255.0F;
        float fB = (float) (fgCol & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setColorRGBA_F(bR, bG, bB, bA);
        t.addVertex(x2, y1, 100.0);
        t.addVertex(x1, y1, 100.0);
        t.setColorRGBA_F(fR, fG, fB, fA);
        t.addVertex(x1, y2, 100.0);
        t.addVertex(x2, y2, 100.0);
        t.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
