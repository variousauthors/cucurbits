package variousauthors.scaffold.block.lavamelon;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import variousauthors.scaffold.CucurbitFruit;
import variousauthors.scaffold.block.BlockBase;

public class BlockLavamelon extends BlockBase implements CucurbitFruit {
    public BlockLavamelon(String name)
    {
        super(Material.GOURD, name, MapColor.LIME);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    static class ItemBlockLavamelon extends ItemBlock {
        public ItemBlockLavamelon(Block block) {
            super(block);
        }

        @Override
        public int getItemBurnTime(ItemStack itemStack) {
            return 20000;
        }
    }

    public Item createItemBlock() {
        return new ItemBlockLavamelon(this).setRegistryName(getRegistryName());
    }
}
