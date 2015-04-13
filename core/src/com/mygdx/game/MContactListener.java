package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enemies.EnemyMmxbee;

//TODO : All entities assigned "block/floor" to
/**
 * Created by Keno on 3/29/2015.
 */
public class MContactListener  implements ContactListener {
    private Array<Body> bodiesToRemove = new Array(); // For Powerups and whatnot

    public MContactListener() {
    }

    @Override
    public void beginContact(Contact c) {
        /*Here is where the game needs to know when the player:
        1. Touches the floor
        2. Touches an item
        3. Touches and enemy

        FIXME Needs to jumps continuously while in contact with wall
        FIXME There is a force that pushes mega away from the wall when jumping : REKT
        */
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa.getUserData().equals("wall") && fb.getUserData().equals("mega")) {
            for (EntActor e : Game.playerEntities) {
                e.canJump(true);
                e.canWallJump(true);
            }
        }
        if(fa.getUserData().equals("platform") && fb.getUserData().equals("mega")) {
            for (EntActor e : Game.playerEntities) {
                e.canJump(true);
            }
        }

        String uDataA = (String) fb.getUserData();
        String[] uDataAAarray = uDataA.split(" ");
        if(uDataAAarray.length == 2){
            if((uDataAAarray[0].equals("bee") && fa.getUserData().equals("mega"))) {
                System.out.println("Hit by "+ uDataAAarray[0] + " "+ uDataAAarray[1]);
                //FIXME Bee resets position
            }
        }


    }

    @Override
    public void endContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa.getUserData().equals("wall") && fb.getUserData().equals("mega")) {
            for (EntActor e : Game.playerEntities) {
                e.canJump(false);
                e.canWallJump(false);
                System.out.println(e.wallJump);
            }
        }
    }

    @Override
    public void preSolve(Contact c, Manifold m) {}

    @Override
    public void postSolve(Contact c, ContactImpulse ci) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
    }

    public Array<Body> getBodies() { return this.bodiesToRemove; }

}
