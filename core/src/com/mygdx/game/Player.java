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
    PolygonShape mmRect;
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
        animations.put(State.RUNNING,new Animation((1.0f/16),ta.getRegions() ));

        ta = new TextureAtlas(Gdx.files.internal("core/assets/megaman/mmcomplete/output/megaman.atlas"));
        animations.put(State.COMPLETE,new Animation((1.0f/10),ta.getRegions() ));

        ta = new TextureAtlas(Gdx.files.internal("core/assets/megaman/mmidle/output/megaman.atlas"));
        animations.put(State.IDLE,new Animation((1.0f/2),ta.getRegions() ));

        ta = new TextureAtlas(Gdx.files.internal("core/assets/megaman/mmjump/output/megaman.atlas"));
        animations.put(State.JUMPING,new Animation((1.0f/14),ta.getRegions() ));

        //TODO : Need to add rectangle to body shape
        circle = new CircleShape();
        circle.setRadius(16f);
        FixtureDef fixtureDefCircle = new FixtureDef();
        fixtureDefCircle.shape = circle;
        fixtureDefCircle.density = 1f;
        fixtureDefCircle.friction = 1f; //no friction against other surfaces //should be between 0 and 1
        fixtureDefCircle.restitution = 0f; // 0 = no bounce, 1 = perfect bounce


        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(50, 120);
        Body body = Game.world.createBody(bodyDef);
        f = body.createFixture(fixtureDefCircle);
        body.setUserData("mega");
        f.setUserData("mega");


        body.setGravityScale(98.7f); //gravityScale = 0 means this thing will just float, 1.0 is default, use this to change how fast THIS object falls
        body.setFixedRotation(true); //this body NEVER rotates despite torque/inertia applied to it
        body.setLinearDamping(0.1f);

        setState(State.IDLE);

        translateX = translateY = -16;

        //isPlayer = true;
        draw();
    }

    @Override
    protected void update() {
        super.update();

        float tempx = f.getBody().getPosition().x;
        float tempy = f.getBody().getPosition().y;
        float yVel = f.getBody().getLinearVelocity().y;

        dashCooldown += Gdx.graphics.getDeltaTime();
        shootCooldown += Gdx.graphics.getDeltaTime();

        if(grounded){ //we're grounded again FIXME Modify for wall jumping
            jumpVelocity = 1f;
            acceptingJumps = true;
        } //How do i get mega not to jump when in the air

        if (jumpFrame && !Gdx.input.isKeyPressed(Input.Keys.W)){ //jump frame is only set to true after we jumped a frame ... AND we didn't jump this frame
            jumpFrame = false;
            acceptingJumps = false; // we don't care if they press the jump key or not, we still won't them jump
        }

        if(state == State.JUMPING) {
            if (animation.isAnimationFinished(stateTime)) { //we need it to stop at frame .4
                stateTime = .3f;
                updateState = false;
            }
        } //Left it out here to take care of the jumping animation while the button isn't being pressed

        if(Gdx.input.isKeyPressed(Input.Keys.W) && jumpVelocity != 0 && acceptingJumps){ //FIXME Needs to not work while in the air
            if(state!= State.JUMPING){
                setState(State.JUMPING);
                stateTime = 0; // Ensure animation reset
            }
            if(animation.isAnimationFinished(stateTime)){ //we need it to stop at frame .4
                stateTime = .4f;
                updateState = false;
            }
            jumpVelocity -= 2 * Gdx.graphics.getDeltaTime();
            if (jumpVelocity < 0) jumpVelocity = 0;

            jumpFrame = true;

            if(wallJump) {
                if(reverse) {
                    f.getBody().applyLinearImpulse(200000f, 200000.0f, f.getBody().getPosition().x, f.getBody().getPosition().y, true);

                } else {
                    f.getBody().applyLinearImpulse(-200000f, 200000.0f, f.getBody().getPosition().x, f.getBody().getPosition().y, true);
                }
            }
            else {
//                f.getBody().applyLinearImpulse(1f, 1f, f.getBody().getPosition().x, f.getBody().getPosition().y, true);
                f.getBody().setLinearVelocity(0, 200f);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){}

        if(Gdx.input.isKeyPressed(Input.Keys.A) ){
            if(state!= State.RUNNING && state!= State.JUMPING) {
                setState(State.RUNNING);
                stateTime = 0; // Ensure animation reset
            }
            reverse = true;
            f.getBody().applyLinearImpulse(-1f, 0f, f.getBody().getPosition().x, f.getBody().getPosition().y, true);
            tempx += -1.5f;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D) ){
            if(state!= State.RUNNING && state!= State.JUMPING) {
                setState(State.RUNNING);
                stateTime = 0; // Ensure animation reset
            }
            reverse = false;
            f.getBody().applyLinearImpulse(1f, 0f,f.getBody().getPosition().x,f.getBody().getPosition().y, true);
            tempx += 1.5f;
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) && f.getBody().getLinearVelocity().y == 0){
            if(state!= State.IDLE) setState(State.IDLE);
        }

        //Dashes
//        if (dashCooldown < 0) System.out.println("Dash Cooldown:" + (0 - dashCooldown));
//
//        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
//            if (Gdx.input.isKeyPressed(Input.Keys.F) && dashCooldown > 0) { //F is pressed and the DashTime is positive
//                dashCooldown = -0.1f;
//
//                //            state = state.DASHING; //For the animation
//                reverse = false;
//
//                f.getBody().applyLinearImpulse(5000000f, 0.0f, f.getBody().getPosition().x, f.getBody().getPosition().y, true);
//            }
//        }

        //Shooting
        if (shootCooldown < 0) System.out.println("Shoot Cooldown:" + (0 - shootCooldown));
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && shootCooldown > 0){ //F is pressed and the DashTime is positive
            dashCooldown -= -1;
            shootCooldown = -0.1f;
            AudioManager.mSounds.shootLemons.play();
        }

        f.getBody().setTransform(tempx, tempy, f.getBody().getAngle());

        draw();
    }

    @Override
    public void dispose() {
        circle.dispose();
        mmRect.dispose();
    }


}

