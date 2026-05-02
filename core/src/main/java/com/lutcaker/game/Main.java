package com.lutcaker.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.lutcaker.screens.MainMenuScreen;

import java.util.Stack;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
   private ScreenManager manager;
   public Music music;
   public static Stack<Screen> previousScreens;

    @Override
    public void create() {
        previousScreens = new Stack<>();
        MainMenuScreen mainMenu = new MainMenuScreen(this);
        setScreenWithHistory(mainMenu);
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
