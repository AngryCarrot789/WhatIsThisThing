package reghzy.witt.integration;

import net.minecraft.src.World;
import reghzy.witt.data.ToolTip;

public interface BlockInfoProvider {
    /**
     * Provides information about a block at some position in a world
     * @param tip The tooltip
     * @param world The world
     * @param x Block X
     * @param y Block Y
     * @param z Block Z
     * @param bId The block ID at the position, provided as we use it to fetch all {@link BlockInfoProvider} instances anyway
     */
    void provide(ToolTip tip, World world, int x, int y, int z, int bId);
}
