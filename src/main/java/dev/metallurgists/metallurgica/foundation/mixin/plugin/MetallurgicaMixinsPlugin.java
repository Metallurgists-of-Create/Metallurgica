package dev.metallurgists.metallurgica.foundation.mixin.plugin;

import dev.metallurgists.metallurgica.foundation.util.CommonUtil;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MetallurgicaMixinsPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("dev.metallurgists.metallurgica.foundation.mixin.jei")) {
            return CommonUtil.isClassFound("mezz.jei.api.IModPlugin");
        }
        if (mixinClassName.contains("dev.metallurgists.metallurgica.foundation.mixin.emi")) {
            return CommonUtil.isClassFound("dev.emi.emi.api.EmiPlugin");
        }
        return true;
    }
    
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    
    }
    
    @Override
    public List<String> getMixins() {
        return null;
    }
    
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    
    }
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    
    }
}
