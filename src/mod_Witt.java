import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.forge.NetworkMod;
import reghzy.witt.WittTickHandler;

public class mod_Witt extends NetworkMod {
    private static mod_Witt instance;
    private KeyBinding keyOpenMenu;

    public mod_Witt() {
        instance = this;
    }

    public static mod_Witt getInstance() {
        return instance;
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void load() {
        FMLCommonHandler.instance().registerTickHandler(new WittTickHandler());

        // TODO: soon
        // ModLoader.registerKey(this, this.keyOpenMenu = new KeyBinding("key.witt.openmenu", 82), false); // NUM0
    }

    public KeyBinding getKeyOpenMenu() {
        return this.keyOpenMenu;
    }
}
