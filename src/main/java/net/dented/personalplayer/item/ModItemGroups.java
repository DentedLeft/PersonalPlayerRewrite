package net.dented.personalplayer.item;

import net.dented.personalplayer.PersonalPlayerMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class ModItemGroups {

    // Vanilla Item Group Entries
    public static void addToItemGroups(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(itemGroup -> itemGroup.addBefore(Items.MUSIC_DISC_13, ModItems.PERSONAL_DISC_PLAYER));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(itemGroup -> itemGroup.addAfter(ModItems.PERSONAL_DISC_PLAYER, ModItems.TEST_DISC));

        PersonalPlayerMod.LOGGER.info("Adding " + PersonalPlayerMod.MOD_ID + " items to vanilla item groups");
    }

}


