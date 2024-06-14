package net.dented.personalplayer.util;

import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.block.jukebox.JukeboxSongs;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.JukeboxPlayableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryPair;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class MusicDiscUtil {

    public static SoundEvent getSoundEvent(ItemStack stack) {
        String string = "null";
        RegistryPair<JukeboxSong> song = null;
        JukeboxPlayableComponent jukeboxPlayableComponent = stack.get(DataComponentTypes.JUKEBOX_PLAYABLE);
        if (jukeboxPlayableComponent != null) {
            song = jukeboxPlayableComponent.song();
            if (song.key().getValue().toTranslationKey().contains("minecraft.")) {
                string = song.key().getValue().toTranslationKey().replace("minecraft.", "music_disc.");
                return SoundEvent.of(Identifier.of(string));
            }
            return SoundEvent.of(Identifier.of(song.key().getValue().toString()));
        }
        return SoundEvent.of(Identifier.of(""));
    }
}
