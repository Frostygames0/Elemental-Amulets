package frostygames0.elementalamulets.client.gui;

import net.minecraft.resources.ResourceLocation;

public record ElementalExtractorScreenWidgets(WidgetDefinition elementStorageBar, WidgetDefinition litProgressSprite,
                                              WidgetDefinition extractionProgressSprite) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private WidgetDefinition elementStorageBar;
        private WidgetDefinition litProgressSprite;
        private WidgetDefinition extractionProgressSprite;

        public Builder addElementStorageBar(int x, int y, int width, int height, boolean horizontal, ResourceLocation sprite) {
            this.elementStorageBar = new WidgetDefinition(x, y, width, height, horizontal, sprite);
            return this;
        }

        public Builder addLitProgressSprite(int x, int y, int width, int height, ResourceLocation sprite) {
            this.litProgressSprite = new WidgetDefinition(x, y, width, height, false, sprite);
            return this;
        }

        public Builder addExtractionProgressSprite(int x, int y, int width, int height, ResourceLocation sprite) {
            this.extractionProgressSprite = new WidgetDefinition(x, y, width, height, false, sprite);
            return this;
        }

        public ElementalExtractorScreenWidgets build() {
            return new ElementalExtractorScreenWidgets(this.elementStorageBar, this.litProgressSprite, this.extractionProgressSprite);
        }
    }

    public record WidgetDefinition(int x, int y, int width, int height, boolean horizontal, ResourceLocation sprite) {
    }
}
