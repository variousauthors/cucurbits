package variousauthors.cucurbits.item.tool;

import variousauthors.cucurbits.Cucurbits;

public class ItemSword extends net.minecraft.item.ItemSword {

    private String name;

    public ItemSword(ToolMaterial material, String name) {
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
