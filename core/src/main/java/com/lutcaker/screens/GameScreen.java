package com.lutcaker.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lutcaker.game.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lutcaker.entities.Obstacle;
import com.lutcaker.entities.Player;
import com.lutcaker.entities.PlayerState;
import com.lutcaker.game.Ground;
import com.lutcaker.game.ScreenManager;
import com.lutcaker.physics.CollisionListener;
import com.lutcaker.physics.Constants;

public class GameScreen implements Screen {
    private Stage stage;
    private Skin skin;
    public ScreenManager manager;
    private Array<Button> buttons;
    private Player player;
    private SpriteBatch batch;

    private CollisionListener listener;

    private Sound sound;
    private Music music;
    private Main game;

    private Array<Obstacle> obstacles;
    private World world;
    private Ground ground;
    private Viewport worldViewport;
    private ScreenViewport uiViewport;
    private OrthographicCamera camera;


    private Table table;
    private float timer;
    private float accumulator;
    final float deadZone = -1;

    boolean isActive;
    private boolean switching = false;

    Label healthLabel;
    Label scoreLabel;

    public GameScreen(Main game) {
        this.game = game;
        timer = 0;

        camera = new OrthographicCamera();
        world = new World(new Vector2(0,-9.81f), true);
        player = new Player(world);
        ground = new Ground(world);
        obstacles = new Array<>();
        batch = new SpriteBatch();

        float worldWidth = 8;
        float worldHeight = 5;

        uiViewport = new ScreenViewport();

        worldViewport = new FitViewport(worldWidth, worldHeight);

        stage = new Stage(uiViewport);

        listener = new CollisionListener();
        world.setContactListener(listener);

    }


    @Override
    public void show() {

        obstacles = new Array<>();

        isActive = true;


        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        healthLabel = new Label("Health: " + player.health, style);
        scoreLabel = new Label("Score: " + player.score, style);

        table = new Table();

        table.setFillParent(true);

        table.top();
        table.left();
        table.add(healthLabel).pad(10);
        healthLabel.setFontScale(2f);
        table.row();
        table.add(scoreLabel).pad(10);
        scoreLabel.setFontScale(2f);



        world.setContactListener(listener);

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float worldWidth = 8;
        float worldHeight = 5;

        player.texture = new Texture("test_rectangle.png");

        player.sprite = new Sprite(player.texture);

        player.sprite.setSize(player.width, player.height);

        player.width = player.sprite.getWidth();
        player.height = player.sprite.getHeight();


        ground.texture = new Texture("game_terrain.png");
        ground.sprite = new Sprite(ground.texture);
        ground.sprite.setSize(ground.width, ground.height);


        /*for (int i = 0; i < 10; i++) {
            digitTextures[i] = new Texture(Gdx.files.internal(i + ".png"));
        }*/
        stage.addActor(table);
        System.out.println(stage.getViewport().getWorldWidth());
        System.out.println(stage.getViewport().getWorldHeight());
    }


    @Override
    public void render(float v) {
        update(v);

        ScreenUtils.clear(Color.BLACK);

        draw();

        stage.act(v);
        stage.draw();
        if (player.isDead && !switching) {
            switching = true;
            game.setScreen(new GameOverScreen(game, player.score));
            return;
        }

    }

    private void update(float deltaTime) {
        input(deltaTime);
        doPhysicsStep(deltaTime);
        handleDeaths();
        runlogic(deltaTime);

        float spriteX = player.sprite.getX();
        float spriteY = player.sprite.getY();

        healthLabel.setText("Health: " + player.health);
        scoreLabel.setText("Score: " + player.score);


        player.sprite.setPosition(
            player.body.getPosition().x - player.width / 2f,
            player.body.getPosition().y - player.height / 2f
        );

        ground.sprite.setPosition(
            ground.body.getPosition().x - ground.width / 2f,
            ground.body.getPosition().y - ground.height / 2f
        );


        for (int i = obstacles.size - 1; i >= 0; i--) {

            Obstacle o = obstacles.get(i);
            float obstacleY = o.sprite.getY();

            o.sprite.setPosition(o.body.getPosition().x - o.width / 2f,
                o.body.getPosition().y - o.height / 2f);

            o.sprite.setSize(o.width, o.height);

            o.body.setLinearVelocity(0, -2f);

            if (obstacleY < deadZone) {
                if (!o.didHitPlayer) {
                    player.addScore(5);
                }
                world.destroyBody(o.body);
                obstacles.removeIndex(i);
            }

        }

    }


    private void runlogic(float deltaTime) {
        float worldWidth = worldViewport.getWorldWidth();
        float worldHeight = worldViewport.getWorldHeight();

        timer += deltaTime;



        if (timer > 1f) {
            timer = 0;
            spawnObstacle();
        }
    }

    private void input(float deltaTime) {
        float speed = 4f;
        float jumpSpeed = 4f;
        float jumpImpulse = 1.0f;
        Vector2 counterPoint = player.body.getWorldCenter();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.body.setLinearVelocity(speed, player.body.getLinearVelocity().y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.body.setLinearVelocity(-speed, player.body.getLinearVelocity().y);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.state = PlayerState.ASCENDING;
        }

        if (player.state == PlayerState.ASCENDING) {
            player.body.applyLinearImpulse(new Vector2(0, jumpImpulse), player.body.getWorldCenter(), true);
        }

        if (player.body.getPosition().y > 5) {
            player.state = PlayerState.DESCENDING;
        }
        if (player.state == PlayerState.DESCENDING) {
            player.body.applyLinearImpulse(new Vector2(0, -jumpImpulse), player.body.getWorldCenter(), true);
        }
    }
    @Override
    public void resize(int i, int i1) {
        camera.setToOrtho(false, i, i1);
        worldViewport.update(i, i1, true);
        stage.getViewport().update(i, i1, true);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    private void spawnObstacle() {
        float worldWidth = worldViewport.getWorldWidth();
        float worldHeight = worldViewport.getWorldHeight();

        Obstacle o = new Obstacle(world, 5,1,1);
        o.sprite.setPosition(MathUtils.random(0f, worldViewport.getWorldWidth() - o.width), 5f);
        obstacles.add(o);
    }

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
            accumulator -= Constants.TIME_STEP;
        }
    }

    void handleDeaths() {
        if (player.isDead && player.body.isActive()) {
            player.body.setActive(false);
        }
    }


    private void draw() {
        worldViewport.apply();
        batch.setProjectionMatrix(worldViewport.getCamera().combined);


        batch.begin();
        float worldWidth = worldViewport.getWorldWidth();
        float worldHeight = worldViewport.getWorldHeight();

        float playerWidth = player.sprite.getWidth();
        float playerHeight = player.sprite.getHeight();
        player.sprite.setX(MathUtils.clamp(player.sprite.getX(),0,worldWidth - playerWidth));
        //player.sprite.setY(MathUtils.clamp(player.sprite.getY(), 0, worldHeight - playerHeight));

        player.sprite.draw(batch);
        ground.sprite.draw(batch);

        //scale the text to fit in the corner of the screen


        for (Obstacle o : obstacles) {
            o.sprite.draw(batch);
        }


        //load the textures here
        //batch.draw(playerTexture, 0, 0,1,1);
        batch.end();
    }


    @Override
    public void hide() {
        isActive = false;
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.setContactListener(null);

        world.dispose();

        for (Obstacle o : obstacles) {
            o.texture.dispose();
        }
    }
}
