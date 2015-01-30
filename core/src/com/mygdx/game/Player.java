package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Chariot on 1/30/2015.
 */
public class Player extends EntActor {

    CircleShape circle;

    Player(){
        TextureAtlas ta;

        ta = new TextureAtlas(Gdx.files.internal("core/assets/megaman/mmrun/output/megaman.atlas"));
        animations.put(State.RUNNING,new Animation((1.0f/8),ta.getRegions() ));

        ta = new TextureAtlas(Gdx.files.internal("core/assets/megaman/mmcomplete/output/megaman.atlas"));
        animations.put(State.COMPLETE,new Animation((1.0f/10),ta.getRegions() ));

        ta = new TextureAtlas(Gdx.files.internal("core/assets/megaman/mmidle/output/megaman.atlas"));
        animations.put(State.IDLE,new Animation((1.0f/2),ta.getRegions() ));

        ta = new TextureAtlas(Gdx.files.internal("core/assets/megaman/mmjump/output/megaman.atlas"));
        animations.put(State.JUMPING,new Animation((1.0f/10),ta.getRegions() ));

        circle = new CircleShape();
        circle.setRadius(16f);
        FixtureDef fixtureDef =new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.0f; // Make it bounce a little bit

        BodyDef bodyDef = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(50, Game.cam.viewportHeight);
        Body body = Game.world.createBody(bodyDef);
        f = body.createFixture(fixtureDef);


        setState(State.IDLE);

        translateX = translateY = -16;
        draw();
    }

    @Override
    void update() {

        if(Gdx.input.isKeyPressed(Input.Keys.W)){

        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){

        }

        if(Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) ){
            reverse = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)){
            reverse = false;

        }
        //super.update();
        draw();
    }

    @Override
    void dispose() {
        circle.dispose();
    }
}
