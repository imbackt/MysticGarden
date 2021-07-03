package com.github.imbackt.mystic.map;

public enum MapType {
    MAP_1("map/map.tmx"),
    MAP_2("map/map2.tmx");

    private final String filePath;

    MapType(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
