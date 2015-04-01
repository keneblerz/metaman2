package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Chariot on 1/30/2015.
 */
//TODO : Develop a Dash system that checks how long since we pressed the move buttons (A & D) in our current case (Not until we perfect normal physics)

public class Player extends EntActor {

    CircleShape circle;
    Contact contact;


    //Trying to implement the same jump system.
    float jumpVelocity = 1; //the percentage of 'jump we have left'. decrease to 0 the longer jump for -- makes a our jump weaker over time
    boolean acceptingJumps = true; //whether or not we care if the user presses the jump button (this stops auto jumps)
    boolean jumpFrame;

    boolean dashing = false;
    float dashCooldown = 0;
    float shootCooldown = 0;

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
        fixtureDef.friction = 0.5f; //no friction against other surfaces //should be between 0 and 1
        fixtureDef.restitution = 0f; // 0 = no bounce, 1 = perfect bounce


        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(50, 120);

        Body body = Game.world.createBody(bodyDef);
        f = body.createFixture(fixtureDef);
        body.setUserData("mega");
        f.setUserData("mega");


        body.setGravityScale(200f); //gravityScale = 0 means this thing will just float, 1.0 is default, use this to change how fast THIS object falls
        body.setFixedRotation(true); //this body NEVER rotates despite torque/inertia applied to it

        setState(State.IDLE);

        translateX = translateY = -16;

        isPlayer = true;
        draw();
    }

    @Override
    void update() {
        super.update();

        dashCooldown += Gdx.graphics.getDeltaTime();
        shootCooldown += Gdx.graphics.getDeltaTime();


        if(state == State.JUMPING) {
            if (animation.isAnimationFinished(stateTime)) { //we need it to stop at frame .4
                stateTime = .4f;
                updateState = false;
            }
        } //Left it out here to take care of the jumping animation while the button isn't being pressed

        if(Gdx.input.isKeyPressed(Input.Keys.W) && jumpVelocity != 0 && acceptingJumps){
            grounded = false;
            if(state!= State.JUMPING){
                setState(State.JUMPING);
//                System.out.println(stateTime);

            }
            if(animation.isAnimationFinished(stateTime)){ //we need it to stop at frame .4
                stateTime = .4f;
                updateState = false;
            }
            jumpVelocity -= 2 * Gdx.graphics.getDeltaTime();
            if (jumpVelocity < 0) jumpVelocity = 0;

            jumpFrame = true;

            f.getBody().applyLinearImpulse(0.0f, 200000.0f, f.getBody().getPosition().x, f.getBody().getPosition().y, true);
//            f.getBody().setLinearVelocity(f.getBody().getLinearVelocity().x,1000);
        }


        if (jumpFrame && !Gdx.input.isKeyPressed(Input.Keys.W)){ //jump frame is only set to true after we jumped a frame ... AND we didn't jump this frame
            jumpFrame = false;
            acceptingJumps = false; // we don't care if they press the jump key or not, we still won't them jump
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){

        }

        if(Gdx.input.isKeyPressed(Input.Keys.A) ){
            if(state!= State.RUNNING && state!= State.JUMPING) setState(State.RUNNING);
            reverse = true;
            f.getBody().applyLinearImpulse(-50000.0f, 0.0f,f.getBody().getPosition().x,f.getBody().getPosition().y, true);
            //f.getBody().applyForce(-100000.0f, 0.0f, f.getBody().getPosition().x, 0, true );
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) ){
            if(state!= State.RUNNING && state!= State.JUMPING) setState(State.RUNNING);
            reverse = false;
            f.getBody().applyLinearImpulse(50000f, 0.0f,f.getBody().getPosition().x,f.getBody().getPosition().y, true);
            //f.getBody().setLinearVelocity(200f,0);
            //f.getBody().applyForce(100000.0f, 0.0f, f.getBody().getPosition().x, 0, true );
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) && f.getBody().getLinearVelocity().y == 0){
            if(state!= State.IDLE) setState(State.IDLE);
            //f.getBody().setLinearVelocity(0,f.getBody().getLinearVelocity().y);
        }
        //super.update();
//        System.out.println("velocity " + f.getBody().getLinearVelocity());

        //Dashses
        if (dashCooldown < 0) System.out.println("Dash Cooldown:" + (0 - dashCooldown));

        if(Gdx.input.isKeyPressed(Input.Keys.F)  && dashCooldown > 0 ){ //F is pressed and the DashTime is positive
            dashCooldown = -4;

//            state = state.DASHING;
            reverse = false;

            f.getBody().applyLinearImpulse(500000f, 0.0f,f.getBody().getPosition().x,f.getBody().getPosition().y, true);
        }

        //Shooting
        if (shootCooldown < 0) System.out.println("Shoot Cooldown:" + (0 - shootCooldown));
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootCooldown > 0){ //F is pressed and the DashTime is positive
            dashCooldown -= -1;
            shootCooldown = -0.3f;
            AudioManager.mSounds.shootLemons.play();

        }

        draw();

        // TODO: Now to make this work with CONTACT
//        a

        if(grounded){ //we're grounded again
            jumpVelocity = 1;
            acceptingJumps = true;
        }

//        System.out.println("Y : " + f.getBody().getPosition().y);
//        System.out.println("Grounded? : " + grounded);
//        System.out.println("Accepting Jumps? : " + acceptingJumps);
    }

    @Override
    void dispose() {
        circle.dispose();
    }


}

