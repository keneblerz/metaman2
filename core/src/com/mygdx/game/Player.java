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
        fixtureDef.density = 1f;
        fixtureDef.friction = 0; //no friction against other surfaces
        fixtureDef.restitution = 0; // Make it not bounce

        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(50, Game.cam.viewportHeight);
        Body body = Game.world.createBody(bodyDef);
        f = body.createFixture(fixtureDef);


        setState(State.IDLE);

        translateX = translateY = -16;

        isPlayer = true;
        draw();
    }

    @Override
    void update() {
        super.update();

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            //f.getBody().applyLinearImpulse(0.0f, 1000.0f, f.getBody().get2DVector().x, f.getBody().get2DVector().y, true);

            f.getBody().setLinearVelocity(f.getBody().getLinearVelocity().x,150);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){


        }

        if(Gdx.input.isKeyPressed(Input.Keys.A) ){
            reverse = true;
            f.getBody().applyLinearImpulse(-2000.0f, 0.0f,f.getBody().getPosition().x,f.getBody().getPosition().y, true);
            //System.out.println("velocity " + f.getBody().getLinearVelocity());
//            f.getBody().setLinearVelocity(-100,f.getBody().getLinearVelocity().y);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) ){
            reverse = false;
            f.getBody().applyLinearImpulse(2000f, 0.0f,f.getBody().getPosition().x,0, true);
            //System.out.println("velocity " + f.getBody().getLinearVelocity());
//            f.getBody().setLinearVelocity(100,f.getBody().getLinearVelocity().y);
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)){
            f.getBody().setLinearVelocity(0,f.getBody().getLinearVelocity().y);
        }
        //super.update();
        System.out.println("velocity " + f.getBody().getLinearVelocity());
        draw();

    }

    @Override
    void dispose() {
        circle.dispose();
    }


}
