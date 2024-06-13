package net.dented.personalplayer.mixin;

import net.dented.personalplayer.item.custom.PersonalDiscPlayerItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
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
        ItemStack itemStack = slot.getStack();
        if (itemStack.getItem() instanceof PersonalDiscPlayerItem) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onClicked", at = @At("HEAD"), cancellable = true)
    private void updatedOnClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        if (otherStack.getItem() instanceof PersonalDiscPlayerItem)
            cir.setReturnValue(false);
    }




}
