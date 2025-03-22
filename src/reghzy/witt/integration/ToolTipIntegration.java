package reghzy.witt.integration;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import reghzy.witt.data.ToolTip;
import reghzy.witt.utils.ClassMultiMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class for allowing additional data to be supplied to a tooltip
 */
public class ToolTipIntegration {
    private static final ToolTipIntegration instance = new ToolTipIntegration();
    private final ClassMultiMap<TileInfoProvider> tileInfoProvider;
    private final ClassMultiMap<BlockInfoProvider> blockInfoProvider;

    // I don't think there's every a case we'd need an interface as a key, so ClassMultiMap seems pointless
    private final HashMap<Class<?>, ArrayList<BlockItemStackProvider>> blockTypeProvider;

    private ToolTipIntegration() {
        if (instance != null)
            throw new UnsupportedOperationException("What for!!!");

        this.tileInfoProvider = new ClassMultiMap<TileInfoProvider>();
        this.blockInfoProvider = new ClassMultiMap<BlockInfoProvider>();
        this.blockTypeProvider = new HashMap<Class<?>, ArrayList<BlockItemStackProvider>>();
    }

    /**
     * Registers a tile entity class, or interface a tile entity may implement, with an info provider.
     * @param tileOrItfClass The tile type or superclass of one or an interface implemented by a tile
     * @param provider       The data provider
     */
    public boolean registerTileInfoProvider(Class<?> tileOrItfClass, TileInfoProvider provider) {
        return this.tileInfoProvider.put(tileOrItfClass, provider);
    }

    /**
     * Registers a block class, or interface a block may implement, with an info provider.
     * @param blockOrItfClass The block type or superclass of one or an interface implemented by a block
     * @param provider        The data provider
     */
    public boolean registerBlockInfoProvider(Class<?> blockOrItfClass, BlockInfoProvider provider) {
        return this.blockInfoProvider.put(blockOrItfClass, provider);
    }

    /**
     * Registers a provider that turns a block into an item stack that the user can place.
     * The item stack is then used to figure out the display name to show at the top
     * of the tool tip (unless something else inserts a row above it...)
     * @param blockClass The block class.
     * @param provider   The provider
     */
    public boolean registerBlockItemProvider(Class<?> blockClass, BlockItemStackProvider provider) {
        ArrayList<BlockItemStackProvider> list = this.blockTypeProvider.get(blockClass);
        if (list == null)
            this.blockTypeProvider.put(blockClass, list = new ArrayList<BlockItemStackProvider>());
        else if (list.contains(provider))
            return false;
        list.add(provider);
        return true;
    }

    /**
     * Provides additional tooltip rows and columns for a specific tile, e.g. energy level, fluid levels, and so on.
     * @param tip        The tool tip being shown
     * @param tileEntity The tile the player is looking at
     */
    public void provideTileInformation(ToolTip tip, TileEntity tileEntity) {
        ArrayList<TileInfoProvider> list = this.tileInfoProvider.getValues(tileEntity.getClass());
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).provide(tip, tileEntity);
            }
        }
    }

    public void provideBlockInformation(ToolTip tip, World world, int x, int y, int z) {
        int bId = world.getBlockId(x, y, z);
        Block block = bId > 0 && bId < 4096 ? Block.blocksList[bId] : null;
        if (block == null) {
            return;
        }

        ArrayList<BlockInfoProvider> list = this.blockInfoProvider.getValues(block.getClass());
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).provide(tip, world, x, y, z, bId);
            }
        }
    }

    /**
     * Gets the first available item stack from a block that a {@link BlockItemStackProvider} provided.
     * Returns null when block ID does not exist or no providers are registered or all of them provided null
     * @param world The world
     * @param x     The block X position
     * @param y     The block Y position
     * @param z     The block Z position
     * @param bId   The block ID in the world as the coords
     * @param bData The block data in the world as the coords
     * @return The block as an itemstack
     */
    public ItemStack getBlockAsItem(World world, int x, int y, int z, int bId, int bData) {
        Block block = Block.blocksList[bId];
        if (block == null) {
            return null;
        }

        ItemStack stack;
        for (Class<?> klass = block.getClass(); klass != null; klass = klass.getSuperclass()) {
            ArrayList<BlockItemStackProvider> list = this.blockTypeProvider.get(klass);
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    if ((stack = list.get(i).getBlockAsItem(world, x, y, z, bId, bData)) != null) {
                        return stack;
                    }
                }
            }
        }

        return null;
    }

    public static ToolTipIntegration getInstance() {
        return instance;
    }
}
