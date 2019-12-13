package com.arrogantgamer.cucurbits;

import com.arrogantgamer.cucurbits.block.AttachedCorecumberStemBlock;
import com.arrogantgamer.cucurbits.block.AttachedLavamelonStemBlock;
import com.arrogantgamer.cucurbits.block.CorecumberBlock;
import com.arrogantgamer.cucurbits.block.CorecumberStemBlock;
import com.arrogantgamer.cucurbits.block.LavamelonBlock;
import com.arrogantgamer.cucurbits.block.LavamelonStemBlock;
import com.arrogantgamer.cucurbits.item.LavamelonItem;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = Cucurbits.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {
    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
	LavamelonBlock lavamelon = new LavamelonBlock(LavamelonBlock.properties);
	CorecumberBlock corecumber = new CorecumberBlock(CorecumberBlock.properties);	

	event.getRegistry().registerAll(
		setup(lavamelon, "lavamelon"),
		setup(new LavamelonStemBlock(lavamelon, LavamelonStemBlock.properties), "lavamelon_stem"),
		setup(new AttachedLavamelonStemBlock(lavamelon, AttachedLavamelonStemBlock.properties), "attached_lavamelon_stem"),
		
		setup(corecumber, "corecumber"),
		setup(new CorecumberStemBlock(corecumber, LavamelonStemBlock.properties), "corecumber_stem"),
		setup(new AttachedCorecumberStemBlock(corecumber, AttachedCorecumberStemBlock.properties), "attached_corecumber_stem")		
		);
    }

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
	event.getRegistry().registerAll(
		setup(new BlockNamedItem(ModBlocks.LAVAMELON_STEM, (new Item.Properties()).group(ItemGroup.MATERIALS)), "lavamelon_seeds"),
		setup(new LavamelonItem(new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)), ModBlocks.LAVAMELON.getRegistryName()),
		
		setup(new BlockNamedItem(ModBlocks.CORECUMBER_STEM, (new Item.Properties()).group(ItemGroup.MATERIALS)), "corecumber_seeds")
		);
    }

    public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
	return setup(entry, new ResourceLocation(Cucurbits.MODID, name));
    }

    public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
	entry.setRegistryName(registryName);
	return entry;
    }
}
