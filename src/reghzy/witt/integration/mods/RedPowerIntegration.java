package reghzy.witt.integration.mods;

import eloraam.logic.TileLogicPointer;
import eloraam.logic.TileLogicSimple;
import net.minecraft.src.TileEntity;
import reghzy.WittMod;
import reghzy.witt.data.RowPlacement;
import reghzy.witt.data.ToolTip;
import reghzy.witt.data.rows.TipRowTextSimple;
import reghzy.witt.integration.TileInfoProvider;
import reghzy.witt.integration.ToolTipIntegration;
import reghzy.witt.utils.ReflectUtils;
import reghzy.witt.utils.StringUtils;
import reghzy.witt.utils.Thickness;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RedPowerIntegration {
    private static final Method Md_IIntervalSet_GetInterval = ReflectUtils.getMd("eloraam.logic.IIntervalSet", "GetInterval");
    private static final Field Fd_TileLogic_Deadmap = ReflectUtils.findField("eloraam.logic.TileLogic", "Deadmap");
    private static final Field Fd_TileLogic_SubId = ReflectUtils.findField("eloraam.logic.TileLogic", "SubId");
    private static final Field Fd_TileLogic_Active = ReflectUtils.findField("eloraam.logic.TileLogic", "Active");

    public static void init() {
        try {
            // Show timer/sequencer interval
            ToolTipIntegration.getInstance().registerTileInfoProvider(TileLogicPointer.class, new TileInfoProvider() {
                @Override
                public void provide(ToolTip tip, TileEntity tile) {
                    // Since I just added the mod as a dependency from the client mods folder,
                    // and client mods are obfuscated, we have to cast to object first.
                    // Obfuscated TE class is kw, which TileLogicSimple extends.

                    Integer subId = (Integer) ReflectUtils.getFieldObj(Fd_TileLogic_SubId, tile);
                    double interval = ((Long) ReflectUtils.invokeMd(Md_IIntervalSet_GetInterval, tile)) / 20.0;

                    // "Interval" doesn't really make sense for a state cell, since it's basically a far
                    // more adjustable repeater. I think "countdown" makes more sense than "delay" though
                    String prefix = subId == 2 ? "Countdown" : "Interval";
                    tip.addRow(new TipRowTextSimple(prefix + ": " + StringUtils.d2s(interval, 2) + "s"), tip.size(RowPlacement.CENTRE) - 1, RowPlacement.CENTRE);
                }
            });

            // Show repeater delay
            ToolTipIntegration.getInstance().registerTileInfoProvider(TileLogicSimple.class, new TileInfoProvider() {
                @Override
                public void provide(ToolTip tip, TileEntity tile) {
                    Integer subId = (Integer) ReflectUtils.getFieldObj(Fd_TileLogic_SubId, tile);

                    // only show delay for repeaters, since every other TileLogicSimple has a fixed delay of 1 tick, AFAIK
                    if (subId == 12) {
                        int delay = 1 << (Integer) ReflectUtils.getFieldObj(Fd_TileLogic_Deadmap, tile);
                        double secs = (double) delay / 20.0;

                        tip.addRow(new TipRowTextSimple("Delay: " + delay + " ticks (" + StringUtils.d2s(secs, 2) + "s)", new Thickness(1, 1, 5, 3)), tip.size(RowPlacement.CENTRE) - 1, RowPlacement.CENTRE);
                    }
                }
            });

            // Ehh...
            // ToolTipIntegration.getInstance().registerTileProvider(TileLogicAdv.class, new IDataProvider() {
            //     @Override
            //     public void provide(ToolTip tip, TileEntity tile) {
            //         Boolean isActive = (Boolean) ReflectUtils.getFieldObj(Fd_TileLogic_Active, tile);
            //         tip.addRow(new TipRowTextSimple(isActive ? "Output: HIGH" : "Output: LOW", new Thickness(1, 1, 5, 3)), tip.size(RowPlacement.CENTRE) - 1, RowPlacement.CENTRE);
            //     }
            // });

            // This does not work for some reason; the two methods return 0.
            // Which is weird since it's the same code as the GUI (e.g. GuiSorter). Perhaps opening the GUI engages it...
            // ToolTipIntegration.getInstance().registerTileProvider(IBluePowerConnectable.class, new IDataProvider() {
            //     @Override
            //     public void provide(ToolTip tip, TileEntity tile) {
            //         IBluePowerConnectable connectable = (IBluePowerConnectable) tile;
            //         BluePowerConductor conductor = connectable.getBlueConductor();
            //         tip.addRow(new TipRowTextSimple("Battery: " + conductor.getChargeScaled(48) + "%", new Thickness(1, 2, 5, 2)), tip.size(RowPlacement.CENTRE) - 1, RowPlacement.CENTRE);
            //         tip.addRow(new TipRowTextSimple("Flow: " + conductor.getFlowScaled(48) + "%", new Thickness(1, 2, 5, 2)), tip.size(RowPlacement.CENTRE) - 1, RowPlacement.CENTRE);
            //     }
            // });
        }
        catch (Throwable e) {
            WittMod.logger.info("RedPower not found - skipping integration");
        }
    }
}
