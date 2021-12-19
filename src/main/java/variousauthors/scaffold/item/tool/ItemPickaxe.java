package variousauthors.scaffold.item.tool;

import variousauthors.scaffold.Scaffold;

public class ItemPickaxe extends net.minecraft.item.ItemPickaxe {

    private String name;

    public ItemPickaxe(ToolMaterial material, String name) {
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
