package com.sintinium.oauth;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = "oauth",
        name = "OAuth",
        acceptableRemoteVersions = "*",
        acceptedMinecraftVersions = "[1.7.10]",
        version = Tags.VERSION)
public class OAuth {

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (event.getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new GuiEventHandler());
            Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
            OAuthConfig.load(cfg);
        }
    }
}
