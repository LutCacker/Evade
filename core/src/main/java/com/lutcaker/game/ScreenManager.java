package com.lutcaker.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Array;

public class ScreenManager {
    private ApplicationAdapter app;
    private Array<Screen> screens;
    private Screen currentScreen;


    public ScreenManager(ApplicationAdapter app) {
        this.app = app;
        this.screens = new Array<>();
    }


    public void addScreen(Screen screen) {
        screens.add(screen);
    }

    public void removeScreen(Screen screen) {
        screens.removeValue(screen, true);
    }

    public void setScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen.hide();
        }
        currentScreen = screen;
        currentScreen.show();
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

