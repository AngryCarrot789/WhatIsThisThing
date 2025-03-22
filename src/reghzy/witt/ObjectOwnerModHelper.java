package reghzy.witt;

import java.util.HashMap;

public class ObjectOwnerModHelper {
    public static final HashMap<Integer, String> bId2ModName = new HashMap<Integer, String>();

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

        bId2ModName.put(48, "RedPower"); // eloraam.world.BlockCobbleMossifier
        bId2ModName.put(85, "Railcraft"); // railcraft.common.rails.BlockFenceReplacement
        bId2ModName.put(98, "RedPower"); // eloraam.world.BlockBrickMossifier
        bId2ModName.put(126, "Equivalent Exchange 2");
        bId2ModName.put(127, "Equivalent Exchange 2");
        bId2ModName.put(128, "Equivalent Exchange 2");
        bId2ModName.put(129, "Equivalent Exchange 2");
        bId2ModName.put(130, "Equivalent Exchange 2");
        bId2ModName.put(133, "RedPower");
        bId2ModName.put(134, "RedPower");
        bId2ModName.put(135, "Nether Ores");
        bId2ModName.put(136, "RedPower");
        bId2ModName.put(137, "RedPower");
        bId2ModName.put(138, "RedPower");
        bId2ModName.put(139, "RedPower");
        bId2ModName.put(140, "RedPower");
        bId2ModName.put(141, "RedPower");
        bId2ModName.put(142, "RedPower");
        bId2ModName.put(143, "RedPower");
        bId2ModName.put(144, "RedPower");
        bId2ModName.put(145, "RedPower");
        bId2ModName.put(146, "RedPower");
        bId2ModName.put(147, "RedPower");
        bId2ModName.put(148, "RedPower");
        bId2ModName.put(150, "RedPower");
        bId2ModName.put(151, "RedPower");
        bId2ModName.put(152, "RedPower");
        bId2ModName.put(153, "Buildcraft");
        bId2ModName.put(154, "Buildcraft");
        bId2ModName.put(155, "Buildcraft");
        bId2ModName.put(156, "Buildcraft");
        bId2ModName.put(157, "Buildcraft");
        bId2ModName.put(158, "Buildcraft");
        bId2ModName.put(159, "Buildcraft");
        bId2ModName.put(160, "Buildcraft");
        bId2ModName.put(161, "Buildcraft");
        bId2ModName.put(162, "Buildcraft");
        bId2ModName.put(163, "Buildcraft");
        bId2ModName.put(164, "Buildcraft");
        bId2ModName.put(165, "Buildcraft");
        bId2ModName.put(166, "Buildcraft");
        bId2ModName.put(167, "Buildcraft");
        bId2ModName.put(169, "Buildcraft");
        bId2ModName.put(170, "Buildcraft");
        bId2ModName.put(171, "Buildcraft");
        bId2ModName.put(172, "Buildcraft");
        bId2ModName.put(173, "Buildcraft");
        bId2ModName.put(174, "Buildcraft");
        bId2ModName.put(175, "Buildcraft");
        bId2ModName.put(176, "Buildcraft");
        bId2ModName.put(177, "RedPower");
        bId2ModName.put(178, "Enderchests");
        bId2ModName.put(179, "Buildcraft");
        bId2ModName.put(181, "Iron Chests");
        bId2ModName.put(183, "Compact Solars");
        bId2ModName.put(187, "Industrial Craft 2");
        bId2ModName.put(188, "Advanced Machines (IC2)");
        bId2ModName.put(190, "Power Converters");
        bId2ModName.put(192, "Industrial Craft 2");
        bId2ModName.put(194, "Tubestuff");
        bId2ModName.put(206, "Railcraft");
        bId2ModName.put(207, "Computercraft");
        bId2ModName.put(208, "Computercraft");
        bId2ModName.put(209, "Railcraft");
        bId2ModName.put(211, "Railcraft");
        bId2ModName.put(212, "Railcraft");
        bId2ModName.put(213, "Railcraft");
        bId2ModName.put(214, "Railcraft");
        bId2ModName.put(215, "CC Sensors");
        bId2ModName.put(216, "Computercraft");
        bId2ModName.put(217, "Industrial Craft 2");
        bId2ModName.put(218, "Industrial Craft 2");
        bId2ModName.put(219, "Industrial Craft 2");
        bId2ModName.put(220, "Industrial Craft 2");
        bId2ModName.put(221, "Industrial Craft 2");
        bId2ModName.put(222, "Industrial Craft 2");
        bId2ModName.put(223, "Industrial Craft 2");
        bId2ModName.put(224, "Industrial Craft 2");
        bId2ModName.put(225, "Industrial Craft 2");
        bId2ModName.put(226, "Industrial Craft 2");
        bId2ModName.put(227, "Industrial Craft 2");
        bId2ModName.put(228, "Industrial Craft 2");
        bId2ModName.put(229, "Industrial Craft 2");
        bId2ModName.put(230, "Industrial Craft 2");
        bId2ModName.put(231, "Industrial Craft 2");
        bId2ModName.put(232, "Industrial Craft 2");
        bId2ModName.put(233, "Industrial Craft 2");
        bId2ModName.put(234, "Industrial Craft 2");
        bId2ModName.put(235, "Industrial Craft 2");
        bId2ModName.put(236, "Industrial Craft 2");
        bId2ModName.put(237, "Industrial Craft 2");
        bId2ModName.put(238, "Industrial Craft 2");
        bId2ModName.put(239, "Industrial Craft 2");
        bId2ModName.put(240, "Industrial Craft 2");
        bId2ModName.put(241, "Industrial Craft 2");
        bId2ModName.put(242, "Industrial Craft 2");
        bId2ModName.put(243, "Industrial Craft 2");
        bId2ModName.put(244, "Industrial Craft 2");
        bId2ModName.put(245, "Industrial Craft 2");
        bId2ModName.put(246, "Industrial Craft 2");
        bId2ModName.put(247, "Industrial Craft 2");
        bId2ModName.put(248, "Industrial Craft 2");
        bId2ModName.put(249, "Industrial Craft 2");
        bId2ModName.put(250, "Industrial Craft 2");
        bId2ModName.put(253, "Modular Force Field System");
        bId2ModName.put(254, "Modular Force Field System");
        bId2ModName.put(255, "Modular Force Field System");
        bId2ModName.put(4095, "Immibis");

//        0 = "mod_CodeChickenCore 0.5.3"
//        1 = "mod_MinecraftForge 3.3.8.164"
//        2 = "mod_NotEnoughItems 1.2.2.4"
//        3 = "mod_ReiMinimap v3.2_04 [1.2.5]"
//        4 = "mod_IC2 v1.97"
//        5 = "mod_IC2AdvancedMachines v4.0"
//        6 = "mod_BuildCraftCore 2.2.14"
//        7 = "mod_BuildCraftBuilders 2.2.14"
//        8 = "mod_BuildCraftEnergy 2.2.14"
//        9 = "mod_BuildCraftFactory 2.2.14"
//        10 = "mod_BuildCraftTransport 2.2.14"
//        11 = "mod_AdditionalPipes 2.1.3 (Minecraft 1.2.5, Buildcraft 2.2.14, Forge 3.0.1.75)"
//        12 = "mod_ComputerCraft 1.33"
//        13 = "mod_RedPowerMachine 2.0pr5b2"
//        14 = "mod_ccSensors MC1.2.5 Build017pr1"
//        15 = "mod_CCTurtle 1.33"
//        16 = "mod_EE 1.4.6.5"
//        17 = "mod_EnderStorage 1.1.3"
//        18 = "mod_IC2NuclearControl v1.1.10"
//        19 = "mod_ImmibisCore 49.1.1"
//        20 = "mod_InvTweaks 1.41b (1.2.4)"
//        21 = "mod_LumySkinPatch 1.0.13"
//        22 = "mod_MAtmos_forModLoader r12 for 1.1.x"
//        23 = "mod_ModularForceFieldSystem rev7"
//        24 = "mod_IC2_ChargingBench 1.95b"
//        25 = "mod_CompactSolars 2.3.2"
//        26 = "mod_IronChest 3.8"
//        27 = "mod_RedPowerCore 2.0pr5b2"
//        28 = "mod_NetherOres 1.2.5R1.2.2"
//        29 = "mod_PowerConverters 1.2.5R1.3.4"
//        30 = "mod_Railcraft 5.3.3"
//        31 = "mod_RedPowerControl 2.0pr5b2"
//        32 = "mod_RedPowerLighting 2.0pr5b2"
//        33 = "mod_RedPowerLogic 2.0pr5b2"
//        34 = "mod_RedPowerWiring 2.0pr5b2"
//        35 = "mod_RedPowerWorld 2.0pr5b2"
//        36 = "mod_TubeStuff 49.1.2"
//        37 = "Balkon's WeaponMod 1.2.5 v8.6.0"
//        38 = "mod_WorldEditCUI 1.2.5 for Minecraft version 1.2.5"
//        39 = "mod_WirelessRedstoneCore 1.2.2.3"
//        40 = "mod_WirelessRedstoneAddons 1.2.2.3"
//        41 = "mod_WirelessRedstoneRedPower 1.2.2.1"
//        42 = "mod_ImmiChunkLoaders rev3.2"
    }
}
