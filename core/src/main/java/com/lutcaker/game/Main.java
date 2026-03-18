package com.lutcaker.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.lutcaker.entities.Obstacle;
import com.lutcaker.entities.Player;
import com.lutcaker.entities.PlayerState;
import com.lutcaker.physics.Constants;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.lutcaker.physics.CollisionListener;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    static final float PIXELS_PER_METER = 32f;

    final float deadZone = -1;

    private SpriteBatch batch;
    Viewport viewport;
    Music music;
    OrthographicCamera camera = new OrthographicCamera();
    World world = new World(new Vector2(0,-9.81f), true);
    Player player = new Player(world);
    Ground ground = new Ground(world);
    Texture spikeTexture;

    //Texture[] digitTextures = new Texture[10];


    CollisionListener listener = new CollisionListener();


    BitmapFont font;

    Array<Obstacle> obstacles;




    Sound sound;
    float timer;

    private float accumulator = 0;

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

    @Override
    public void create() {

        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("ea_sports.fnt"));
        obstacles = new Array<>();

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
        viewport = new FitViewport(worldWidth, worldHeight);

    }


    private void spawnObstacle() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Obstacle o = new Obstacle(world, 5,1,1);
        o.sprite.setPosition(MathUtils.random(0f, viewport.getWorldWidth() - o.width), 5f);
        obstacles.add(o);
    }

    @Override
    public void render() {

        float spriteX = player.sprite.getX();
        float spriteY = player.sprite.getY();

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
                o.body.getPosition().y  - o.height / 2f);

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


        float deltaTime = Gdx.graphics.getDeltaTime();
        input();
        doPhysicsStep(deltaTime);
        handleDeaths();
        runlogic();
        draw();
    }


    private void input() {
        float speed = 4f;
        float jumpSpeed = 4f;
        float jumpImpulse = 4f;
        Vector2 counterPoint = player.body.getWorldCenter();

        float deltaTime = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.body.setLinearVelocity(speed, player.body.getLinearVelocity().y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.body.setLinearVelocity(-speed, player.body.getLinearVelocity().y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.state = PlayerState.ASCENDING;
        }

        if (player.state == PlayerState.ASCENDING) {
            player.body.applyLinearImpulse(new Vector2(0, jumpImpulse), player.body.getWorldCenter(), true);
        } if (player.sprite.getY() >= 10) {
            player.state = PlayerState.DESCENDING;
        }

        if (player.state == PlayerState.DESCENDING) {
            player.body.applyLinearImpulse(new Vector2(0,-jumpImpulse), player.body.getWorldCenter(), true);
        }




    }

    private void runlogic() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        timer += deltaTime;



        if (timer > 1f) {
            timer = 0;
            spawnObstacle();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);


        batch.begin();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

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


        font.getData().setScale(0.05f, 0.05f);
        font.draw(batch, "Score: " + player.score, 0.5f, viewport.getWorldHeight());
        font.draw(batch, "Health: " + player.health, 0.5f, viewport.getWorldHeight() - 0.5f);
        //load the textures here
        //batch.draw(playerTexture, 0, 0,1,1);
        batch.end();
    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        viewport.update(width, height, true);
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
