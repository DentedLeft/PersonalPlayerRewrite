package net.dented.personalplayer.component;

import com.mojang.serialization.Codec;
import net.dented.personalplayer.PersonalPlayerMod;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<DiscPlayerContentsComponent> DISC_PLAYER_CONTENTS =
            ComponentType.<DiscPlayerContentsComponent>builder().codec(DiscPlayerContentsComponent.CODEC).packetCodec(DiscPlayerContentsComponent.PACKET_CODEC).build();

    public static void registerModDataComponentTypes() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(PersonalPlayerMod.MOD_ID, "disc_player_contents"), DISC_PLAYER_CONTENTS);
    }

}
