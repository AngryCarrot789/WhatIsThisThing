package reghzy.witt.integration;

import net.minecraft.src.TileEntity;
import reghzy.witt.data.ToolTip;

public interface TileInfoProvider {
    /**
     * Provides tooltip data for a tile-entity
     * @param tip  The tooltip being created
     * @param tile The tile the player is looking at
     */
    void provide(ToolTip tip, TileEntity tile);
}
