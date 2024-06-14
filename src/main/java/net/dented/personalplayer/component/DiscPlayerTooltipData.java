package net.dented.personalplayer.component;

import net.minecraft.item.tooltip.TooltipData;

public record DiscPlayerTooltipData(DiscPlayerContentsComponent contents) implements TooltipData {
    public DiscPlayerTooltipData(DiscPlayerContentsComponent contents) {
        this.contents = contents;
    }

    public DiscPlayerContentsComponent contents() {
        return this.contents;
    }
}

