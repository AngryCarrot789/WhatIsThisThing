package reghzy.witt.integration.mods;

import ee.BlockEETorch;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import reghzy.WittMod;
import reghzy.witt.integration.BlockItemStackProvider;
import reghzy.witt.integration.ToolTipIntegration;

public class EE2Integration {
    public static void init() {
        try {
            // The interdiction torches has a weird implementation where you can register multiple
            // torches with different metadata, but it also uses meta to represent direction.
            // There is only 1 torch with a meta of 0, with a technical range of 1-5, but if you
            // try to get the name of the item using a meta that isn't registered (e.g. 1-5),
            // it throws an exception. The vanilla torch has no such issue :/ So we fix it here
            ToolTipIntegration.getInstance().registerBlockItemProvider(BlockEETorch.class, new BlockItemStackProvider() {
                @Override
                public ItemStack getBlockAsItem(World world, int x, int y, int z, int bId, int bData) {
                    return new ItemStack(bId, 1, 0);
                }
            });
        }
        catch (Throwable e) {
            WittMod.logger.info("EE2 not found - skipping integration");
        }
    }
}
