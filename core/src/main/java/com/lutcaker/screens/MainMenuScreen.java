package com.lutcaker.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lutcaker.game.ScreenManager;

public class MainMenuScreen implements Screen {
    private Stage stage;
    private Skin skin;
    ScreenManager manager;

    public MainMenuScreen(ScreenManager manager) {
        this.manager = manager;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton startButton = new TextButton("Start Game", skin);
        startButton.setSize(200, 50);
        startButton.setPosition(300, 250);

        startButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               MainMenuScreen screen = new MainMenuScreen(manager);
                manager.addScreen(screen);
                manager.setScreen(screen);
           }
        });


        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.setSize(200,50);
        quitButton.setPosition(300,180);

        quitButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               Gdx.app.exit();
           }
        });

        stage.addActor(startButton);
        stage.addActor(quitButton);

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
