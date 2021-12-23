package variousauthors.scaffold.block.lavamelon;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import variousauthors.scaffold.block.BlockBase;
import variousauthors.scaffold.item.ItemBase;

import java.util.Random;

public class BlockLavamelon extends BlockBase {
    public BlockLavamelon()
    {
        super(Material.GOURD, "lavamelon", MapColor.LIME);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    class ItemBlockLavamelon extends ItemBlock {
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
