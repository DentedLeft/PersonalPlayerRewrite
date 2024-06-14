package net.dented.personalplayer.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class DiscPlayerContentsComponent {
    public static final DiscPlayerContentsComponent DEFAULT = new DiscPlayerContentsComponent(List.of());
    public static final Codec<DiscPlayerContentsComponent> CODEC;
    public static final PacketCodec<RegistryByteBuf, DiscPlayerContentsComponent> PACKET_CODEC;
    private static final Fraction NESTED_BUNDLE_OCCUPANCY;
    private static final int ADD_TO_NEW_SLOT = -1;
    final List<ItemStack> discs;
    final Fraction occupancy;

    DiscPlayerContentsComponent(List<ItemStack> stacks, Fraction occupancy) {
        this.discs = stacks;
        this.occupancy = occupancy;
    }

    public DiscPlayerContentsComponent(List<ItemStack> stacks) {
        this(stacks, calculateOccupancy(stacks));
    }

    private static Fraction calculateOccupancy(List<ItemStack> stacks) {
        Fraction fraction = Fraction.ZERO;

        ItemStack itemStack;
        for(Iterator var2 = stacks.iterator(); var2.hasNext(); fraction = fraction.add(getOccupancy(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)))) {
            itemStack = (ItemStack)var2.next();
        }

        return fraction;
    }

    static Fraction getOccupancy(ItemStack stack) {
        DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent)stack.get(ModDataComponentTypes.DISC_PLAYER_CONTENTS);
        if (discPlayerContentsComponent != null) {
            return NESTED_BUNDLE_OCCUPANCY.add(discPlayerContentsComponent.getOccupancy());
        } else {
            List<BeehiveBlockEntity.BeeData> list = (List)stack.getOrDefault(DataComponentTypes.BEES, List.of());
            return !list.isEmpty() ? Fraction.ONE : Fraction.getFraction(1, stack.getMaxCount());
        }
    }

    public ItemStack get(int index) {
        return (ItemStack)this.discs.get(index);
    }

    public Stream<ItemStack> stream() {
        return this.discs.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> iterate() {
        return this.discs;
    }

    public Iterable<ItemStack> iterateCopy() {
        return Lists.transform(this.discs, ItemStack::copy);
    }

    public int size() {
        return this.discs.size();
    }

    public Fraction getOccupancy() {
        return this.occupancy;
    }

    public boolean isEmpty() {
        return this.discs.isEmpty();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof DiscPlayerContentsComponent)) {
            return false;
        } else {
            DiscPlayerContentsComponent discPlayerContentsComponent = (DiscPlayerContentsComponent)o;
            return this.occupancy.equals(discPlayerContentsComponent.occupancy) && ItemStack.stacksEqual(this.discs, discPlayerContentsComponent.discs);
        }
    }

    public int hashCode() {
        return ItemStack.listHashCode(this.discs);
    }

    public String toString() {
        return "BundleContents" + String.valueOf(this.discs);
    }

    static {
        CODEC = ItemStack.CODEC.listOf().xmap(DiscPlayerContentsComponent::new, (component) -> {
            return component.discs;
        });
        PACKET_CODEC = ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(DiscPlayerContentsComponent::new, (component) -> {
            return component.discs;
        });
        NESTED_BUNDLE_OCCUPANCY = Fraction.getFraction(1, 16);
    }

    public static class Builder {
        private final List<ItemStack> stacks;
        private Fraction occupancy;

        public Builder(DiscPlayerContentsComponent base) {
            this.stacks = new ArrayList(base.discs);
            this.occupancy = base.occupancy;
        }

        public DiscPlayerContentsComponent.Builder clear() {
            this.stacks.clear();
            this.occupancy = Fraction.ZERO;
            return this;
        }

        private int addInternal(ItemStack stack) {
            if (!stack.isStackable()) {
                return -1;
            } else {
                for(int i = 0; i < this.stacks.size(); ++i) {
                    if (ItemStack.areItemsAndComponentsEqual((ItemStack)this.stacks.get(i), stack)) {
                        return i;
                    }
                }

                return -1;
            }
        }

        private int getMaxAllowed(ItemStack stack) {
            Fraction fraction = Fraction.ONE.subtract(this.occupancy);
            return Math.max(fraction.divideBy(DiscPlayerContentsComponent.getOccupancy(stack)).intValue(), 0);
        }

        public int add(ItemStack stack) {
            if (!stack.isEmpty() && stack.isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of("c:music_discs"))) && stack.getItem().canBeNested()) {
                int i = Math.min(stack.getCount(), this.getMaxAllowed(stack));
                if (i == 0) {
                    return 0;
                } else {
                    this.occupancy = this.occupancy.add(DiscPlayerContentsComponent.getOccupancy(stack).multiplyBy(Fraction.getFraction(i, 1)));
                    int j = this.addInternal(stack);
                    if (j != -1) {
                        ItemStack itemStack = (ItemStack)this.stacks.remove(j);
                        ItemStack itemStack2 = itemStack.copyWithCount(itemStack.getCount() + i);
                        stack.decrement(i);
                        this.stacks.add(0, itemStack2);
                    } else {
                        this.stacks.add(0, stack.split(i));
                    }

                    return i;
                }
            } else {
                return 0;
            }
        }

        public int add(Slot slot, PlayerEntity player) {
            ItemStack itemStack = slot.getStack();
            int i = this.getMaxAllowed(itemStack);
            return this.add(slot.takeStackRange(itemStack.getCount(), i, player));
        }

        @Nullable
        public ItemStack removeFirst() {
            if (this.stacks.isEmpty()) {
                return null;
            } else {
                ItemStack itemStack = ((ItemStack)this.stacks.remove(0)).copy();
                this.occupancy = this.occupancy.subtract(DiscPlayerContentsComponent.getOccupancy(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
                return itemStack;
            }
        }

        public Fraction getOccupancy() {
            return this.occupancy;
        }

        public DiscPlayerContentsComponent build() {
            return new DiscPlayerContentsComponent(List.copyOf(this.stacks), this.occupancy);
        }
    }
}