package variousauthors.cucurbits.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import variousauthors.cucurbits.Cucurbits;

public class ItemBase extends Item {

    protected String name;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(Cucurbits.modId, name);
        setCreativeTab(Cucurbits.creativeTab);
    }

    public void registerItemModel() {
        Cucurbits.proxy.registerItemRenderer(this, 0, getRegistryName());
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}