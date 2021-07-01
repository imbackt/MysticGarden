package com.github.imbackt.mystic.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.imbackt.mystic.MysticGarden;

public class Lwjgl3Launcher {
	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("MysticGarden");
		configuration.setWindowedMode(450, 800);
		configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

		new Lwjgl3Application(new MysticGarden(), configuration);
	}
}