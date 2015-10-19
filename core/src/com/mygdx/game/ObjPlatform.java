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

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Keno on 2/22/2015.
 */
public class ObjPlatform extends EntEnvironment {

    ObjPlatform(float new_x, float new_y){
        x = new_x;
        y = new_y;

        fs = new HashMap<>();
        shapes = new HashMap<>();

        //texture
        region = new TextureRegion(new Texture(Gdx.files.internal("core/assets/objects/mmplatform.png")));


        //platform itself
        shape = new PolygonShape();
        shape.setAsBox(region.getRegionWidth()/2,(region.getRegionHeight()/2f)- 10 );

        BodyDef groundBodyDef = new BodyDef();
        Body groundBody = Game.world.createBody(groundBodyDef);

        Fixture f = groundBody.createFixture(shape, 0.0f);
        f.setFriction(.5f);

        f.setUserData("platform");

        fs.put("platform", f);
        shapes.put("platform", shape);
////////////////////////////////////////////////////////////////////////
//        //platform Top -- like the walkable part
//        shape = new PolygonShape();
//        shape.setAsBox(region.getRegionWidth()/2, 1);
//
//        groundBodyDef = new BodyDef();
//        groundBody = Game.world.createBody(groundBodyDef);
//
//        f = groundBody.createFixture(shape, 0.0f);
//        f.setFriction(.5f);
//
//        f.setUserData("platform top");
//
//        fs.put("platform top", f);
//        shapes.put("platform top", shape);
//////////////////////////////////////////////////////////////////////// - Its just for me, the ADD kicks in hard sometimes

        //sprite stuff
        sprite = new Sprite(region);
        sprite.setPosition(new_x,new_y);

        fs.get("platform").getBody().setTransform( (new_x + region.getRegionWidth()/2) / Game.PPM, (new_y -2 + region.getRegionHeight() / 2) / Game.PPM, 0);
//        fs.get("platform top").getBody().setTransform( (new_x + region.getRegionWidth()/2) / Game.PPM, new_y / Game.PPM + (region.getRegionHeight()-1) / Game.PPM,0);
//        f.getBody().setTransform(new_x + region.getRegionWidth()/2,new_y + region.getRegionHeight()/2,0);
    }

    @Override
    public void dispose() {
        shape.dispose();
    }


}
