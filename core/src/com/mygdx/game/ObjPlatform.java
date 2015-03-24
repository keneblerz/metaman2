package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;


/**
 * Created by Keno on 2/22/2015.
 */
public class ObjPlatform extends EntEnvironment {

    ObjPlatform(float new_x, float new_y){
        x = new_x;
        y = new_y;
        region = new TextureRegion( new Texture(Gdx.files.internal("core/assets/objects/mmplatform.png")));

        shape = new PolygonShape();
        shape.setAsBox(region.getRegionWidth()/2,region.getRegionHeight()/2);

        BodyDef groundBodyDef = new BodyDef();

       // groundBodyDef.position.set(new Vector2(0, 0));// Set its world position

        Body groundBody = Game.world.createBody(groundBodyDef);
        Fixture f =groundBody.createFixture(shape, 0.0f);

        f.setFriction(.5f);



        sprite = new Sprite(region);
        sprite.setPosition(new_x,new_y);
        f.getBody().setTransform(new_x + region.getRegionWidth()/2,new_y + region.getRegionHeight()/2,0);



    }

    @Override
    void dispose() {
        shape.dispose();

    }


}
