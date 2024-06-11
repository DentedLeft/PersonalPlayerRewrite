package net.dented.personalplayer.sound;

import net.dented.personalplayer.item.custom.PersonalDiscPlayerItem;
import net.dented.personalplayer.mixin.ScreenHandlerMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class PersonalDiscPlayerSoundInstance extends MovingSoundInstance {
    private final PlayerEntity playerEntity;
    private final ItemStack stack;
    public static PersonalDiscPlayerSoundInstance instance;

    public PersonalDiscPlayerSoundInstance(SoundEvent soundEvent, PlayerEntity playerEntity, ItemStack stack, boolean repeat, float volume) {
        super(soundEvent, SoundCategory.RECORDS, Random.create());
        this.playerEntity = playerEntity;
        this.stack = stack;
        this.repeat = repeat;
        this.volume = volume;
        this.x = this.playerEntity.getX();
        this.y = this.playerEntity.getY();
        this.z = this.playerEntity.getZ();
    }

    @Override
    public void tick() {
        if (this.playerEntity.isDead()) {
            this.cancel();
        } else {
            this.x = this.playerEntity.getX();
            this.y = this.playerEntity.getY();
            this.z = this.playerEntity.getZ();
        }
    }

    public void cancel() {
        this.setDone();
    }

    public void play() {
        MinecraftClient.getInstance().getSoundManager().play(this);
    }

}