package reghzy.witt;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRedstoneRepeater;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.forge.IShearable;
import reghzy.witt.data.ColumnPlacement;
import reghzy.witt.data.RowPlacement;
import reghzy.witt.data.cols.TipColumn_ItemSprite;
import reghzy.witt.data.rows.TipRowTileFurnace;
import reghzy.witt.data.rows.TipRowBlockType;
import reghzy.witt.data.rows.TipRowTextSimple;
import reghzy.witt.data.ToolTip;
import reghzy.witt.integration.BlockInfoProvider;
import reghzy.witt.integration.BlockItemStackProvider;
import reghzy.witt.integration.mods.EE2Integration;
import reghzy.witt.integration.mods.IC2Integration;
import reghzy.witt.integration.TileInfoProvider;
import reghzy.witt.integration.mods.RedPowerIntegration;
import reghzy.witt.integration.ToolTipIntegration;
import reghzy.witt.utils.StringUtils;
import reghzy.witt.utils.Thickness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LookHandler {
    private static LookHandler instance;
    private ToolTip tip;
    private final Minecraft mc;
    private MovingObjectPosition target = null;

    public LookHandler() {
        this.mc = ModLoader.getMinecraftInstance();

        // show furnace progress
        ToolTipIntegration.getInstance().registerTileInfoProvider(TileEntityFurnace.class, new TileInfoProvider() {
            @Override
            public void provide(ToolTip tip, TileEntity tile) {
                tip.addRow(new TipRowTileFurnace((TileEntityFurnace) tile, new Thickness(5, 2, 5, 5)), RowPlacement.FOOTER);
            }
        });

        // show vanilla repeater delay
        class RSRepeaterProvider implements BlockInfoProvider, BlockItemStackProvider {

            @Override
            public void provide(ToolTip tip, World world, int x, int y, int z, int bId) {
                int delay = (world.getBlockMetadata(x, y, z) & 12) >> 2;
                double secs = (double) delay / 20.0;
                tip.addRow(new TipRowTextSimple("Delay: " + delay + " ticks (" + StringUtils.d2s(secs, 2) + "s)", new Thickness(1, 1, 5, 3)), tip.size(RowPlacement.CENTRE) - 1, RowPlacement.CENTRE);
            }

            @Override
            public ItemStack getBlockAsItem(World world, int x, int y, int z, int bId, int bData) {
                return new ItemStack(Item.redstoneRepeater.shiftedIndex, 0, 0);
            }
        }

        RSRepeaterProvider provider = new RSRepeaterProvider();
        ToolTipIntegration.getInstance().registerBlockInfoProvider(BlockRedstoneRepeater.class, provider);
        ToolTipIntegration.getInstance().registerBlockItemProvider(BlockRedstoneRepeater.class, provider);

        RedPowerIntegration.init();
        IC2Integration.init();
        EE2Integration.init();
    }

    public static LookHandler getInstance() {
        if (instance == null)
            instance = new LookHandler();
        return instance;
    }

    public void onTickPlayer() {
        this.tip = null;
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
            this.target = this.mc.objectMouseOver;
            return;
        }

        EntityLiving viewpoint = this.mc.renderViewEntity;
        this.target = viewpoint != null ? rayTrace(viewpoint, 4.0D, 0.0F) : null;
        if (this.target != null) {
            ItemStack targetStack = getTargetStack();
            if (targetStack != null) {
                this.tip = new ToolTip();
                this.tip.addColumn(ColumnPlacement.LEFT, new TipColumn_ItemSprite(targetStack));
                this.tip.addRow(new TipRowBlockType(targetStack, new Thickness(1, 5, 5, 3)), RowPlacement.CENTRE);
                int bId = this.mc.theWorld.getBlockId(this.target.blockX, this.target.blockY, this.target.blockZ);
                String modName = ObjectOwnerModHelper.bId2ModName.get(bId);
                if (modName == null)
                    modName = "Minecraft";

                this.tip.addRow(new TipRowTextSimple("\u00A7b\u00A7o" + modName, new Thickness(1, 3, 5, 5)), RowPlacement.CENTRE);

                ToolTipIntegration.getInstance().provideBlockInformation(this.tip, this.mc.theWorld, this.target.blockX, this.target.blockY, this.target.blockZ);
                TileEntity tile = this.mc.theWorld.getBlockTileEntity(this.target.blockX, this.target.blockY, this.target.blockZ);
                if (tile != null) {
                    ToolTipIntegration.getInstance().provideTileInformation(this.tip, tile);
                }
            }
        }
    }

    public MovingObjectPosition getTarget() {
        return this.target;
    }

    public ItemStack getTargetStack() {
        if (this.target.typeOfHit == EnumMovingObjectType.TILE) {
            return getTargetItemStack();
        }

        return null;
    }

    public MovingObjectPosition rayTrace(EntityLiving entity, double distance, float interpolate) {
        Vec3D pos = entity.getPosition(interpolate);
        Vec3D look = entity.getLook(interpolate);
        Vec3D end = pos.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);

        final boolean rayTraceLiquid = true;
        return entity.worldObj.rayTraceBlocks_do(pos, end, rayTraceLiquid);
    }

    public ItemStack getTargetItemStack() {
        if (this.target == null) {
            return null;
        }

        World world = this.mc.theWorld;
        int x = this.target.blockX;
        int y = this.target.blockY;
        int z = this.target.blockZ;
        int blockID = world.getBlockId(x, y, z);
        int blockData = world.getBlockMetadata(x, y, z);
        Block mouseBlock = Block.blocksList[blockID];
        if (mouseBlock != null) {
            ItemStack providedItemStack = ToolTipIntegration.getInstance().getBlockAsItem(world, x, y, z, blockID, blockData);
            if (providedItemStack != null) {
                return providedItemStack;
            }

            if (world.getBlockTileEntity(x, y, z) == null) {
                try {
                    ItemStack is = new ItemStack(mouseBlock, 1, blockData);
                    return is; // variable for debugging
                }
                catch (Exception e) { /* ignored */ }
            }

            try {
                ArrayList<ItemStack> list = mouseBlock.getBlockDropped(world, x, y, z, blockData, 0);
                if (!list.isEmpty()) {
                    return list.get(0);
                }
            }
            catch (Exception e) { /* ignored */ }

            if (mouseBlock instanceof IShearable) {
                IShearable shear = (IShearable) mouseBlock;
                if (shear.isShearable(new ItemStack(Item.shears), world, x, y, z)) {
                    ArrayList<ItemStack> list = shear.onSheared(new ItemStack(Item.shears), world, x, y, z, 0);
                    Collections.sort(list, new Comparator<ItemStack>() {
                        @Override
                        public int compare(ItemStack a, ItemStack b) {
                            return b.getItemDamage() - a.getItemDamage();
                        }
                    });

                    return list.get(0);
                }
            }

            return new ItemStack(mouseBlock, 1, blockData);
        }

        return null;
    }

    public ToolTip getCurrentTip() {
        return this.tip;
    }
}
