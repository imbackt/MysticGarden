package com.github.imbackt.mystic.view;

public enum AnimationType {
    HERO_MOVE_UP("characters_and_effects/character_and_effect.atlas", "hero", 0.05f, 0),
    HERO_MOVE_DOWN("characters_and_effects/character_and_effect.atlas", "hero", 0.05f, 2),
    HERO_MOVE_LEFT("characters_and_effects/character_and_effect.atlas", "hero", 0.05f, 1),
    HERO_MOVE_RIGHT("characters_and_effects/character_and_effect.atlas", "hero", 0.05f, 3);

    private String atlasPath;
    private String atlasKey;
    private float frameTime;
    private int rowIndex;

    AnimationType(String atlasPath, String atlasKey, float frameTime, int rowIndex) {
        this.atlasPath = atlasPath;
        this.atlasKey = atlasKey;
        this.frameTime = frameTime;
        this.rowIndex = rowIndex;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public String getAtlasKey() {
        return atlasKey;
    }

    public float getFrameTime() {
        return frameTime;
    }

    public int getRowIndex() {
        return rowIndex;
    }
}
