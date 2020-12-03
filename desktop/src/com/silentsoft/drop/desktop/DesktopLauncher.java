package com.silentsoft.drop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.silentsoft.drop.Drop;
import com.silentsoft.drop.DropOriginal;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 800;
		config.height = 480;

		new LwjglApplication(new Drop(), config);

	}
}
