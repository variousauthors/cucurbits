package variousauthors.cucurbits.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import variousauthors.cucurbits.block.ModBlocks;
import variousauthors.cucurbits.item.ModItems;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(ModBlocks.oreCopper, new ItemStack(ModItems.ingotCopper), 0.7f);

        ModBlocks.oreCopper.initOreDict();
        ModItems.ingotCopper.initOreDict();
        ModItems.corn.initOreDict();
    }

}
