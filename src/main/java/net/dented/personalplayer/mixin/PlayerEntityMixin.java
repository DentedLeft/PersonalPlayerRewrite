package net.dented.personalplayer.mixin;

import net.dented.personalplayer.component.DiscPlayerContentsComponent;
import net.dented.personalplayer.component.ModDataComponentTypes;
import net.dented.personalplayer.sound.PersonalDiscPlayerSoundInstance;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
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
            if (disc.getItem() instanceof MusicDiscItem discItem && PersonalDiscPlayerSoundInstance.instance != null) {
                if (discItem.getSound().equals(PersonalDiscPlayerSoundInstance.instance.getPersonalDiscPlayerSound())) {
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
            if (disc.getItem() instanceof MusicDiscItem discItem && PersonalDiscPlayerSoundInstance.instance != null) {
                if (discItem.getSound().equals(PersonalDiscPlayerSoundInstance.instance.getPersonalDiscPlayerSound())) {
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
