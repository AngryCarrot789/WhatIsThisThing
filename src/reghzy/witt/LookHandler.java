package reghzy.witt;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
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
import reghzy.witt.data.RowPlacement;
import reghzy.witt.data.cols.TipColumn_ItemSprite;
import reghzy.witt.data.rows.TipRowTileFurnace;
import reghzy.witt.data.rows.TipRowBlockType;
import reghzy.witt.data.rows.TipRowTextConstant;
import reghzy.witt.data.ToolTip;
import reghzy.witt.integration.IDataProvider;
import reghzy.witt.integration.ToolTipIntegration;
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
        ToolTipIntegration.getInstance().registerTileProvider(TileEntityFurnace.class, new IDataProvider() {
            @Override
            public void provide(ToolTip tip, TileEntity tile) {
                tip.addRow(new TipRowTileFurnace((TileEntityFurnace) tile), RowPlacement.FOOTER);
            }
        });
    }

    public static LookHandler getInstance() {
        if (instance == null)
            instance = new LookHandler();
        return instance;
    }

    public void onTickPlayer() {
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
            this.target = this.mc.objectMouseOver;
            return;
        }

        this.tip = null;
        EntityLiving viewpoint = this.mc.renderViewEntity;
        this.target = viewpoint != null ? rayTrace(viewpoint, 4.0D, 0.0F) : null;
        if (this.target != null) {
            ItemStack targetStack = getTargetStack();
            if (targetStack != null) {
                this.tip = new ToolTip();
                this.tip.addColumn(true, new TipColumn_ItemSprite(targetStack));
                this.tip.addRow(new TipRowBlockType(targetStack, new Thickness(1, 5, 5, 3)), RowPlacement.CENTRE);

                int bId = this.mc.theWorld.getBlockId(this.target.blockX, this.target.blockY, this.target.blockZ);
                String modName = TranslationHelper.bid2ModName.get(bId);
                if (modName == null)
                    modName = "Minecraft";

                this.tip.addRow(new TipRowTextConstant((char) 167 + "8" + modName, new Thickness(1, 3, 5, 5)), RowPlacement.CENTRE);

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
            return getIdentifierStack();
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

    public ItemStack getIdentifierStack() {
        ArrayList<ItemStack> items = getIdentifierItems();
        if (items.isEmpty())
            return null;
        Collections.sort(items, new Comparator<ItemStack>() {
            @Override
            public int compare(ItemStack a, ItemStack b) {
                return b.getItemDamage() - a.getItemDamage();
            }
        });
        return items.get(0);
    }

    public ArrayList<ItemStack> getIdentifierItems() {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        if (this.target == null)
            return items;
        World world = this.mc.theWorld;
        int x = this.target.blockX;
        int y = this.target.blockY;
        int z = this.target.blockZ;
        int blockID = world.getBlockId(x, y, z);
        int blockData = world.getBlockMetadata(x, y, z);
        Block mouseBlock = Block.blocksList[blockID];
        if (mouseBlock != null) {
            if (world.getBlockTileEntity(x, y, z) == null) {
                try {
                    ItemStack block = new ItemStack(mouseBlock, 1, blockData);
                    items.add(block);
                    return items;
                }
                catch (Exception e) { /* ignored */ }
            }

            try {
                ArrayList<ItemStack> list = mouseBlock.getBlockDropped(world, x, y, z, blockData, 0);
                if (list.size() > 0) {
                    items.add(list.get(0));
                    return items;
                }
            }
            catch (Exception e) { /* ignored */ }

            if (mouseBlock instanceof IShearable) {
                IShearable shear = (IShearable) mouseBlock;
                if (shear.isShearable(new ItemStack(Item.shears), world, x, y, z))
                    items.addAll(shear.onSheared(new ItemStack(Item.shears), world, x, y, z, 0));
            }

            if (items.size() == 0)
                items.add(0, new ItemStack(mouseBlock, 1, blockData));
        }

        return items;
    }

    public ToolTip getCurrentTip() {
        return this.tip;
    }
}
