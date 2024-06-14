package net.dented.personalplayer.sound;

import net.dented.personalplayer.PersonalPlayerMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {

    //public static final SoundEvent TEST_SOUND = registerSoundEvent("music_disc.test");



    // Helper Methods
    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(PersonalPlayerMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        PersonalPlayerMod.LOGGER.info("Registering sounds for " + PersonalPlayerMod.MOD_ID);
    }

}
