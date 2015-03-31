package com.mygdx.game;


import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

//TODO : All entities assigned "block/floor" to
/**
 * Created by Keno on 3/29/2015.
 */
public class MContactListener  implements ContactListener {
    private int numFootContacts = 0;
    private boolean playerDead;
    private Array<Body> bodiesToRemove = new Array(); // For Powerups and whatnot

    public MContactListener() {
    }

    @Override
    public void beginContact(Contact c) {
        /*Here is where the game needs to know when the player:
        1. Touches the floor
        2. Touches an item
        3. Touches and enemy
        */
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa.getUserData().equals("mega") || fb.getUserData().equals("platform")) {
            numFootContacts++;
            System.out.println("Contact " + numFootContacts);

            for (EntActor e : Game.playerEntities) {
                e. = true;
            }

        }
    }

    @Override
    public void endContact(Contact c) {

    }

    @Override
    public void preSolve(Contact c, Manifold m) {}

    @Override
    public void postSolve(Contact c, ContactImpulse ci) {}

    public Array<Body> getBodies() { return this.bodiesToRemove; }
    public boolean playerCanJump() { return this.numFootContacts > 0; }
    public boolean isPlayerDead() { return this.playerDead; }

}
