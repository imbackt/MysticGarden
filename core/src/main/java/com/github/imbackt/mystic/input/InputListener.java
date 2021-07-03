package com.github.imbackt.mystic.input;

public interface InputListener {
    void keyPressed(final InputManager manager, final GameKeys key);

    void keyUp(final InputManager manager, final GameKeys key);
}
