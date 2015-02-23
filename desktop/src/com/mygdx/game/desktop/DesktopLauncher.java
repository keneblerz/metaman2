package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.game.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {

//        REGENERATE ATLASES
//        TexturePacker.Settings settings = new TexturePacker.Settings();
//        String dir = "core\\assets\\megaman\\";
//        TexturePacker.process(settings, dir + "mmjump", dir + "mmjump\\output", "megaman");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 281;
		config.height = 225;
		config.title = "the great bahamian side scroller";
		new LwjglApplication(new Game(), config);
	}
}
