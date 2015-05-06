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
    int numfootcontacts = 0;
    private Array<Body> bodiesToRemove = new Array(); // For Powerups and whatnot

    public MContactListener() {
    }

    @Override
    public void beginContact(Contact c) {
        /*Here is where the game needs to know when the player:
        1. Touches the floor
        2. Touches an item
        3. Touches and enemy

        FIXME Needs to jumps continuously while in contact with wall : LETS SAVE WALL JUMPING FOR LATER
        FIXME There is a force that pushes mega away from the wall when jumping : REKT
        */
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

//        if(fa.getUserData().equals("wall") && fb.getUserData().equals("mega")) {
//            for (EntActor e : Game.playerEntities) {
//                e.canJump(true);
////                e.canWallJump(true);
//            }
//        }
        if(fa.getUserData().equals("platform") && fb.getUserData().equals("mega")) {
            for (EntActor e : Game.playerEntities) {
                e.canJump(true);
            }
        }
        if(fa.getUserData().equals("platform top") && fb.getUserData().equals("mega")) {
            for (EntActor e : Game.playerEntities) {
                e.canJump(true);
            }
        }

        String uDataA = (String) fb.getUserData();
        String[] uDataAAarray = uDataA.split(" ");
        if(uDataAAarray.length == 2) {
            for (EntActor e : Game.playerEntities) {
                if ((uDataAAarray[0].equals("bee") && fa.getUserData().equals("mega"))) {
                    System.out.println("Hit by " + uDataAAarray[0] + " " + uDataAAarray[1]);
                    //FIXME Bee resets position

                    System.out.println("Clock reset");
                    e.clock = 6f;
                }
            }
        }
    }

    @Override
    public void endContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

//        if(fa.getUserData().equals("wall") && fb.getUserData().equals("mega")) {
//            for (EntActor e : Game.playerEntities) {
//                e.canJump(false);
////                e.canWallJump(false);
////                System.out.println(e.wallJump);
//            }
//        }
        if(fa.getUserData().equals("platform") && fb.getUserData().equals("mega")) {
            for (EntActor e : Game.playerEntities) {
                e.canJump(false);
            }
        }
        if(fa.getUserData().equals("platform top") && fb.getUserData().equals("mega")) {
            for (EntActor e : Game.playerEntities) {
                e.canJump(false);
            }
        }
    }

    @Override
    public void preSolve(Contact c, Manifold m) {
//        Fixture fa = c.getFixtureA();
//        Fixture fb = c.getFixtureB();
    }

    @Override
    public void postSolve(Contact c, ContactImpulse ci) {
//        Fixture fa = c.getFixtureA();
//        Fixture fb = c.getFixtureB();
    }

    protected boolean hasFootCollided() { return numfootcontacts > 0;}
    public Array<Body> getBodies() { return this.bodiesToRemove; }

}
