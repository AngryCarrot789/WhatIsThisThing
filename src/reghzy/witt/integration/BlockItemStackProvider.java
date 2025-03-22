package reghzy.witt.integration;

import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

/**
 * Provides a block as an item that the player would use to re-place said block.
 * <p>
 * Most of the type, an IS can be created from the bId and bData, but in some cases,
 * specifically when a block is a TileEntity, it's tricky to determine the actual
 * item to be placed (e.g. IC2 machines, we can do this, but what about RedPower
 * blocks like timers and repeaters? They all have the same bId and bData when placed)
 * </p>
 */
public interface BlockItemStackProvider {
    ItemStack getBlockAsItem(World world, int x, int y, int z, int bId, int bData);
}
