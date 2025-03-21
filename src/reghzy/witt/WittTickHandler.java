package reghzy.witt;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import reghzy.witt.data.ToolTip;

import java.util.EnumSet;

public class WittTickHandler implements ITickHandler {
    private static WittTickHandler instance;
    private final EnumSet<TickType> ticks = EnumSet.of(TickType.RENDER, TickType.PLAYER);
    private final Minecraft mc;

    public WittTickHandler() {
        this.mc = ModLoader.getMinecraftInstance();
        instance = this;
    }

    public static WittTickHandler getInstance() {
        return instance;
    }

    @Override
    public void tickStart(EnumSet<TickType> enumSet, Object... objects) {
        // if (this.currentScreen == null || this.currentScreen.allowUserInput)
    }

    @Override
    public void tickEnd(EnumSet<TickType> types, Object... objects) {
        if (types.contains(TickType.RENDER)) {
            if ((this.mc.currentScreen == null) && (this.mc.theWorld != null)) {
                // Do not draw when GUI is off or tab is pressed or no target
                if (!Minecraft.isGuiEnabled() || this.mc.gameSettings.keyBindPlayerList.pressed || (LookHandler.getInstance().getTarget() == null)) {
                    return;
                }

                ToolTip tip = LookHandler.getInstance().getCurrentTip();
                if (tip == null) {
                    return;
                }

                tip.onRender(this.mc, 0, 0, this.mc.displayWidth, this.mc.displayHeight);
            }
        }
        else if (types.contains(TickType.PLAYER)) {
            if (this.mc.theWorld != null && this.mc.thePlayer != null) {
                LookHandler.getInstance().onTickPlayer();
            }
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return ticks;
    }

    @Override
    public String getLabel() {
        return "WITT Tick Handler";
    }
}
