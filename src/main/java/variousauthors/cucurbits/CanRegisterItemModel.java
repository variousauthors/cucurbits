package variousauthors.cucurbits;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import variousauthors.cucurbits.Cucurbits;

public interface CanRegisterItemModel {
    net.minecraft.util.ResourceLocation getRegistryName();

    default void registerItemModel() {
        if (this instanceof Item) {
            Cucurbits.proxy.registerItemRenderer((Item) this, 0, getRegistryName());
        } else if (this instanceof Block) {
            Cucurbits.proxy.registerItemRenderer(Item.getItemFromBlock((Block) this), 0, getRegistryName());
        }
    }
}
