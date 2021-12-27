package variousauthors.scaffold.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import variousauthors.scaffold.Scaffold;
import variousauthors.scaffold.block.bakers_squash.ItemBakersSquashSeed;
import variousauthors.scaffold.block.corecumber.ItemCorecumberSeed;
import variousauthors.scaffold.block.lavamelon.ItemLavamelonSeed;
import variousauthors.scaffold.item.tool.*;

public class ModItems {
    public static ItemOre ingotCopper = new ItemOre("ingot_copper", "ingotCopper");
    public static ItemCornSeed cornSeed = new ItemCornSeed();
    public static ItemCorn corn = new ItemCorn();

    public static ItemLavamelonSeed lavamelonSeed = new ItemLavamelonSeed("seeds_lavamelon");
    public static ItemCorecumberSeed corecumberSeed = new ItemCorecumberSeed("seeds_corecumber");
    public static ItemBakersSquashSeed bakersSquashSeed = new ItemBakersSquashSeed("seeds_bakers_squash");

    public static ItemSword copperSword = new ItemSword(Scaffold.copperToolMaterial, "copper_sword");
    public static ItemPickaxe copperPickaxe = new ItemPickaxe(Scaffold.copperToolMaterial, "copper_pickaxe");
    public static ItemAxe copperAxe = new ItemAxe(Scaffold.copperToolMaterial, "copper_axe");
    public static ItemHoe copperHoe = new ItemHoe(Scaffold.copperToolMaterial, "copper_hoe");
    public static ItemShovel copperShovel = new ItemShovel(Scaffold.copperToolMaterial, "copper_shovel");

    public static ItemArmor copperHelmet = new ItemArmor(Scaffold.copperArmorMaterial, EntityEquipmentSlot.HEAD, "copper_helmet");
    public static ItemArmor copperChestplate = new ItemArmor(Scaffold.copperArmorMaterial, EntityEquipmentSlot.CHEST, "copper_chestplate");
    public static ItemArmor copperLeggings = new ItemArmor(Scaffold.copperArmorMaterial, EntityEquipmentSlot.LEGS, "copper_leggings");
    public static ItemArmor copperBoots = new ItemArmor(Scaffold.copperArmorMaterial, EntityEquipmentSlot.FEET, "copper_boots");

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                ingotCopper,
                cornSeed,
                corn,
                copperSword,
                copperHoe,
                copperAxe,
                copperShovel,
                copperPickaxe,
                copperHelmet,
                copperChestplate,
                copperLeggings,
                copperBoots,
                lavamelonSeed,
                corecumberSeed,
                bakersSquashSeed
        );
    }

    public static void registerModels() {
        cornSeed.registerItemModel();
        ingotCopper.registerItemModel();
        corn.registerItemModel();
        copperSword.registerItemModel();
        copperHoe.registerItemModel();
        copperAxe.registerItemModel();
        copperShovel.registerItemModel();
        copperPickaxe.registerItemModel();
        copperHelmet.registerItemModel();
        copperChestplate.registerItemModel();
        copperLeggings.registerItemModel();
        copperBoots.registerItemModel();
        lavamelonSeed.registerItemModel();
        corecumberSeed.registerItemModel();
        bakersSquashSeed.registerItemModel();
    }
}