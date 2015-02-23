package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Created by Chariot on 1/30/2015.
 */
public abstract class EntEnvironment extends Entity {

    PolygonShape shape;
    TextureRegion region;

    @Override
    void draw() {
        sprite = new Sprite(region);
        sprite.setPosition(x,y);
    }

    @Override
    void update() {
    }


}
