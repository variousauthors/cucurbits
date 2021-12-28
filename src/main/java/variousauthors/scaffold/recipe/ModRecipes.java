package variousauthors.scaffold.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import variousauthors.scaffold.block.ModBlocks;
import variousauthors.scaffold.item.ModItems;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(ModBlocks.oreCopper, new ItemStack(ModItems.ingotCopper, 1), 0.7f);

        ModBlocks.oreCopper.initOreDict();
        ModItems.ingotCopper.initOreDict();
        ModItems.corn.initOreDict();
    }

}
