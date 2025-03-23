package reghzy;

import codechicken.nei.API;
import codechicken.nei.IHandleTooltip;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.NetworkMod;
import reghzy.witt.ObjectOwnerModHelper;
import reghzy.witt.WittConfig;
import reghzy.witt.WittTickHandler;
import reghzy.witt.gui.GuiWittSettings;

import java.util.List;
import java.util.logging.Logger;

/**
 * The Witt mod class, containing information about WhatIsThisTHing
 */
public abstract class WittMod extends NetworkMod {
    public static final Logger logger = Logger.getLogger("WITT");
    private static WittMod instance;
    private KeyBinding keyOpenMenu;
    private WittConfig currentConfig;

    public WittMod() {
        instance = this;
        this.currentConfig = new WittConfig();
    }

    public static WittMod getInstance() {
        return instance;
    }

    @Override
    public String getVersion() {
        return "1.0.2";
    }

    @Override
    public void load() {
        FMLCommonHandler.instance().registerTickHandler(new WittTickHandler());

        // ModLoader.registerKey(this, this.keyOpenMenu = new KeyBinding("key.witt.openmenu", 82), false); // NUM0
        ModLoader.registerKey(this, this.keyOpenMenu = new KeyBinding("WhatIsThisThing Settings", 82), false); // NUM0
    }

    @Override
    public void modsLoaded() {
        super.modsLoaded();
        ObjectOwnerModHelper.loadMods();

        try {
            API.addTooltipHandler(new NEIIntegration());
        }
        catch (Throwable t) {
            WittMod.logger.info("Failed to load NEI tooltip");
        }
    }

    public static class NEIIntegration implements IHandleTooltip {
        @Override
        public List<String> handleTooltip(ItemStack itemstack, List<String> list) {
            if (WittMod.getInstance().getConfig().canShowModNameInNEI) {
                String mod = ObjectOwnerModHelper.getModNameForItemId(itemstack.itemID);
                if (mod != null) {
                    list.add("\u00A7b\u00A7o" + mod);
                }
            }

            return list;
        }
    }

    @Override
    public void keyboardEvent(KeyBinding event) {
        if (event == this.keyOpenMenu) {
            ModLoader.getMinecraftInstance().displayGuiScreen(new GuiWittSettings());
        }
    }

    public WittConfig getConfig() {
        return this.currentConfig;
    }
}
