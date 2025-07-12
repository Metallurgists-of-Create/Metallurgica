package com.freezedown.metallurgica.content.primitive.mortar;

import net.createmod.catnip.config.ConfigBase;

public class MortarAndPestleConfig extends ConfigBase {

    public final ConfigFloat mortarHungerMultiplier = f(.32f, 0, 10, "mortarHungerMultiplier", Comments.mortarHungerMultiplier);
    @Override
    public String getName() {
        return "Configure Mortar and Pestle";
    }

    private static class Comments {
        static String mortarHungerMultiplier = "[allan please add details]";
    }
}
