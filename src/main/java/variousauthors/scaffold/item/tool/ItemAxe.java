package variousauthors.scaffold.item.tool;

import variousauthors.scaffold.Scaffold;

public class ItemAxe extends net.minecraft.item.ItemAxe {

    private String name;

    public ItemAxe(ToolMaterial material, String name) {
        super(material, 8f, -3.1f);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Scaffold.creativeTab);
        this.name = name;
    }

    public void registerItemModel() {
        Scaffold.proxy.registerItemRenderer(this, 0, getRegistryName());
    }
}
