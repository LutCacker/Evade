package com.lutcaker.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lutcaker.game.Ground;
import com.lutcaker.game.Main;


public class SettingsScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Array<Button> buttons;
    public Main game;
    Table table;
    Label title;
    boolean isActive;

   public SettingsScreen(Main game) {
       this.game = game;
       table = new Table();
       table.setFillParent(true);
       table.center();
   }


    @Override
    public void show() {

        buttons = new Array<>();
        isActive = true;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        try {
            skin = new Skin(Gdx.files.internal("uiskin.json"));
        } catch (Exception e) {
            Gdx.app.error("Skin", "Failed to load skin!: ", e);
        }


        this.title = new Label("Settings", skin);
        title.setFontScale(2f);

        table.add(title).padBottom(40);
        table.row();


        Label vols = new Label("Volume", skin);
        table.add(vols).padBottom(40);


        table.row();
        Slider svol = new Slider(0f, 100f, 2f, false, skin);
        svol.setValue(30f);

        svol.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                /*if (game.music != null) {

                }*/
            }
        });

        table.add(svol).width(100).height(10).pad(10);
        table.row();

        Label sfxs = new Label("SFX", skin);
        table.add(sfxs).padBottom(40);

        table.row();
        Slider ssfx = new Slider(0f, 100f, 2f, false, skin);

        ssfx.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {

            }
        });

        table.add(ssfx).width(100).height(50).pad(10);
        table.row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.goBack();
            }
        });
        table.row();
        table.add(backButton).width(200).height(50).pad(10);
        backButton.setColor(1,0,0,1);
        stage.addActor(table);

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.0f, 0.2f, 0.0f, 1.0f);
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
       dispose();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        isActive = false;
    }
}
