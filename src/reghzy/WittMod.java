package reghzy;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.forge.NetworkMod;
import reghzy.witt.WittTickHandler;

import java.util.logging.Logger;

/**
 * The Witt mod class, containing information about WhatIsThisTHing
 */
public abstract class WittMod extends NetworkMod {
    public static final Logger logger = Logger.getLogger("WITT");
    private static WittMod instance;
    private KeyBinding keyOpenMenu;

    public WittMod() {
        instance = this;
    }

    public static WittMod getInstance() {
        return instance;
    }

    @Override
    public String getVersion() {
        return "1.0.1";
    }

    @Override
    public void load() {
        FMLCommonHandler.instance().registerTickHandler(new WittTickHandler());

        // TODO: soon
        // ModLoader.registerKey(this, this.keyOpenMenu = new KeyBinding("key.witt.openmenu", 82), false); // NUM0
    }
}
