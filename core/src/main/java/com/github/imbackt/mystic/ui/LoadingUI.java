package com.github.imbackt.mystic.ui;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.StringBuilder;
import com.github.imbackt.mystic.MysticGarden;

public class LoadingUI extends Table {
    private final String loadingString;
    private final ProgressBar progressBar;
    private final TextButton pressAnyKey;
    private final TextButton textButton;

    public LoadingUI(final MysticGarden context) {
        super(context.getSkin());
        setFillParent(true);

        final I18NBundle i18NBundle = context.getI18NBundle();
        loadingString = i18NBundle.format("loading");

        progressBar = new ProgressBar(0, 1, 0.01f, false, getSkin(), "default");
        //progressBar.setAnimateDuration(1);

        textButton = new TextButton(loadingString, getSkin(), "normal");
        textButton.getLabel().setWrap(true);

        pressAnyKey = new TextButton(i18NBundle.format("pressAnyKey"), getSkin(), "normal");
        pressAnyKey.getLabel().setWrap(true);
        pressAnyKey.setVisible(false);

        add(pressAnyKey).expand().fill().center().row();
        add(textButton).expandX().fillX().bottom().row();
        add(progressBar).expandX().fillX().bottom().pad(20, 25, 20, 25);
    }

    public void setProgress(final float progress) {
        progressBar.setValue(progress);

        final StringBuilder stringBuilder = textButton.getLabel().getText();
        stringBuilder.setLength(0);
        stringBuilder.append(loadingString);
        stringBuilder.append(" (");
        stringBuilder.append((int)(progress * 100));
        stringBuilder.append("%)");
        textButton.getLabel().invalidateHierarchy();

        if (progress >= 1 && !pressAnyKey.isVisible()) {
            pressAnyKey.setVisible(true);
            pressAnyKey.setColor(1, 1, 1, 0);
            pressAnyKey.addAction(Actions.forever(Actions.sequence(Actions.alpha(1, 1), Actions.alpha(0, 1))));
        }
    }
}
