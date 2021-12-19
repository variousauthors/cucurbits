package variousauthors.scaffold.item.tool;

import net.minecraft.item.ItemSpade;
import variousauthors.scaffold.Scaffold;

public class ItemShovel extends ItemSpade {

    private String name;

    public ItemShovel(ToolMaterial material, String name) {
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
