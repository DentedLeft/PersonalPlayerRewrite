package net.dented.personalplayer.mixin;

import net.dented.personalplayer.item.custom.PersonalDiscPlayerItem;
import net.dented.personalplayer.sound.PersonalDiscPlayerSoundInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

    @Inject(method= "insertItem", at = @At("HEAD"))
    private void insertDiscPlayerTest(ItemStack stack, int startIndex, int endIndex, boolean fromLast, CallbackInfoReturnable<Boolean> cir) {
        ScreenHandler handler = (ScreenHandler) (Object) this;
        if (handler instanceof PlayerScreenHandler) return;
        if (stack.getItem() instanceof PersonalDiscPlayerItem) {
            if (PersonalDiscPlayerItem.hasDisc(stack) && !PersonalDiscPlayerSoundInstance.instance.isDone()) {
                PersonalDiscPlayerSoundInstance.instance.cancel();
            }
        }
    }

}
