package variousauthors.scaffold.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import variousauthors.scaffold.Scaffold;
import variousauthors.scaffold.item.ModItems;

public class ScaffoldTab extends CreativeTabs {
    public ScaffoldTab() {
        super(Scaffold.modId);
        setBackgroundImageName("item_search.png");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.ingotCopper);
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
