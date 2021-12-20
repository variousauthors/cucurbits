package variousauthors.scaffold.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import variousauthors.scaffold.CanRegisterItemModel;
import variousauthors.scaffold.Scaffold;

public class BlockBase extends Block implements CanRegisterItemModel {

    protected String name;

    public BlockBase(Material material, String name, MapColor mapColor) {
        super(material, mapColor);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Scaffold.creativeTab);
    }

    public BlockBase(Material material, String name) {
        super(material);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Scaffold.creativeTab);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }

    @Override
    public BlockBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}
