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
        circle.setRadius(14f / Game.PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f; //no friction against other surfaces //should be between 0 and 1
        fixtureDef.restitution = 0f; // 0 = no bounce, 1 = perfect bounce


        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.active = true;
        bodyDef.position.set(120 / Game.PPM, 180 / Game.PPM);

        Body body = Game.world.createBody(bodyDef);
        f = body.createFixture(fixtureDef);
        f.setSensor(true);
        body.setUserData("bee " + num);
        f.setUserData("bee " + num);

        body.setGravityScale(0); //gravityScale = 0 means this thing will just float, 1.0 is default, use this to change how fast THIS object falls
        body.setFixedRotation(true); //this body NEVER rotates despite torque/inertia applied to it


        translateX = translateY = -14 / Game.PPM;

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

        if (clock == 0)
            mmPosition = Game.mm.f.getBody().getPosition(); //Get MM's position every 5 seconds

        clock += Gdx.graphics.getDeltaTime();

        if(clock < 5) {
            isTracking = true;
            //System.out.println("Is Tracking ");
        } else if(clock > 5f) {
            isTracking = false;
            System.out.println("Clock Time " + clock);
        }

        if(isTracking){

            if(f.getBody().getPosition().x > mmPosition.x) {
                f.getBody().setLinearVelocity(-50f, f.getBody().getLinearVelocity().y);
            }
            if(f.getBody().getPosition().x < mmPosition.x) {
                f.getBody().setLinearVelocity(50f,f.getBody().getLinearVelocity().y);
            }
            if (f.getBody().getPosition().y > mmPosition.y) {
                f.getBody().setLinearVelocity(f.getBody().getLinearVelocity().x, -50f);
            }
            if (f.getBody().getPosition().y < mmPosition.y) {
                f.getBody().setLinearVelocity(f.getBody().getLinearVelocity().x, 50f);
            }
        } else {
            f.getBody().setLinearVelocity(0, 30);
        }

        //
        if(getActorCollision())
            resetClock(6f);
        if(clock > 10) {
            resetClock(0);
        }

        draw();
    }
    boolean isTracking = true;
}
