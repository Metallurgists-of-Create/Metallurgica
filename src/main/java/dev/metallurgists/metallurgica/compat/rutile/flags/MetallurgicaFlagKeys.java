package dev.metallurgists.metallurgica.compat.rutile.flags;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.CasingFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.CogWheelFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.LargeCogWheelFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.SheetmetalFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.fluid.LiquidFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.fluid.MoltenFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.*;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import net.minecraft.resources.ResourceLocation;

public class MetallurgicaFlagKeys {

    //Items
    public static FlagKey<SheetFlag> SHEET = createFlag("sheet", SheetFlag.class);
    public static FlagKey<SemiPressedSheetFlag> SEMI_PRESSED_SHEET = createFlag("semi_pressed_sheet", SemiPressedSheetFlag.class);
    public static FlagKey<MineralFlag> MINERAL = createFlag("mineral", MineralFlag.class);
    public static FlagKey<RubbleFlag> RUBBLE = createFlag("rubble", RubbleFlag.class);
    public static FlagKey<WireFlag> WIRE = createFlag("wire", WireFlag.class);
    public static FlagKey<SpoolFlag> SPOOL = createFlag("spool", SpoolFlag.class);

    //Blocks
    public static FlagKey<CasingFlag> CASING = createFlag("casing", CasingFlag.class);
    public static FlagKey<SheetmetalFlag> SHEETMETAL = createFlag("sheetmetal", SheetmetalFlag.class);
    public static FlagKey<CogWheelFlag> COG_WHEEL = createFlag("cog_wheel", CogWheelFlag.class);
    public static FlagKey<LargeCogWheelFlag> LARGE_COG_WHEEL = createFlag("large_cog_wheel", LargeCogWheelFlag.class);

    //Fluids
    public static FlagKey<MoltenFlag> MOLTEN = createFlag("molten", MoltenFlag.class);
    public static FlagKey<LiquidFlag> LIQUID = createFlag("liquid", LiquidFlag.class);


    public static void init() {

    }

    private static <C extends IMaterialFlag> FlagKey<C> createFlag(String name, Class<C> type) {
        ResourceLocation location = Metallurgica.asResource(name);
        return RutileAPI.registerFlag(location, FlagKey.create(location.toString(), type));
    }
}
