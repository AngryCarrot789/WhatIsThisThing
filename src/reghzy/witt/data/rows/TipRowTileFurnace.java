package reghzy.witt.data.rows;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntityFurnace;
import org.lwjgl.util.Point;
import reghzy.witt.data.ToolTip;
import reghzy.witt.utils.Thickness;

import java.text.MessageFormat;

public class TipRowTileFurnace extends TipRow {
    private final TileEntityFurnace furnace;
    private final Thickness padding;
    private String lastMsgForTick;
    private int lastMsgTick;

    public TipRowTileFurnace(TileEntityFurnace furnace, Thickness padding) {
        this.furnace = furnace;
        this.padding = padding;
    }

    private String getMessage(int burnTick) {
        if (burnTick != this.lastMsgTick || this.lastMsgForTick == null) {
            if (this.furnace.furnaceBurnTime < 1) {
                this.lastMsgForTick = "Not cooking anything";
            }
            else {
                this.lastMsgForTick = MessageFormat.format("Remaining: {0} seconds", (200 - this.furnace.furnaceCookTime) / 20);
            }

            this.lastMsgTick = burnTick;
        }

        return this.lastMsgForTick;
    }

    @Override
    public void onRender(ToolTip tip, Minecraft mc, int x, int y, int width) {
        super.onRender(tip, mc, x, y, width);
        TipRowText.drawText(getMessage(this.furnace.furnaceBurnTime), this.padding, ModLoader.getMinecraftInstance(), x, y);
    }

    @Override
    public Point getSize() {
        return TipRowText.measureText(getMessage(this.furnace.furnaceBurnTime), this.padding, ModLoader.getMinecraftInstance());
    }
}
