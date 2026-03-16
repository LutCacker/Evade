package com.lutcaker.physics;
import com.badlogic.gdx.physics.box2d.*;
import com.lutcaker.entities.Obstacle;
import com.lutcaker.entities.Player;
import com.lutcaker.game.Ground;


public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        Object dataA = a.getBody().getUserData();
        Object dataB = b.getBody().getUserData();


        //Check if dataA was a player and if dataB was an obstacle
        if (dataA instanceof Player && dataB instanceof Obstacle) {
            ((Player) dataA).takeDamage(((Obstacle) dataB).damage);
            System.out.println(((Player) dataA).health);
        }

        //CHeck if dataB was a player and if dataA was an Obstacle
        if (dataB instanceof Player && dataA instanceof Obstacle) {
            ((Player) dataB).takeDamage(((Obstacle) dataA).damage);
        }

        if (dataA instanceof Player && dataB instanceof Ground) {
            ((Player) dataA).onGround = true;
        }



    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        Object dataA = a.getBody().getUserData();
        Object dataB = b.getBody().getUserData();

        if (dataA instanceof Player && dataB instanceof Ground) {
            ((Player) dataA).onGround = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
