package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by Chariot on 1/30/2015.
 */
public abstract class EntActor extends Entity{
    boolean reverse;
    float stateTime;
    float maxVelocity = 50;



    void setState(State s){ //sets the state of the object
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
    void draw() {
        sprite = new Sprite(animation.getKeyFrame(stateTime, true));
        sprite.setPosition(f.getBody().getPosition().x + translateX, f.getBody().getPosition().y + translateY);
        //sprite.setScale(.25f);
        if (rotating) sprite.setRotation(MathUtils.radiansToDegrees * f.getBody().getAngle());
    }

    @Override
    void update() {
        stateTime += Gdx.graphics.getDeltaTime();
    }
}
