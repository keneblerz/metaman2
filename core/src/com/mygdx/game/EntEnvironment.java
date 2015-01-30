package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Chariot on 1/30/2015.
 */
public abstract class EntEnvironment extends Entity {

    TextureRegion region;

    @Override
    void draw() {
        sprite = new Sprite(region);
    }

    @Override
    void update() {

    }
}
