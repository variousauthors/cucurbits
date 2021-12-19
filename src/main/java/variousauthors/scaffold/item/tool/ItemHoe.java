package variousauthors.scaffold.item.tool;

import variousauthors.scaffold.Scaffold;

public class ItemHoe extends net.minecraft.item.ItemHoe {

    private String name;

    public ItemHoe(ToolMaterial material, String name) {
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
