package net.dented.personalplayer.util;

import net.dented.personalplayer.PersonalPlayerMod;
import net.dented.personalplayer.item.ModItems;
import net.dented.personalplayer.item.custom.PersonalDiscPlayerItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {

    public static void registerModModelPredicates() {
        ModelPredicateProviderRegistry.register(ModItems.PERSONAL_DISC_PLAYER, Identifier.of(PersonalPlayerMod.MOD_ID, "disc_loaded"),
                (stack, world, entity, seed) -> PersonalDiscPlayerItem.hasDisc(stack) ? 1 : 0);

        PersonalPlayerMod.LOGGER.info("Registering model predicates for " + PersonalPlayerMod.MOD_ID);
    }

}
