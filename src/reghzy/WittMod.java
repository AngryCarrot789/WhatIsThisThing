package reghzy;

import net.minecraft.src.forge.NetworkMod;

import java.util.logging.Logger;

public abstract class WittMod extends NetworkMod {
    public static final Logger logger = Logger.getLogger("WITT");
    private static WittMod instance;

    public WittMod() {
        instance = this;
    }

    public static WittMod getInstance() {
        return instance;
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
