package variousauthors.scaffold;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public interface CanRegisterItemBlock {
    net.minecraft.util.ResourceLocation getRegistryName();

    default Item createItemBlock() {
        if (this instanceof Block) {
            return new ItemBlock((Block) this).setRegistryName(getRegistryName());
        } else {
            return null;
        }
    }
}
