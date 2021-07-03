package com.github.imbackt.mystic.view;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.imbackt.mystic.MysticGarden;

public class GameUI extends Table {
    public GameUI(MysticGarden context) {
        super(context.getSkin());
        setFillParent(true);

        add(new TextButton("Play", getSkin(), "huge"));
        bottom();
    }
}
