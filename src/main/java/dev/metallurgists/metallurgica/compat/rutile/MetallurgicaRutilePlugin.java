package dev.metallurgists.metallurgica.compat.rutile;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.element.MetallurgicaElements;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.handlers.MetallurgicaMaterialRecipes;
import dev.metallurgists.metallurgica.compat.rutile.materials.*;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.registry.MCreativeTabs;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler.IRutileMaterialRecipeHandler;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePlugin;

@RutilePlugin
public class MetallurgicaRutilePlugin implements IRutilePlugin {
    @Override
    public String getPluginNamespace() {
        return Metallurgica.ID;
    }

    @Override
    public MetallurgicaRegistrate getRegistrate() {
        return (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MCreativeTabs.MATERIALS);
    }

    @Override
    public IRutileMaterialRecipeHandler getRuntimeMaterialRecipes() {
        return new MetallurgicaMaterialRecipes();
    }

    @Override
    public void registerElements() {
        MetallurgicaElements.init();
    }

    @Override
    public void registerFlags() {
        MetallurgicaFlagKeys.init();
    }
}
