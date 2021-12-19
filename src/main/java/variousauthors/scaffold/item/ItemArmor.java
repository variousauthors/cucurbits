package variousauthors.scaffold.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import variousauthors.scaffold.CanRegisterItemModel;

public class ItemArmor extends net.minecraft.item.ItemArmor implements CanRegisterItemModel {
    private String name;

    public ItemArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name) {
        super(material, 0, slot);
        setRegistryName(name);
        setUnlocalizedName(name);
        this.name = name;
    }
}
