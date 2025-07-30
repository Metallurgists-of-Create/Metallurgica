package dev.metallurgists.metallurgica.compat.rutile.materials;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.MineralFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.RubbleFlag;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.registry.flags.DustFlag;

public class MineralMaterials {

    public static void modify() {

    }

    public static void register() {

    }

    public static final Material Malachite = new Material.Builder(Metallurgica.asResource("malachite"))
            .composition(RutileElements.COPPER, 2, RutileElements.CARBON, 1, RutileElements.OXYGEN, 3, RutileElements.HYDROGEN, 2)
                .addFlags(new MineralFlag()).buildAndRegister();

    public static final Material Magnetite = new Material.Builder(Metallurgica.asResource("magnetite"))
            .composition(RutileElements.IRON, 3, RutileElements.OXYGEN, 4)
                .addFlags(new MineralFlag(), new DustFlag()).buildAndRegister();

    public static final Material Hematite = new Material.Builder(Metallurgica.asResource("hematite"))
            .composition(RutileElements.IRON, 2, RutileElements.OXYGEN, 3)
                .addFlags(new MineralFlag()).buildAndRegister();

    public static final Material Bauxite = new Material.Builder(Metallurgica.asResource("bauxite"))
            .composition(RutileElements.ALUMINUM, 2, RutileElements.OXYGEN, 3, RutileElements.HYDROGEN, 2)
                .addFlags(new MineralFlag()).buildAndRegister();

    public static final Material Spodumene = new Material.Builder(Metallurgica.asResource("spodumene"))
            .composition(RutileElements.LITHIUM, 1, RutileElements.ALUMINUM, 1, RutileElements.SILICON, 1, RutileElements.OXYGEN, 3)
                .addFlags(new MineralFlag()).buildAndRegister();

    public static final Material Sphalerite = new Material.Builder(Metallurgica.asResource("sphalerite"))
            .composition(RutileElements.ZINC, 1, RutileElements.SULFUR, 1)
                .addFlags(new MineralFlag()).buildAndRegister();

    public static final Material Smithsonite = new Material.Builder(Metallurgica.asResource("smithsonite"))
            .composition(RutileElements.ZINC, 1, RutileElements.CARBON, 1, RutileElements.OXYGEN, 3)
                .addFlags(new MineralFlag()).buildAndRegister();

    public static final Material Rutile = new Material.Builder(Metallurgica.asResource("rutile"))
            .composition(RutileElements.TITANIUM, 1, RutileElements.OXYGEN, 2)
                .addFlags(new MineralFlag(), new DustFlag(true), new RubbleFlag().crushing().bonusChance(0.05f)).buildAndRegister();

    public static final Material Potash = new Material.Builder(Metallurgica.asResource("potash"))
            .composition(RutileElements.POTASSIUM, 1, RutileElements.CARBON, 1, RutileElements.OXYGEN, 3)
                .addFlags(new MineralFlag()).buildAndRegister();

    public static final Material Cassiterite = new Material.Builder(Metallurgica.asResource("cassiterite"))
            .composition(RutileElements.TIN, 1, RutileElements.OXYGEN, 2)
                .addFlags(new MineralFlag()).buildAndRegister();

    public static final Material Fluorite = new Material.Builder(Metallurgica.asResource("fluorite"))
            .composition(RutileElements.CALCIUM, 1, RutileElements.FLUORINE, 2)
                .addFlags(new MineralFlag(), new DustFlag(true)).buildAndRegister();

    public static final Material Cuprite = new Material.Builder(Metallurgica.asResource("cuprite"))
            .composition(RutileElements.COPPER, 1, RutileElements.OXYGEN, 2)
                .existingIds(RutileFlagKeys.DUST, "minecraft:redstone")
                .addFlags(new MineralFlag(), new DustFlag()).buildAndRegister();

    public static final Material Vanadinite = new Material.Builder(Metallurgica.asResource("vanadinite"))
            .composition(RutileElements.LEAD, 5, RutileElements.VANADIUM, 1, RutileElements.OXYGEN, 4, RutileElements.CHLORINE, 1)
                .addFlags(new MineralFlag(), new DustFlag(), new RubbleFlag().crushing().bonusChance(0.15f)).buildAndRegister();
}
