package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chariot on 1/30/2015.
 */
public abstract class Entity {

    public enum State { //tells which state the entity is in. IDLE is the default. (meaning it aint doing nothing)
        IDLE, RUNNING, JUMPING, COMPLETE
    }

    protected Sprite sprite;
    float x; //x position
    float y; //y positions
    float h; //height
    float w; //width
    float r; //rotation
    float a; //alpha
    State state = State.IDLE;


    boolean multiFixture; //if true, the entity has multiple fixtures/stuff
    HashMap<String,Fixture> fs = new HashMap<>();

    public Fixture f;
    protected float translateX; //to center the sprite image around its physics object
    protected float translateY;
    boolean rotating; //does this object rotate along with the physics

    public Animation animation;
    public HashMap<State,Animation> animations = new HashMap<>();

    abstract void update();
    abstract void draw();
    public abstract void dispose();
}
