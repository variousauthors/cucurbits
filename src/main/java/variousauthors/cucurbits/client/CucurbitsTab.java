package variousauthors.cucurbits.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import variousauthors.cucurbits.Cucurbits;
import variousauthors.cucurbits.item.ModItems;

public class CucurbitsTab extends CreativeTabs {
    public CucurbitsTab() {
        super(Cucurbits.modId);
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
