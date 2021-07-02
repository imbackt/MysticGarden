package com.github.imbackt.mystic.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameUI extends Table {
    public GameUI(Skin skin) {
        super(skin);
        setFillParent(true);

        add(new TextButton("Play", skin, "huge"));
    }
}
