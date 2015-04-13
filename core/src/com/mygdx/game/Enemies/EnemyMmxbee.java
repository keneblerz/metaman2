package com.mygdx.game.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.EntActor;
import com.mygdx.game.Game;

/**
 * Created by Keno on 4/7/2015.
 */
public class EnemyMmxbee  extends EntActor {
    CircleShape circle;
    Contact contact;

    Vector2 mmPosition;

    static int num = 0;


    public EnemyMmxbee(){
        TextureAtlas ta;
        num++;
        ta = new TextureAtlas(Gdx.files.internal("core/assets/enemies/mmxbee/atlas/mmxbee.atlas"));
        animations.put(State.IDLE,new Animation((1.0f/8),ta.getRegions() ));


        circle = new CircleShape();
        circle.setRadius(18f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f; //no friction against other surfaces //should be between 0 and 1
        fixtureDef.restitution = 0f; // 0 = no bounce, 1 = perfect bounce


        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(120, 180);

        Body body = Game.world.createBody(bodyDef);
        f = body.createFixture(fixtureDef);
        body.setUserData("bee " + num);
        f.setUserData("bee " + num);

        String ex = (String)f.getUserData();
        ex.split(" "); //bee, num



        body.setGravityScale(0); //gravityScale = 0 means this thing will just float, 1.0 is default, use this to change how fast THIS object falls
        body.setFixedRotation(true); //this body NEVER rotates despite torque/inertia applied to it


        translateX = translateY = -14;

        setState(State.IDLE);


        f.getBody().setLinearVelocity(30, 0);

        mmPosition = Game.mm.f.getBody().getPosition(); //Get MM's position every 5 seconds
        draw();
    }

    public void dispose(){
        circle.dispose();

    }
    @Override
    protected void update() {
        super.update();
        clock += Gdx.graphics.getDeltaTime();
        trackerclock += Gdx.graphics.getDeltaTime();

        if(clock > 5) {
            isTracking = false;
            trackerclock = 0;
            clock = 0;

            System.out.println("Clock Time " + clock);
        }

        if(trackerclock > 3){
            mmPosition = Game.mm.f.getBody().getPosition(); //Get MM's position every 5 seconds
            isTracking = true;

            trackerclock = 0;

            System.out.println("Is Tracking " + isTracking);
        }

        if(isTracking){

            if(f.getBody().getPosition().x > mmPosition.x) {
                f.getBody().setLinearVelocity(-30, 0);
            }
            if(f.getBody().getPosition().x < mmPosition.x) {
                f.getBody().setLinearVelocity(30,0);
            }
            if (f.getBody().getPosition().y > mmPosition.y) {
                f.getBody().setLinearVelocity(f.getBody().getLinearVelocity().x, -30);
            }
            if (f.getBody().getPosition().y < mmPosition.y) {
                f.getBody().setLinearVelocity(f.getBody().getLinearVelocity().x, 30);
            }
//            System.out.println(f.getBody().getPosition().x - Game.mm.f.getBody().getPosition().x);

//            if( (f.getBody().getPosition().x - Game.mm.f.getBody().getPosition().x) < 1 ||
//                    (Game.mm.f.getBody().getPosition().x - f.getBody().getPosition().x) > -1 ){
//                f.getBody().setLinearVelocity(0,-120);
//            }
            if(f.getBody().getPosition() == mmPosition)
                isTracking = false;
        } else {
            f.getBody().setLinearVelocity(0, 10);
        }

        draw();
    }

    float clock = 0; //general purpose clock for updating actor position
    float trackerclock = 0; // Used to update Mega's position
    float speed = .001f;
    boolean reverse = false;
    boolean isTracking = true;
    boolean clockupdate = true;
}
