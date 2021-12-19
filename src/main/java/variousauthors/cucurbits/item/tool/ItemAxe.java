package variousauthors.cucurbits.item.tool;

import variousauthors.cucurbits.Cucurbits;

public class ItemAxe extends net.minecraft.item.ItemAxe {

    private String name;

    public ItemAxe(ToolMaterial material, String name) {
        super(material, 8f, -3.1f);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Cucurbits.creativeTab);
        this.name = name;
    }

    public void registerItemModel() {
        Cucurbits.proxy.registerItemRenderer(this, 0, getRegistryName());
    }
}
