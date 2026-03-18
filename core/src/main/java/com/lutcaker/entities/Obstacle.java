package com.lutcaker.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public class Obstacle extends Entity {
    public Texture texture;
    public Sprite sprite;
    public Rectangle hitbox;
    public Body body;
    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    public PolygonShape shape;
    public int damage;
    public int width;
    public int height;
    public boolean didHitPlayer;
    float randomX;


    public Obstacle(World world, int damage, int width, int height) {
        this.width = width;
        this.height = height;
        this.damage = damage;
        int worldWidth = 5;
        int worldHeight = 8;
        didHitPlayer = false;
        shape = new PolygonShape();
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        randomX = MathUtils.random(this.width / 2f, worldHeight - this.width / 2f);
        bodyDef.position.set(randomX, worldHeight + 1);
        shape.setAsBox(width / 2f, height / 2f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0f;
        fixtureDef.isSensor = true;

        body = world.createBody(bodyDef);

        body.createFixture(fixtureDef);

        body.setUserData(this);

        shape.dispose();

        texture = new Texture("obstacle_spike.png");
        sprite = new Sprite(texture);
        sprite.setSize(width, height);

    }


}
