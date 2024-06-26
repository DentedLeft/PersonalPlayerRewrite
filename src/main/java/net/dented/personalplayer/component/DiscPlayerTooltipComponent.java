package net.dented.personalplayer.component;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.Fraction;

public class DiscPlayerTooltipComponent implements TooltipComponent {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.of("container/bundle/background");
    private static final int field_32381 = 4;
    private static final int field_32382 = 1;
    private static final int WIDTH_PER_COLUMN = 18;
    private static final int HEIGHT_PER_ROW = 20;
    private final DiscPlayerContentsComponent field_49537;

    public DiscPlayerTooltipComponent(DiscPlayerContentsComponent data) {
        this.field_49537 = data;
    }

    public int getHeight() {
        return this.getRowsHeight() + 4;
    }

    public int getWidth(TextRenderer textRenderer) {
        return this.getColumnsWidth();
    }

    private int getColumnsWidth() {
        return this.getColumns() * 18 + 2;
    }

    private int getRowsHeight() {
        return this.getRows() * 20 + 2;
    }

    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int i = this.getColumns();
        int j = this.getRows();
        context.drawGuiTexture(BACKGROUND_TEXTURE, x, y, this.getColumnsWidth(), this.getRowsHeight());
        boolean bl = this.field_49537.getOccupancy().compareTo(Fraction.ONE) >= 0;
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int m = 0; m < i; ++m) {
                int n = x + m * 18 + 1;
                int o = y + l * 20 + 1;
                this.drawSlot(n, o, k++, bl, context, textRenderer);
            }
        }

    }

    private void drawSlot(int x, int y, int index, boolean shouldBlock, DrawContext context, TextRenderer textRenderer) {
        if (index >= this.field_49537.size()) {
            this.draw(context, x, y, shouldBlock ? DiscPlayerTooltipComponent.SlotSprite.BLOCKED_SLOT : DiscPlayerTooltipComponent.SlotSprite.SLOT);
        } else {
            ItemStack itemStack = this.field_49537.get(index);
            this.draw(context, x, y, DiscPlayerTooltipComponent.SlotSprite.SLOT);
            context.drawItem(itemStack, x + 1, y + 1, index);
            context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1);
            if (index == 0) {
                HandledScreen.drawSlotHighlight(context, x + 1, y + 1, 0);
            }

        }
    }

    private void draw(DrawContext context, int x, int y, DiscPlayerTooltipComponent.SlotSprite sprite) {
        context.drawGuiTexture(sprite.texture, x, y, 0, sprite.width, sprite.height);
    }

    private int getColumns() {
        return 1 /*Math.min(2, (int)Math.ceil(Math.sqrt((double)this.field_49537.size() + 1.0)))*/;
    }

    private int getRows() {
        return 1/*(int)Math.ceil(((double)this.field_49537.size() + 1.0) / (double)this.getColumns())*/;
    }

    @Environment(EnvType.CLIENT)
    private static enum SlotSprite {
        BLOCKED_SLOT(Identifier.of("container/bundle/blocked_slot"), 18, 20),
        SLOT(Identifier.of("container/bundle/slot"), 18, 20);

        public final Identifier texture;
        public final int width;
        public final int height;

        private SlotSprite(final Identifier texture, final int width, final int height) {
            this.texture = texture;
            this.width = width;
            this.height = height;
        }
    }
}