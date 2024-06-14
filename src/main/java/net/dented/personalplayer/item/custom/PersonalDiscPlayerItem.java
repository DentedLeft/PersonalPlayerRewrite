package net.dented.personalplayer.item.custom;

import net.dented.personalplayer.component.DiscPlayerContentsComponent;
import net.dented.personalplayer.component.DiscPlayerTooltipData;
import net.dented.personalplayer.component.ModDataComponentTypes;
import net.dented.personalplayer.sound.PersonalDiscPlayerSoundInstance;
import net.dented.personalplayer.util.MusicDiscUtil;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.JukeboxPlayableComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.WardenAngerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryPair;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
                } else if (itemStack.getItem().canBeNested() && itemStack.isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of("c:music_discs")))) {
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
                    if (i > 0 && otherStack.isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of("c:music_discs")))) {
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
        if (discPlayerContentsComponent != null && !discPlayerContentsComponent.isEmpty()) {
            ItemStack stack = discPlayerContentsComponent.get(0);
            if (stack != null && stack.isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of("c:music_discs")))) {
                if (user.getWorld().isClient()) {
                    if (PersonalDiscPlayerSoundInstance.instance == null) {
                        PersonalDiscPlayerSoundInstance.instance = new PersonalDiscPlayerSoundInstance(MusicDiscUtil.getSoundEvent(stack), user, user.getMainHandStack(), true, 0.8f);
                        PersonalDiscPlayerSoundInstance.instance.play();
                    } else {
                        PersonalDiscPlayerSoundInstance.instance.cancel();
                        PersonalDiscPlayerSoundInstance.instance = null;
                    }
                }
            }
        }

        return super.use(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!PersonalDiscPlayerItem.hasDisc(stack) && PersonalDiscPlayerSoundInstance.instance != null && ItemStack.areEqual(stack, PersonalDiscPlayerSoundInstance.instance.getStack())) {
            PersonalDiscPlayerSoundInstance.instance.cancel();
            PersonalDiscPlayerSoundInstance.instance = null;
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    public static boolean hasDisc (ItemStack stack) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent) stack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (discPlayerContentsComponent != null && discPlayerContentsComponent.stream().findFirst().isPresent()) {
            return true;
        }
        return false;

    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return !stack.contains(DataComponentTypes.HIDE_TOOLTIP) && !stack.contains(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP) ? Optional.ofNullable((DiscPlayerContentsComponent)stack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS)).map(DiscPlayerTooltipData::new) : Optional.empty();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent) stack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (discPlayerContentsComponent != null && !discPlayerContentsComponent.isEmpty()) {
            ItemStack itemStack = discPlayerContentsComponent.stream().findFirst().get();
            String translationKey = itemStack.getTranslationKey();
            if (translationKey != null) {
                tooltip.add(Text.translatable(translationKey + ".desc").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            }
        } else {
            tooltip.add(Text.translatable("item.personalplayer.personal_disc_player.tooltip").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }

    }

}
