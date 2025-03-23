package reghzy.witt;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import reghzy.WittMod;

import java.io.File;
import java.security.CodeSource;
import java.util.HashMap;

public class ObjectOwnerModHelper {
    private static final HashMap<String, ModContainer> codeSourceToMod = new HashMap<String, ModContainer>();
    public static final HashMap<Integer, String> idToModDisplayNameFallback = new HashMap<Integer, String>();
    private static final HashMap<Integer, String> blockIdToModName = new HashMap<Integer, String>();
    private static final HashMap<Integer, String> itemIdToModName = new HashMap<Integer, String>();
    private static final HashMap<String, String> modIdToDisplayName = new HashMap<String, String>();

    public static void loadMods() {
        for (ModContainer mod : Loader.getModList()) {
            try {
                CodeSource protection = mod.getMod().getClass().getProtectionDomain().getCodeSource();
                if (protection != null) {
                    String location = protection.getLocation().getFile();
                    if (location == null) {
                        continue;
                    }

                    if (new File(location).isDirectory())
                        continue;

                    String fix = location.endsWith("/") || location.endsWith("\\") ? location.substring(0, location.length() - 1) : location;
                    if (fix.endsWith("modpack.jar") || fix.endsWith("minecraft.jar")) {
                        // do not add minecraft/forge things
                        continue;
                    }

                    codeSourceToMod.put(location, mod);
                }
            }
            catch (Throwable e) {
                WittMod.logger.warning("Error parsing mod " + mod.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Some mods do not ever specify their "readable name", only a mod id.
     * This can be used to convert a modId into a nice name, i.e. "mod_RedPowerCore" into "RedPower Core".
     * <p>
     *     These mappings are only used when no mod name is specified in the mod metadata file (mcmod.info)
     * </p>
     */
    public static void addModIdToDisplayNameMapping(String modId, String displayName) {
        if (displayName == null) {
            modIdToDisplayName.remove(modId);
        }
        else {
            modIdToDisplayName.put(modId, displayName);
        }
    }

    /**
     * Adds a fallback mod display name for block ids. Used as a last resort instead of showing "Minecraft"
     */
    public static void addFallbackModNameForId(String modName, int... itemOrBlockIds) {
        for (int id : itemOrBlockIds)
            idToModDisplayNameFallback.put(id, modName);
    }

    static {
        /*
            Use in debug evaluation and remove null entries with regex (\n[0-9]*=null):
                Object[] array = new Object[4096];
                System.arraycopy(Class.forName("pb").getField("m").get(null), 0, array, 0, 4096);
                AtomicInteger integer = new AtomicInteger();
                Arrays.stream(array).map(x -> {
                    return String.valueOf(integer.getAndIncrement()) + "=" + (x != null ? x.getClass().getName() : "null") + "\n";
                }).collect(Collectors.joining());
         */

        addFallbackModNameForId("RedPower", 48); // eloraam.world.BlockCobbleMossifier
        addFallbackModNameForId("Railcraft", 85); // railcraft.common.rails.BlockFenceReplacement
        addFallbackModNameForId("RedPower", 98); // eloraam.world.BlockBrickMossifier
        addFallbackModNameForId("Railcraft", 206, 209, 211, 212, 213, 214);
        addFallbackModNameForId("Equivalent Exchange 2", 126, 127, 128, 129, 130);
        addFallbackModNameForId("RedPower", 133, 134, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 150, 151, 152, 177);
        addFallbackModNameForId("Nether Ores", 135);
        addFallbackModNameForId("Buildcraft", 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 169, 170, 171, 172, 173, 174, 175, 176, 179);
        addFallbackModNameForId("Enderchests", 178);
        addFallbackModNameForId("Iron Chests", 181);
        addFallbackModNameForId("Compact Solar Arrays", 183);
        addFallbackModNameForId("Industrial Craft 2", 187);
        addFallbackModNameForId("Advanced Machines (IC2)", 188);
        addFallbackModNameForId("Power Converters", 190);
        addFallbackModNameForId("Industrial Craft 2", 192);
        addFallbackModNameForId("Tubestuff", 194);
        addFallbackModNameForId("ComputerCraft", 207, 208);
        addFallbackModNameForId("CC Sensors", 215);
        addFallbackModNameForId("Computercraft", 216);
        addFallbackModNameForId("Industrial Craft 2", 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250);
        addFallbackModNameForId("Modular ForceField Systems", 253, 254, 255);
        addFallbackModNameForId("Immibis", 4095);

        addModIdToDisplayNameMapping("mod_CodeChickenCore", "CodeChicken Core");
        addModIdToDisplayNameMapping("mod_MinecraftForge", "Minecraft Forge");
        addModIdToDisplayNameMapping("mod_NotEnoughItems", "Not Enough Items");
        addModIdToDisplayNameMapping("mod_ReiMinimap", "Rei's Minimap");
        addModIdToDisplayNameMapping("mod_RedPowerCore", "RedPower");
        addModIdToDisplayNameMapping("mod_RedPowerMachine", "RedPower Machines");
        addModIdToDisplayNameMapping("mod_RedPowerControl", "RedPower Control");
        addModIdToDisplayNameMapping("mod_RedPowerLighting", "RedPower Lighting");
        addModIdToDisplayNameMapping("mod_RedPowerLogic", "RedPower Logic");
        addModIdToDisplayNameMapping("mod_RedPowerWiring", "RedPower Wiring");
        addModIdToDisplayNameMapping("mod_RedPowerWorld", "RedPower World");
        addModIdToDisplayNameMapping("mod_IC2", "IndustrialCraft 2");
        addModIdToDisplayNameMapping("mod_IC2AdvancedMachines", "Advanced Machines (IC2)");
        addModIdToDisplayNameMapping("mod_BuildCraftCore", "BuildCraft");
        addModIdToDisplayNameMapping("mod_BuildCraftBuilders", "BuildCraft Builders");
        addModIdToDisplayNameMapping("mod_BuildCraftEnergy", "BuildCraft Energy");
        addModIdToDisplayNameMapping("mod_BuildCraftFactory", "BuildCraft Factory");
        addModIdToDisplayNameMapping("mod_BuildCraftTransport", "BuildCraft Transport");
        addModIdToDisplayNameMapping("mod_AdditionalPipes", "Additional Pipes");
        addModIdToDisplayNameMapping("mod_ComputerCraft", "ComputerCraft");
        addModIdToDisplayNameMapping("mod_ccSensors", "CC Sensors");
        addModIdToDisplayNameMapping("mod_CCTurtle", "CC Turtles");
        addModIdToDisplayNameMapping("mod_EE", "Equivalent Exchange 2");
        addModIdToDisplayNameMapping("mod_EnderStorage", "Ender Storage");
        addModIdToDisplayNameMapping("mod_IC2NuclearControl", "Nuclear Control (IC2)");
        addModIdToDisplayNameMapping("mod_ImmibisCore", "Immibis");
        addModIdToDisplayNameMapping("mod_InvTweaks", "InvTweaks");
        addModIdToDisplayNameMapping("mod_LumySkinPatch", "LumySkinPatch");
        addModIdToDisplayNameMapping("mod_MAtmos_forModLoader", "MAtmos");
        addModIdToDisplayNameMapping("mod_ModularForceFieldSystem", "Modular ForceField Systems");
        addModIdToDisplayNameMapping("mod_IC2_ChargingBench", "Charging Bench (IC2)");
        addModIdToDisplayNameMapping("mod_CompactSolars", "Compact Solar Arrays");
        addModIdToDisplayNameMapping("mod_IronChest", "Iron Chest");
        addModIdToDisplayNameMapping("mod_NetherOres", "Nether Ores");
        addModIdToDisplayNameMapping("mod_PowerConverters", "Power Converters");
        addModIdToDisplayNameMapping("mod_Railcraft", "Railcraft");
        addModIdToDisplayNameMapping("mod_TubeStuff", "Tube Stuff");
        addModIdToDisplayNameMapping("mod_WirelessRedstoneCore", "WR-CBE");
        addModIdToDisplayNameMapping("mod_WirelessRedstoneAddons", "WR-CBE Addons");
        addModIdToDisplayNameMapping("mod_WirelessRedstoneRedPower", "WR-CBE RedPower");
        addModIdToDisplayNameMapping("mod_ImmiChunkLoaders", "Immibis Chunk Loaders");
    }

    public static String getModNameForBlock(int id) {
        String name = blockIdToModName.get(id);
        if (name != null && !name.isEmpty()) {
            return name;
        }

        Block block = id > 0 && id < Block.blocksList.length ? Block.blocksList[id] : null;
        if (block != null) {
            name = getModForObjectSlow(block);
        }

        if (name == null || name.isEmpty()) {
            if ((name = idToModDisplayNameFallback.get(id)) == null || name.isEmpty()) {
                name = "Minecraft";
            }

            blockIdToModName.put(id, name);
        }

        return name;
    }

    public static String getModNameForItemId(int id) {
        String name = itemIdToModName.get(id);
        if (name != null && !name.isEmpty()) {
            return name;
        }

        Item item = id > 0 && id < Item.itemsList.length ? Item.itemsList[id] : null;
        if (item != null) {
            name = getModForObjectSlow(item);
        }

        if (name == null || name.isEmpty()) {
            if ((name = idToModDisplayNameFallback.get(id)) == null || name.isEmpty()) {
                name = "Minecraft";
            }

            itemIdToModName.put(id, name);
        }

        return name;
    }

    public static String getModForObjectSlow(Object object) {
        String codeSrcFile;
        ModContainer container;
        try {
            CodeSource protection = object.getClass().getProtectionDomain().getCodeSource();
            if (protection == null) {
                return null;
            }

            codeSrcFile = protection.getLocation().getFile();
            if (codeSrcFile == null || codeSrcFile.isEmpty()) {
                return null;
            }
        }
        catch (Throwable ignored) {
            return null;
        }

        if ((container = codeSourceToMod.get(codeSrcFile)) != null) {
            ModMetadata metadata = container.getMetadata();

            // Get metadata name
            String modName = metadata != null ? metadata.name : null;
            if (modName == null || modName.isEmpty())
                modName = modIdToDisplayName.get(container.getName());

            // If no luck, get mod name
            if (modName == null || modName.isEmpty())
                modName = container.getName();

            if (modName == null)
                modName = "";

            if (modName.startsWith("mod_"))
                modName = modName.substring(4);

            if (!modName.isEmpty() && !modName.equalsIgnoreCase("minecraftforge")) {
                // upper case first letter just in case we're using a boring mod id
                if (modName.length() > 1)
                    modName = Character.toUpperCase(modName.charAt(0)) + modName.substring(1);

                return modName;
            }
        }

        return null;
    }
}
