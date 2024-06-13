package net.dented.personalplayer.sound;

import net.dented.personalplayer.item.ModItems;
import net.dented.personalplayer.item.custom.PersonalDiscPlayerItem;
import net.dented.personalplayer.mixin.ScreenHandlerMixin;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class PersonalDiscPlayerSoundInstance extends MovingSoundInstance {
    private final PlayerEntity playerEntity;
    private final ItemStack stack;
    public static PersonalDiscPlayerSoundInstance instance;
    private final SoundEvent soundEvent;

    public PersonalDiscPlayerSoundInstance(SoundEvent soundEvent, PlayerEntity playerEntity, ItemStack stack, boolean repeat, float volume) {
        super(soundEvent, SoundCategory.RECORDS, Random.create());
        this.playerEntity = playerEntity;
        this.stack = stack;
        this.soundEvent = soundEvent;
        this.repeat = repeat;
        this.volume = volume;
        this.x = this.playerEntity.getX();
        this.y = this.playerEntity.getY();
        this.z = this.playerEntity.getZ();
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public SoundEvent getPersonalDiscPlayerSound() {
        return this.soundEvent;
    }

    @Override
    public void tick() {
        if (!this.playerEntity.getInventory().contains(this.stack) && MinecraftClient.getInstance().options.dropKey.isPressed()) {
            this.cancel();
        }
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