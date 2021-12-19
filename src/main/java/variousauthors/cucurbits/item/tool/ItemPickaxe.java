package variousauthors.cucurbits.item.tool;

import net.minecraft.item.Item;
import variousauthors.cucurbits.Cucurbits;

public class ItemPickaxe extends net.minecraft.item.ItemPickaxe {

    private String name;

    public ItemPickaxe(ToolMaterial material, String name) {
        super(material);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Cucurbits.creativeTab);

        this.name = name;
    }

    public void registerItemModel() {
        Cucurbits.proxy.registerItemRenderer(this, 0, getRegistryName());
    }

}
