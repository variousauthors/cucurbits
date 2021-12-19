package variousauthors.scaffold;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface CanRegisterItemModel {
    net.minecraft.util.ResourceLocation getRegistryName();

    default void registerItemModel() {
        if (this instanceof Item) {
            Scaffold.proxy.registerItemRenderer((Item) this, 0, getRegistryName());
        } else if (this instanceof Block) {
            Scaffold.proxy.registerItemRenderer(Item.getItemFromBlock((Block) this), 0, getRegistryName());
        }
    }
}
