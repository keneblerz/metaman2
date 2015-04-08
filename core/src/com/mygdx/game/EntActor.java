package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by Chariot on 1/30/2015.
 */
public abstract class EntActor extends Entity{
    boolean isPlayer = false;
    boolean reverse;
    boolean updateState = true;
    boolean grounded;
    boolean wallJump;
    protected float stateTime;
    float maxVelocity = 50;


    public void setState(State s){ //sets the state of the object
        updateState = true;
        stateTime = 0;
        state = s;

        switch (state){
            case IDLE:
                animation = animations.get(State.IDLE);
                break;
            case RUNNING:
                animation = animations.get(State.RUNNING);
                break;
            case JUMPING:
                animation = animations.get(State.JUMPING);
                break;
            case COMPLETE:
                animation = animations.get(State.COMPLETE);
                break;
        }
    }

    @Override
    public void draw() {
        sprite = new Sprite(animation.getKeyFrame(stateTime, true));


        sprite.setPosition(f.getBody().getPosition().x + translateX, f.getBody().getPosition().y + translateY);
        if (reverse) sprite.flip(true,false);
        if (rotating) sprite.setRotation(MathUtils.radiansToDegrees * f.getBody().getAngle());

        //sprite.setScale(.25f);


    }

    public void canJump(boolean b) {
        grounded = b;
    }
    public void canWallJump(boolean b) { wallJump = b; }

    @Override
    protected void update() {
        if (updateState)
            stateTime += Gdx.graphics.getDeltaTime();
    }

    public Vector2 get2DVector() {
        Vector2 tempPosition = new Vector2(f.getBody().getPosition().x, f.getBody().getPosition().y);
        return tempPosition;
    }
}
