package variousauthors.scaffold.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import variousauthors.scaffold.Scaffold;

public class ItemBase extends Item {

    protected String name;
    protected int burnTime = -1; // default, mc will decide based on material etc

    public ItemBase(String name) {
        this.name = name;
        this.burnTime = -1;
        setUnlocalizedName(name);
        setRegistryName(Scaffold.modId, name);
        setCreativeTab(Scaffold.creativeTab);
    }

    public void registerItemModel() {
        Scaffold.proxy.registerItemRenderer(this, 0, getRegistryName());
    }

    /** set this to 0 for non-burnable and -1 for a-la-carte */
    public ItemBase setBurnTime(int burnTime) {
        this.burnTime = burnTime;
        return this;
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack)
    {
        return this.burnTime;
    }

}