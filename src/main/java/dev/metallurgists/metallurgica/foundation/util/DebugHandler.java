package dev.metallurgists.metallurgica.foundation.util;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.config.MetallurgicaConfigs;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

import java.util.List;

public final class DebugHandler {

    private DebugHandler() {}

    private static final String PREFIX = ChatFormatting.YELLOW + "[Metallurgica] " + ChatFormatting.RESET;

    public static void onDrawDebugText(List<String> left) {
        Minecraft mc = Minecraft.getInstance();
        Level world = mc.level;
        if (mc.options.renderDebug && MetallurgicaConfigs.client().debugInfo.get()) {
            left.add("");
            String version = ModList.get().getModContainerById(Metallurgica.ID).get().getModInfo().getVersion().toString();
            int materialCount = RutileAPI.materialManager.getRegisteredMaterials().size();
            left.add(PREFIX + "Version: " + version + ", Materials: " + materialCount);
        }
    }
}
