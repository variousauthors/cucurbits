package variousauthors.cucurbits.item.tool;

import net.minecraft.item.ItemSpade;
import variousauthors.cucurbits.Cucurbits;

public class ItemShovel extends ItemSpade {

    private String name;

    public ItemShovel(ToolMaterial material, String name) {
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
