package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.HashMap;

/**
 * Created by Chariot on 1/30/2015.
 */
public abstract class Entity {

    enum State { //tells which state the entity is in. IDLE is the default. (meaning it aint doing nothing)
        IDLE, RUNNING, JUMPING, COMPLETE
    }

    Sprite sprite;
    float x; //x position
    float y; //y positions
    float h; //height
    float w; //width
    float r; //rotation
    float a; //alpha
    State state = State.IDLE;


    public Fixture f;
    float translateX; //to center the sprite image around its physics object
    float translateY;

    Animation animation;
    HashMap<State,Animation> animations = new HashMap<>();

    abstract void update();
    abstract void draw();
    abstract void dispose();
}
