package net.dented.personalplayer.mixin;

import net.dented.personalplayer.component.DiscPlayerContentsComponent;
import net.dented.personalplayer.component.ModDataComponentTypes;
import net.dented.personalplayer.sound.PersonalDiscPlayerSoundInstance;
import net.dented.personalplayer.util.MusicDiscUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.JukeboxPlayableComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
    public void dropItem0(ItemStack stack, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent) stack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (discPlayerContentsComponent != null && !discPlayerContentsComponent.isEmpty()) {
            ItemStack disc = discPlayerContentsComponent.get(0);
            if (disc.isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of("c:music_discs"))) && PersonalDiscPlayerSoundInstance.instance != null) {
                if (MusicDiscUtil.getSoundEvent(disc).getId().equals(PersonalDiscPlayerSoundInstance.instance.getId())) {
                    PlayerEntity player = (PlayerEntity) (Object) this;
                    if (player.getWorld().isClient()) {
                        PersonalDiscPlayerSoundInstance.instance.cancel();
                        PersonalDiscPlayerSoundInstance.instance = null;
                    }
                }
            }
        }
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
    public void dropItem1(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent) stack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (discPlayerContentsComponent != null && !discPlayerContentsComponent.isEmpty()) {
            ItemStack disc = discPlayerContentsComponent.get(0);
            if (disc.isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of("c:music_discs")))  && PersonalDiscPlayerSoundInstance.instance != null) {
                if (MusicDiscUtil.getSoundEvent(disc).getId().equals(PersonalDiscPlayerSoundInstance.instance.getId())) {
                    PlayerEntity player = (PlayerEntity) (Object) this;
                    if (player.getWorld().isClient()) {
                        PersonalDiscPlayerSoundInstance.instance.cancel();
                        PersonalDiscPlayerSoundInstance.instance = null;
                    }
                }
            }
        }
    }

/*
    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
    public void dropItem0(ItemStack stack, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getWorld().isClient()) {
            if (PersonalDiscPlayerSoundInstance.instance != null && PersonalDiscPlayerItem.hasDisc(stack)) {
                PersonalDiscPlayerSoundInstance.instance.cancel();
                PersonalDiscPlayerSoundInstance.instance = null;
            }
        }
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
    public void dropItem1(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getWorld().isClient()) {
            if (PersonalDiscPlayerSoundInstance.instance != null && PersonalDiscPlayerItem.hasDisc(stack)) {
                PersonalDiscPlayerSoundInstance.instance.cancel();
                PersonalDiscPlayerSoundInstance.instance = null;
            }
        }
    }
*/
}
