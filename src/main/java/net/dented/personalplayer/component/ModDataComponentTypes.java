package net.dented.personalplayer.component;

import com.mojang.serialization.Codec;
import net.dented.personalplayer.PersonalPlayerMod;
import net.minecraft.component.DataComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DataComponentType<DiscPlayerContentsComponent> DISC_PLAYER_CONTENTS =
            DataComponentType.<DiscPlayerContentsComponent>builder().codec(DiscPlayerContentsComponent.CODEC).packetCodec(DiscPlayerContentsComponent.PACKET_CODEC).build();

    public static void registerModDataComponentTypes() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(PersonalPlayerMod.MOD_ID, "disc_player_contents"), DISC_PLAYER_CONTENTS);
    }

}
