package com.github.imbackt.mystic;

import com.badlogic.gdx.Game;

public class MysticGarden extends Game {
	@Override
	public void create() {
		setScreen(new FirstScreen());
	}
}