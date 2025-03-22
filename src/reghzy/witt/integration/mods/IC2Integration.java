package reghzy.witt.integration.mods;

import ic2.advancedmachines.BlockAdvancedMachines;
import ic2.common.BlockCable;
import ic2.common.BlockMachine;
import ic2.common.BlockMultiID;
import ic2.common.TileEntityElecMachine;
import ic2.common.TileEntityElectricBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import reghzy.WittMod;
import reghzy.witt.data.RowPlacement;
import reghzy.witt.data.ToolTip;
import reghzy.witt.data.rows.TipRowTextSimple;
import reghzy.witt.integration.BlockItemStackProvider;
import reghzy.witt.integration.TileInfoProvider;
import reghzy.witt.integration.ToolTipIntegration;
import reghzy.witt.utils.ReflectUtils;
import reghzy.witt.utils.Thickness;

import java.lang.reflect.Field;

public class IC2Integration {
    private static final Field Fd_TileEntityElecMachine_energy = ReflectUtils.findField("ic2.common.TileEntityElecMachine", "energy");
    private static final Field Fd_TileEntityElecMachine_maxEnergy = ReflectUtils.findField("ic2.common.TileEntityElecMachine", "maxEnergy");
    private static final Field Fd_TileEntityElecMachine_maxInput = ReflectUtils.findField("ic2.common.TileEntityElecMachine", "maxInput");
    private static final Field Fd_TileEntityElectricBlock_energy = ReflectUtils.findField("ic2.common.TileEntityElectricBlock", "energy");
    private static final Field Fd_TileEntityElectricBlock_maxStorage = ReflectUtils.findField("ic2.common.TileEntityElectricBlock", "maxStorage");
    private static final Field Fd_TileEntityElectricBlock_output = ReflectUtils.findField("ic2.common.TileEntityElectricBlock", "output");

    public static void init() {
        try {
            // Generic Machines
            ToolTipIntegration.getInstance().registerTileInfoProvider(TileEntityElecMachine.class, new TileInfoProvider() {
                @Override
                public void provide(ToolTip tip, TileEntity tile) {
                    int energy = (Integer) ReflectUtils.getFieldObj(Fd_TileEntityElecMachine_energy, tile);
                    int maxEnergy = (Integer) ReflectUtils.getFieldObj(Fd_TileEntityElecMachine_maxEnergy, tile);
                    int maxInput = (Integer) ReflectUtils.getFieldObj(Fd_TileEntityElecMachine_maxInput, tile);

                    tip.addRow(new TipRowTextSimple(energy + "/" + maxEnergy + " EU", new Thickness(1, 1, 5, 1)), tip.size(RowPlacement.CENTRE) - 1, RowPlacement.CENTRE);

                    String voltage;
                    if (maxInput < 32)
                        voltage = "ELV";
                    else if (maxInput < 128)
                        voltage = "LV";
                    else if (maxInput < 512)
                        voltage = "MV";
                    else if (maxInput < 1024)
                        voltage = "HV";
                    else
                        voltage = "\u00A7b\u00A7lEHV";

                    tip.addRow(new TipRowTextSimple("Voltage: " + voltage, new Thickness(1, 2, 5, 3)), tip.size(RowPlacement.CENTRE) - 1, RowPlacement.CENTRE);
                }
            });

            // Energy Storage Machine
            ToolTipIntegration.getInstance().registerTileInfoProvider(TileEntityElectricBlock.class, new TileInfoProvider() {
                @Override
                public void provide(ToolTip tip, TileEntity tile) {
                    int energy = (Integer) ReflectUtils.getFieldObj(Fd_TileEntityElectricBlock_energy, tile);
                    int maxStorage = (Integer) ReflectUtils.getFieldObj(Fd_TileEntityElectricBlock_maxStorage, tile);
                    int output = (Integer) ReflectUtils.getFieldObj(Fd_TileEntityElectricBlock_output, tile);

                    tip.addRow(new TipRowTextSimple(energy + "/" + maxStorage + " EU", new Thickness(1, 1, 5, 1)), tip.size(RowPlacement.CENTRE) - 1, RowPlacement.CENTRE);

                    String voltage;
                    if (output < 32)
                        voltage = "ELV";
                    else if (output < 128)
                        voltage = "LV";
                    else if (output < 512)
                        voltage = "MV";
                    else if (output < 1024)
                        voltage = "HV";
                    else
                        voltage = "\u00A7b\u00A7lEHV";

                    tip.addRow(new TipRowTextSimple("Voltage: " + voltage, new Thickness(1, 1, 5, 3)), tip.size(RowPlacement.CENTRE) - 1, RowPlacement.CENTRE);
                }
            });

            // IC2 machine blocks can be dropped as their raw item id:data
            ToolTipIntegration.getInstance().registerBlockItemProvider(BlockMultiID.class, new BlockItemStackProvider() {
                @Override
                public ItemStack getBlockAsItem(World world, int x, int y, int z, int bId, int bData) {
                    return new ItemStack(bId, 1, bData);
                }
            });

            ToolTipIntegration.getInstance().registerBlockItemProvider(BlockMachine.class, new BlockItemStackProvider() {
                @Override
                public ItemStack getBlockAsItem(World world, int x, int y, int z, int bId, int bData) {
                    return new ItemStack(bId, 1, bData);
                }
            });

            ToolTipIntegration.getInstance().registerBlockItemProvider(BlockCable.class, new BlockItemStackProvider() {
                @Override
                public ItemStack getBlockAsItem(World world, int x, int y, int z, int bId, int bData) {
                    return new ItemStack(30184, 1, bData);
                }
            });
        }
        catch (Throwable e) {
            WittMod.logger.info("IC2 not found - skipping integration");
        }

        try {
            // IC2/Advanced Machines machine blocks can be dropped as their raw item id:data
            ToolTipIntegration.getInstance().registerBlockItemProvider(BlockAdvancedMachines.class, new BlockItemStackProvider() {
                @Override
                public ItemStack getBlockAsItem(World world, int x, int y, int z, int bId, int bData) {
                    return new ItemStack(bId, 1, bData);
                }
            });
        }
        catch (Throwable e) {
            WittMod.logger.info("Advanced Machines (IC2 extension) not found - skipping integration");
        }
    }
}
