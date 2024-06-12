package net.dented.personalplayer.mixin;

import net.dented.personalplayer.component.DiscPlayerTooltipComponent;
import net.dented.personalplayer.component.DiscPlayerTooltipData;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TooltipComponent.class)
public interface TooltipComponentMixin {
    
    @Inject(method = "of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;", at = @At("HEAD"), cancellable = true)
    private static void ofUpdated(TooltipData data, CallbackInfoReturnable<TooltipComponent> cir) {
        if (data instanceof DiscPlayerTooltipData discPlayerTooltipData) {
            cir.setReturnValue(new DiscPlayerTooltipComponent(discPlayerTooltipData.contents()));
        }
    }
    
}
