package variousauthors.scaffold.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import variousauthors.scaffold.Scaffold;

public class ItemBase extends Item {

    protected String name;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(Scaffold.modId, name);
        setCreativeTab(Scaffold.creativeTab);
    }

    public void registerItemModel() {
        Scaffold.proxy.registerItemRenderer(this, 0, getRegistryName());
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}