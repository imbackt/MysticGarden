package com.github.imbackt.mystic.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.github.imbackt.mystic.MysticGarden;

public class AudioManager {
    private AudioType currentMusicType;
    private Music currentMusic;
    private final AssetManager assetManager;

    public AudioManager(final MysticGarden context) {
        this.assetManager = context.getAssetManager();
        currentMusic = null;
        currentMusicType = null;
    }

    public void playAudio(final AudioType type) {
        if (type.isMusic()) {
            if (currentMusicType == type) {
                return;
            } else if (currentMusic != null) {
                currentMusic.stop();
            }
            currentMusicType = type;
            currentMusic = assetManager.get(type.getFilePath(), Music.class);
            currentMusic.setLooping(true);
            currentMusic.setVolume(type.getVolume());
            currentMusic.play();
        } else {
            assetManager.get(type.getFilePath(), Sound.class).play(type.getVolume());
        }
    }

    public void stopCurrentMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
            currentMusicType = null;
        }
    }
}
