package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Keno on 4/1/2015.
 */
public class AudioManager {
    static class mMusic {
        static String intro = "01 - X - CAPCOM LOGO.mp3";
    }

    static class mSounds {
        static Sound shootLemons = Gdx.audio.newSound(new FileHandle(soundsDIr + "MMX00.wav"));
    }

    static String musicDir = "core\\assets\\audio\\Music\\";
    static String soundsDIr = "core\\assets\\audio\\SFX\\";
    static Music music;

    static void changeMusic(String newMusic){
        if (music != null) music.dispose();
        music = Gdx.audio.newMusic(new FileHandle(musicDir + newMusic));
        music.play();

    }

}
