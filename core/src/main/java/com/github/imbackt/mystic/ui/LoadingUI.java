package com.github.imbackt.mystic.ui;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LoadingUI extends Table {
    private final ProgressBar progressBar;
    private final TextButton pressAnyKey;
    private final TextButton textButton;

    public LoadingUI(Skin skin) {
        super(skin);
        setFillParent(true);

        progressBar = new ProgressBar(0, 1, 0.01f, false, skin, "default");
        progressBar.setAnimateDuration(1);

        textButton = new TextButton("Loading...", skin, "huge");
        textButton.getLabel().setWrap(true);

        pressAnyKey = new TextButton("Press any key", skin, "normal");
        pressAnyKey.getLabel().setWrap(true);
        pressAnyKey.setVisible(false);

        add(pressAnyKey).expand().fill().center().row();
        add(textButton).expandX().fillX().bottom().row();
        add(progressBar).expandX().fillX().bottom().pad(20, 25, 20, 25);
    }

    public void setProgress(final float progress) {
        progressBar.setValue(progress);
        if (progress >= 1 && !pressAnyKey.isVisible()) {
            pressAnyKey.setVisible(true);
            pressAnyKey.setColor(1, 1, 1, 0);
            pressAnyKey.addAction(Actions.forever(Actions.sequence(Actions.alpha(1, 1), Actions.alpha(0, 1))));
        }
    }
}
