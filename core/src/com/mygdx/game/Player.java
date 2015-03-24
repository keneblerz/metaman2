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
        fixtureDef.friction = .5f; //no friction against other surfaces //should be between 0 and 1
        fixtureDef.restitution = .1f; // 0 = no bounce, 1 = perfect bounce


        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(50, 120);

        Body body = Game.world.createBody(bodyDef);
        f = body.createFixture(fixtureDef);


        body.setGravityScale(14f); //gravityScale = 0 means this thing will just float, 1.0 is default, use this to change how fast THIS object falls
        body.setFixedRotation(true); //this body NEVER rotates despite torque/inertia applied to it

        setState(State.IDLE);

        translateX = translateY = -16;

        isPlayer = true;
        draw();
    }

    @Override
    void update() {
        super.update();

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            if(state!= State.JUMPING){
                setState(State.JUMPING);
//                System.out.println(stateTime);

            }
            if(animation.isAnimationFinished(stateTime)){ //we need it to stop at frame .4
                stateTime = .4f;
                updateState = false;
            }
            //f.getBody().applyLinearImpulse(0.0f, -10000.0f, f.getBody().getPosition().x, f.getBody().getPosition().y, true);
            f.getBody().setLinearVelocity(f.getBody().getLinearVelocity().x,1000);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){

        }

        if(Gdx.input.isKeyPressed(Input.Keys.A) ){
            if(state!= State.RUNNING && state!= State.JUMPING) setState(State.RUNNING);
            reverse = true;
            f.getBody().applyLinearImpulse(-5000.0f, 0.0f,f.getBody().getPosition().x,f.getBody().getPosition().y, true);
            //f.getBody().applyForce(-100000.0f, 0.0f, f.getBody().getPosition().x, 0, true );
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) ){
            if(state!= State.RUNNING && state!= State.JUMPING) setState(State.RUNNING);
            reverse = false;
            f.getBody().applyLinearImpulse(5000f, 0.0f,f.getBody().getPosition().x,f.getBody().getPosition().y, true);
            //f.getBody().setLinearVelocity(200f,0);
            //f.getBody().applyForce(100000.0f, 0.0f, f.getBody().getPosition().x, 0, true );
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) && f.getBody().getLinearVelocity().y == 0){
            if(state!= State.IDLE) setState(State.IDLE);
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
