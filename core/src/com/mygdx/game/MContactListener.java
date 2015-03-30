package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

//TODO : All entities assigned "block/floor" to
/**
 * Created by Keno on 3/29/2015.
 */
public class MContactListener  implements ContactListener {
    private int numFootContacts;
    private boolean playerDead;
    private Array<Body> bodiesToRemove = new Array(); // For Powerups and whatnot

    public MContactListener() {
    }

    @Override
    public void beginContact(Contact c) { }

    @Override
    public void endContact(Contact c) {}

    @Override
    public void preSolve(Contact c, Manifold m) {}

    @Override
    public void postSolve(Contact c, ContactImpulse ci) {}

    public Array<Body> getBodies() { return this.bodiesToRemove; }
    public boolean playerCanJump() { return this.numFootContacts > 0; }
    public boolean isPlayerDead() { return this.playerDead; }

}
