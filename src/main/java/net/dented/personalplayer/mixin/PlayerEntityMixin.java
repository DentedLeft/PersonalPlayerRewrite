package net.dented.personalplayer.mixin;

import net.dented.personalplayer.item.custom.PersonalDiscPlayerItem;
import net.dented.personalplayer.sound.PersonalDiscPlayerSoundInstance;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
    public void dropItem0(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (stack.getItem() instanceof PersonalDiscPlayerItem && PersonalDiscPlayerItem.hasDisc(stack)) {
            if (player.getWorld().isClient()) {
                if (PersonalDiscPlayerSoundInstance.instance != null) {
                    PersonalDiscPlayerSoundInstance.instance.cancel();
                    PersonalDiscPlayerSoundInstance.instance = null;
                }
            }
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath0(DamageSource damageSource, CallbackInfo ci) {
    }
}