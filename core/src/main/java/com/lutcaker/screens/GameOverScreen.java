package com.lutcaker.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lutcaker.entities.Player;
import com.lutcaker.game.Main;
import com.lutcaker.game.ScreenManager;

public class GameOverScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Array<Button> buttons;
    public Main game;
    Table table;
    Label title;
    boolean isActive;
    Label resultsLabel;
    private long playerScore;
    public GameOverScreen(Main game, long playerScore) {
        this.game = game;
        this.playerScore = playerScore;
    }

    @Override
    public void show() {
        buttons = new Array<>();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.center();
        isActive = true;

        try {
            skin = new Skin(Gdx.files.internal("uiskin.json"));
        } catch (Exception e) {
            Gdx.app.error("Skin", "Failed to load skin!", e);
        }
        this.title = new Label("Game Over", skin);
        title.setFontScale(2f);

        table.add(title).padBottom(40);
        table.row();

        resultsLabel = new Label("Score: " + playerScore, skin);
        table.add(resultsLabel).padBottom(20);
        table.row();

        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        table.add(mainMenuButton).width(200).height(50).pad(10);
        table.row();

        mainMenuButton.setColor(0,1,1,1);

        TextButton quitButton = new TextButton("Quit", skin);
        table.add(quitButton).width(200).height(50).pad(10);

        quitButton.setColor(1,0,0,1);
        buttons.add(quitButton);


        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreenWithHistory(new MainMenuScreen(game));
            }
        });

        stage.addActor(table);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(v);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i, i1, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        isActive = false;
    }

    @Override
    public void dispose() {

    }
}
