package variousauthors.scaffold.item.tool;

import variousauthors.scaffold.Scaffold;

public class ItemSword extends net.minecraft.item.ItemSword {

    private String name;

    public ItemSword(ToolMaterial material, String name) {
        super(material);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Scaffold.creativeTab);

        this.name = name;
    }

    public void registerItemModel() {
        Scaffold.proxy.registerItemRenderer(this, 0, getRegistryName());
    }

}
