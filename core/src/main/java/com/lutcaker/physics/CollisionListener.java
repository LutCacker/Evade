package com.lutcaker.physics;
import com.badlogic.gdx.physics.box2d.*;
import com.lutcaker.entities.Obstacle;
import com.lutcaker.entities.Player;
import com.lutcaker.game.Ground;
import com.lutcaker.game.Main;


public class CollisionListener implements ContactListener {
    private Main game;
    public CollisionListener(Main game) {
        this.game = game;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        Object dataA = a.getBody().getUserData();
        Object dataB = b.getBody().getUserData();


        //Check if dataA was a player and if dataB was an obstacle
        if (dataA instanceof Player && dataB instanceof Obstacle) {
            ((Player) dataA).takeDamage(((Obstacle) dataB).damage);
            ((Obstacle) dataB).didHitPlayer = true;
            game.hurtSound.play(game.sfxVolume);

            if (((Player) dataA).health == 0) {
                game.hurtSound.stop();
                game.deathSound.play(game.sfxVolume);
            }
        }

        //CHeck if dataB was a player and if dataA was an Obstacle
        if (dataB instanceof Player && dataA instanceof Obstacle) {
            ((Player) dataB).takeDamage(((Obstacle) dataA).damage);
            ((Obstacle) dataA).didHitPlayer = true;
            game.hurtSound.play(game.sfxVolume);

            if (((Player) dataB).health == 0) {
                game.hurtSound.stop();
                game.deathSound.play(game.sfxVolume);
            }
        }

        if (dataA instanceof Player && dataB instanceof Ground) {
            ((Player) dataA).addGroundContacts();
        }

        if (dataB instanceof Player && dataA instanceof Ground) {
            ((Player) dataB).addGroundContacts();
        }



    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        Object dataA = a.getBody().getUserData();
        Object dataB = b.getBody().getUserData();

        if (dataA instanceof Player && dataB instanceof Ground) {
            ((Player) dataA).removeGroundContacts();
        }

        if (dataB instanceof Player && dataA instanceof Ground) {
            ((Player) dataB).removeGroundContacts();
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
