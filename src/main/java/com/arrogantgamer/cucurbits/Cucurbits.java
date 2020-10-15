package com.arrogantgamer.cucurbits;

import com.arrogantgamer.cucurbits.init.ModItems;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, acceptedMinecraftVersions = Constants.ACCEPTED_MINECRAFT_VERSIONS)
public class Cucurbits {
    @Instance
    public static Cucurbits instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
	System.out.println(Constants.MODID + ":preInit");
	ModItems.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
	System.out.println(Constants.MODID + ":init");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
	System.out.println(Constants.MODID + ":postInit");
    }
}
