package net.dented.personalplayer.item.custom;

import com.mojang.datafixers.kinds.Kind1;
import net.dented.personalplayer.component.DiscPlayerContentsComponent;
import net.dented.personalplayer.component.ModDataComponentTypes;
import net.dented.personalplayer.sound.PersonalDiscPlayerSoundInstance;
import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class PersonalDiscPlayerItem extends Item {

    public PersonalDiscPlayerItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent)stack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
            if (discPlayerContentsComponent == null) {
                return false;
            } else {
                ItemStack itemStack = slot.getStack();
                DiscPlayerContentsComponent.Builder builder = new DiscPlayerContentsComponent.Builder(discPlayerContentsComponent);
                if (itemStack.isEmpty()) {
                    this.playRemoveOneSound(player);
                    ItemStack itemStack2 = builder.removeFirst();
                    if (itemStack2 != null) {
                        ItemStack itemStack3 = slot.insertStack(itemStack2);
                        builder.add(itemStack3);
                    }
                } else if (itemStack.getItem().canBeNested() && itemStack.getItem() instanceof MusicDiscItem discItem) {
                    int i = builder.add(slot, player);
                    if (i > 0) {
                    /*    if (player.getWorld().isClient()) {
                            if (PersonalDiscPlayerSoundInstance.instance != null) {
                                PersonalDiscPlayerSoundInstance.instance.cancel();
                                PersonalDiscPlayerSoundInstance.instance = null;
                            }
                            stack.set(ModDataComponentTypes.DISC_PLAYER_CONTENTS, builder.build());
                            PersonalDiscPlayerSoundInstance.instance = new PersonalDiscPlayerSoundInstance(discItem.getSound(), player, stack, true, 0.8f);
                            PersonalDiscPlayerSoundInstance.instance.play();
                        } */
                        this.playInsertSound(player);
                    }
                }

                stack.set(ModDataComponentTypes.DISC_PLAYER_CONTENTS, builder.build());
                return true;
            }
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent)stack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
            if (discPlayerContentsComponent == null) {
                return false;
            } else {
                DiscPlayerContentsComponent.Builder builder = new DiscPlayerContentsComponent.Builder(discPlayerContentsComponent);
                if (otherStack.isEmpty()) {
                    ItemStack itemStack = builder.removeFirst();
                    if (itemStack != null) {
                        this.playRemoveOneSound(player);
                        cursorStackReference.set(itemStack);
                    }
                } else {
                    int i = builder.add(otherStack);
                    stack.set(ModDataComponentTypes.DISC_PLAYER_CONTENTS, builder.build());
                    if (i > 0 && otherStack.getItem() instanceof MusicDiscItem discItem) {
                       /* if (player.getWorld().isClient()) {
                            if (PersonalDiscPlayerSoundInstance.instance != null) {
                                PersonalDiscPlayerSoundInstance.instance.cancel();
                                PersonalDiscPlayerSoundInstance.instance = null;
                            }

                            PersonalDiscPlayerSoundInstance.instance = new PersonalDiscPlayerSoundInstance(discItem.getSound(), player, stack, true, 0.8f);
                            PersonalDiscPlayerSoundInstance.instance.play();
                        }*/
                        this.playInsertSound(player);
                    }
                }

                stack.set(ModDataComponentTypes.DISC_PLAYER_CONTENTS, builder.build());
                return true;
            }
        } else {
            return false;
        }
    }


    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent) entity.getStack().get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (discPlayerContentsComponent != null) {
            entity.getStack().set(ModDataComponentTypes.DISC_PLAYER_CONTENTS, DiscPlayerContentsComponent.DEFAULT);
            ItemUsage.spawnItemContents(entity, discPlayerContentsComponent.iterateCopy());
        }
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent) user.getMainHandStack().get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (discPlayerContentsComponent != null) {
            ItemStack stack = discPlayerContentsComponent.get(0);
            if (stack != null && stack.getItem() instanceof MusicDiscItem discItem) {
                if (user.getWorld().isClient()) {
                    if (PersonalDiscPlayerSoundInstance.instance != null) {
                        PersonalDiscPlayerSoundInstance.instance.cancel();
                        PersonalDiscPlayerSoundInstance.instance = null;
                    }

                    PersonalDiscPlayerSoundInstance.instance = new PersonalDiscPlayerSoundInstance(discItem.getSound(), user, user.getMainHandStack(), true, 0.8f);
                    PersonalDiscPlayerSoundInstance.instance.play();
                }
            }
        }


        return super.use(world, user, hand);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return false;
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    public static boolean hasDisc (ItemStack stack) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent) stack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (discPlayerContentsComponent != null && discPlayerContentsComponent.stream().findAny().isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent) stack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (!discPlayerContentsComponent.isEmpty()) {
            ItemStack itemStack = discPlayerContentsComponent.stream().findFirst().get();
            String translationKey = itemStack.getTranslationKey();
            if (translationKey != null) {
                tooltip.add(Text.translatable(translationKey + ".desc").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            }
        } else {
            tooltip.add(Text.translatable("item.personalplayer.personal_disc_player.tooltip").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            super.appendTooltip(stack, context, tooltip, type);
        }

    }

}
