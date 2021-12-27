package variousauthors.scaffold.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import variousauthors.scaffold.block.bakers_squash.BlockBakersSquash;
import variousauthors.scaffold.block.bakers_squash.BlockStemBakersSquash;
import variousauthors.scaffold.block.corecumber.BlockCorecumber;
import variousauthors.scaffold.block.corecumber.BlockStemCorecumber;
import variousauthors.scaffold.block.counter.BlockCounter;
import variousauthors.scaffold.block.lavamelon.BlockLavamelon;
import variousauthors.scaffold.block.lavamelon.BlockStemLavamelon;
import variousauthors.scaffold.block.pedestal.BlockPedestal;

public class ModBlocks {
    public static BlockOre oreCopper = new BlockOre("ore_copper", "oreCopper");
    public static BlockCropCorn cropCorn = new BlockCropCorn();
    public static BlockPedestal pedestal = new BlockPedestal();

    public static BlockCounter counter = new BlockCounter();

    public static BlockLavamelon lavamelon = new BlockLavamelon("lavamelon");
    public static BlockStemLavamelon stemLavamelon = new BlockStemLavamelon(lavamelon, "stem_lavamelon");

    public static BlockCorecumber corecumber = new BlockCorecumber("corecumber");
    public static BlockStemCorecumber stemCorecumber = new BlockStemCorecumber(corecumber, "stem_corecumber");

    /* alternative names: Gherkiln, kiln cucumber, kiln squash */
    public static BlockBakersSquash bakersSquash = new BlockBakersSquash("bakers_squash");
    public static BlockStemBakersSquash stemBakersSquash = new BlockStemBakersSquash(bakersSquash, "stem_bakers_squash");

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                oreCopper,
                cropCorn,
                pedestal,
                counter,
                lavamelon,
                stemLavamelon,
                corecumber,
                stemCorecumber,
                bakersSquash,
                stemBakersSquash
        );

        GameRegistry.registerTileEntity(pedestal.getTileEntityClass(), pedestal.getRegistryName().toString());
        GameRegistry.registerTileEntity(corecumber.getTileEntityClass(), corecumber.getRegistryName().toString());
        GameRegistry.registerTileEntity(bakersSquash.getTileEntityClass(), bakersSquash.getRegistryName().toString());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                oreCopper.createItemBlock(),
                pedestal.createItemBlock(),
                counter.createItemBlock(),
                lavamelon.createItemBlock(),
                corecumber.createItemBlock(),
                bakersSquash.createItemBlock()
        );
    }

    public static void registerModels() {
        oreCopper.registerItemModel();
        pedestal.registerItemModel();
        counter.registerItemModel();
        lavamelon.registerItemModel();
        corecumber.registerItemModel();
        bakersSquash.registerItemModel();
    }

}
