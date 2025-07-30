package dev.metallurgists.metallurgica.registry;

import dev.metallurgists.metallurgica.Metallurgica;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;
import net.minecraft.resources.ResourceLocation;

public class MetallurgicaSpriteShifts {
    
    public static final CTSpriteShiftEntry drillExpansion = vertical("drill_expansion");
    public static final CTSpriteShiftEntry blastProofGlass = omni("blast_proof_glass");
    public static final CTSpriteShiftEntry directionalMetalBlock = horizontal("directional_metal_block");

    public static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }
    
    private static CTSpriteShiftEntry horizontal(String name) {
        return getCT(AllCTTypes.HORIZONTAL, name);
    }
    
    private static CTSpriteShiftEntry vertical(String name) {
        return getCT(AllCTTypes.VERTICAL, name);
    }

    private static CTSpriteShiftEntry rectangle(String name) {
        return getCT(AllCTTypes.RECTANGLE, name);
    }

    public static CTSpriteShiftEntry materialOmni(Material material, FlagKey<?> flagKey) {
        return getMaterialCT(AllCTTypes.OMNIDIRECTIONAL, material, flagKey);
    }

    public static CTSpriteShiftEntry materialHorizontal(Material material, FlagKey<?> flagKey) {
        return getMaterialCT(AllCTTypes.HORIZONTAL, material, flagKey);
    }

    public static CTSpriteShiftEntry materialVertical(Material material, FlagKey<?> flagKey) {
        return getMaterialCT(AllCTTypes.VERTICAL, material, flagKey);
    }

    public static CTSpriteShiftEntry materialRectangle(Material material, FlagKey<?> flagKey) {
        return getMaterialCT(AllCTTypes.RECTANGLE, material, flagKey);
    }
    
    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Metallurgica.asResource(originalLocation), Metallurgica.asResource(targetLocation));
    }
    
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, Metallurgica.asResource("block/" + blockTextureName),
                Metallurgica.asResource("block/" + connectedTextureName + "_connected"));
    }
    
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }

    public static CTSpriteShiftEntry getMaterialCT(CTType type, Material material, FlagKey<?> flagKey) {
        return CTSpriteShifter.getCT(type,
                new ResourceLocation(material.getNamespace(), "block/materials/" + material.getName() + "/" + flagKey.getId().getPath()),
                new ResourceLocation(material.getNamespace(), "block/materials/" + material.getName() + "/" + flagKey.getId().getPath() + "_connected"));
    }
    
}
