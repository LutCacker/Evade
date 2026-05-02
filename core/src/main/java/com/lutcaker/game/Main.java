package com.lutcaker.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.lutcaker.screens.MainMenuScreen;

import java.util.Stack;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
   private ScreenManager manager;
   public Music music;
   public Sound jumpSound;
   public static Stack<Screen> previousScreens;
   public float sfxVolume = 10f;
   public Sound hurtSound;
   public Sound deathSound;

    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("bgmusic.mp3"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("pixel_jump.mp3"));
        hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt_sound.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("roblox_death_sound.mp3"));

        previousScreens = new Stack<>();
        MainMenuScreen mainMenu = new MainMenuScreen(this);
        setScreenWithHistory(mainMenu);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    public void setScreenWithHistory(Screen newScreen) {
        if (getScreen() != null) {
            previousScreens.push(getScreen());
        }
            setScreen(newScreen);

    }

    public void goBack() {
            if (!previousScreens.isEmpty()) {
             Screen previous = previousScreens.pop();
             setScreen(previous);
            }
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
