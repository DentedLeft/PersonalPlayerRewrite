package net.dented.personalplayer.component;

import net.minecraft.client.item.TooltipData;
import net.minecraft.component.type.BundleContentsComponent;

public record DiscPlayerTooltipData(DiscPlayerContentsComponent contents) implements TooltipData {
    public DiscPlayerTooltipData(DiscPlayerContentsComponent contents) {
        this.contents = contents;
    }

    public DiscPlayerContentsComponent contents() {
        return this.contents;
    }
}

