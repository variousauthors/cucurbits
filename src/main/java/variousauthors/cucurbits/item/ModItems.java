package variousauthors.cucurbits.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import variousauthors.cucurbits.Cucurbits;
import variousauthors.cucurbits.item.tool.*;

public class ModItems {
    public static ItemOre ingotCopper = new ItemOre("ingot_copper", "ingotCopper");
    public static ItemCornSeed cornSeed = new ItemCornSeed();
    public static ItemCorn corn = new ItemCorn();

    public static ItemSword copperSword = new ItemSword(Cucurbits.copperToolMaterial, "copper_sword");
    public static ItemPickaxe copperPickaxe = new ItemPickaxe(Cucurbits.copperToolMaterial, "copper_pickaxe");
    public static ItemAxe copperAxe = new ItemAxe(Cucurbits.copperToolMaterial, "copper_axe");
    public static ItemHoe copperHoe = new ItemHoe(Cucurbits.copperToolMaterial, "copper_hoe");
    public static ItemShovel copperShovel = new ItemShovel(Cucurbits.copperToolMaterial, "copper_shovel");

    public static ItemArmor copperHelmet = new ItemArmor(Cucurbits.copperArmorMaterial, EntityEquipmentSlot.HEAD, "copper_helmet");
    public static ItemArmor copperChestplate = new ItemArmor(Cucurbits.copperArmorMaterial, EntityEquipmentSlot.CHEST, "copper_chestplate");
    public static ItemArmor copperLeggings = new ItemArmor(Cucurbits.copperArmorMaterial, EntityEquipmentSlot.LEGS, "copper_leggings");
    public static ItemArmor copperBoots = new ItemArmor(Cucurbits.copperArmorMaterial, EntityEquipmentSlot.FEET, "copper_boots");

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
                copperBoots
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
    }
}