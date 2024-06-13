package net.dented.personalplayer.mixin;

import net.dented.personalplayer.component.DiscPlayerContentsComponent;
import net.dented.personalplayer.component.ModDataComponentTypes;
import net.dented.personalplayer.item.custom.PersonalDiscPlayerItem;
import net.dented.personalplayer.sound.PersonalDiscPlayerSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public class BundleItemMixin {

    @Inject(method = "onStackClicked", at = @At("HEAD"), cancellable = true)
    private void updatedOnStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent) slot.getStack().get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (discPlayerContentsComponent != null && !discPlayerContentsComponent.isEmpty()) {
            ItemStack disc = discPlayerContentsComponent.get(0);
            if (disc.getItem() instanceof MusicDiscItem discItem && PersonalDiscPlayerSoundInstance.instance != null) {
                if (discItem.getSound().equals(PersonalDiscPlayerSoundInstance.instance.getPersonalDiscPlayerSound())) {
                    if (player.getWorld().isClient()) {
                        PersonalDiscPlayerSoundInstance.instance.cancel();
                        PersonalDiscPlayerSoundInstance.instance = null;
                    }
                }
            }
        }
    }

    @Inject(method = "onClicked", at = @At("HEAD"), cancellable = true)
    private void updatedOnClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent) otherStack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (discPlayerContentsComponent != null && !discPlayerContentsComponent.isEmpty()) {
            ItemStack disc = discPlayerContentsComponent.get(0);
            if (disc.getItem() instanceof MusicDiscItem discItem && PersonalDiscPlayerSoundInstance.instance != null) {
                if (discItem.getSound().equals(PersonalDiscPlayerSoundInstance.instance.getPersonalDiscPlayerSound())) {
                    if (player.getWorld().isClient()) {
                        PersonalDiscPlayerSoundInstance.instance.cancel();
                        PersonalDiscPlayerSoundInstance.instance = null;
                    }
                }
            }
        }
    }

}
