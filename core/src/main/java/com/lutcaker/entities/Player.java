package com.lutcaker.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Entity {
    public Texture texture;
    public Sprite sprite;
    public Rectangle hitbox;
    public Body body;
    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    public PolygonShape shape;
    public PlayerState state;
    public float width;
    public float height;
    public int health;
    public long score;
    public boolean isContacting;
    private int groundContacts = 0;
    public boolean onGround;
    public boolean isDead = false;

    public void addGroundContacts() {
         groundContacts++;
    }

    public void removeGroundContacts() {
        groundContacts = Math.max(0, groundContacts - 1);
    }

    public boolean isOnGround() {
        return groundContacts > 0;
    }

    public void die() {
        isDead = true;
    }

    public void addScore(long amount) {
        score += amount;
    }

    public long getScore() {
        return score;
    }

    public void takeDamage(int damage) {
        this.health -= damage;

        if (this.health <= 0) {
            this.health = 0;
        }

        if (health == 0 && !isDead) {
            die();
        }
    }

    public Player(World world) {
        score = 0;
        health = 100;
        width = 1;
        height = 1;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0,5);
        shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        isContacting = false;

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;

        body = world.createBody(bodyDef);

        body.createFixture(fixtureDef);

        body.setUserData(this);

        shape.dispose();

    }
}




