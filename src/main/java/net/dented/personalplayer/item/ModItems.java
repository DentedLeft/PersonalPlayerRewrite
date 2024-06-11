package net.dented.personalplayer.item;

import net.dented.personalplayer.PersonalPlayerMod;
import net.dented.personalplayer.component.DiscPlayerContentsComponent;
import net.dented.personalplayer.component.ModDataComponentTypes;
import net.dented.personalplayer.item.custom.PersonalDiscPlayerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    // Items

    public static final Item PERSONAL_DISC_PLAYER = registerItem("personal_disc_player",
            new PersonalDiscPlayerItem(new Item.Settings().maxCount(1).component(ModDataComponentTypes.DISC_PLAYER_CONTENTS, DiscPlayerContentsComponent.DEFAULT)));


    // Helper Methods
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(PersonalPlayerMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        PersonalPlayerMod.LOGGER.info("Registering items for " + PersonalPlayerMod.MOD_ID);
    }

}


