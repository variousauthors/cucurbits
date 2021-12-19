package variousauthors.scaffold.proxy;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import variousauthors.scaffold.block.pedestal.TESRPedestal;
import variousauthors.scaffold.block.pedestal.TileEntityPedestal;

public class CommonProxy {
    public void registerItemRenderer(Item item, int meta, ResourceLocation location) {
    }

    public String localize(String unlocalized, Object... args) {
        return I18n.translateToLocalFormatted(unlocalized, args);
    }

    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class, new TESRPedestal());
    }
}
