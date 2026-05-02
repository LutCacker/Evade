package com.lutcaker.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Array;

public class ScreenManager {
    private Game game;
    private Array<Screen> screens;
    private Screen currentScreen;


    public ScreenManager(Game game) {
        this.game = game;
        this.screens = new Array<>();
    }


    public void addScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen = screen;
        }

        game.setScreen(currentScreen);
    }

    public void removeScreen(Screen screen) {
        screens.removeValue(screen, true);
    }

    public void setScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen.hide();
        }
        currentScreen = screen;
        game.setScreen(currentScreen);
    }

    public void render(float deltaTime) {
        if (currentScreen != null) {
            currentScreen.render(deltaTime);
        }
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }
}

