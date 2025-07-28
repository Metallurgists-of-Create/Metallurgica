package dev.metallurgists.metallurgica.foundation.config.server;

import dev.metallurgists.metallurgica.foundation.config.server.subcat.MKinetics;
import dev.metallurgists.metallurgica.foundation.config.server.subcat.MMachineConfig;
import net.createmod.catnip.config.ConfigBase;

public class MServer extends ConfigBase {
    
    public final MKinetics kinetics = this.nested(0, MKinetics::new, "Parameters and abilities of Metallurgica's kinetic mechanisms");
    public final MMachineConfig machineConfig = this.nested(0, MMachineConfig::new, "Fine tune the behavior of machines");

    public final ConfigFloat mediumResistivity = f(0.015f, 0, 0.5f, "mediumResistivity", Comments.mediumResistivity);
    public final ConfigFloat highResistivity = f(0.03f, 0, 1f, "highResistivity", Comments.highResistivity);
    
    //public final MMachineConfig machineConfig = this.nested(0, MMachineConfig::new, "Fine tune the behavior of machines");
    @Override
    public String getName() {
        return "server";
    }
    
    private static class Comments {
        static String mediumResistivity = "Minimum resistivity to be considered 'medium'";
        static String highResistivity = "Minimum resistivity to be considered 'high'";
    }
}
