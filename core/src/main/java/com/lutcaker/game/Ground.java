package com.lutcaker.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Ground {
    public Texture texture;
    public Sprite sprite;
    public Body body;
    public BodyDef bodyDef;
    public PolygonShape shape;
    public FixtureDef fixtureDef;
    public float width = 20f;
    public float height = 2f;

    public Ground(World world) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 1);
        shape = new PolygonShape();

        shape.setAsBox(width  , height / 8f);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        shape.dispose();
    }
}


