package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

public class Game extends ApplicationAdapter {

	static ArrayList<Entity> backgroundEntities ;
	static ArrayList<EntActor> enemyEntities ;
	static ArrayList<Entity> pickupEntities ;
	static ArrayList<EntActor> playerEntities ;
	Player mm; //megaman

	Box2DDebugRenderer debugRenderer; //to show our nice collision bounding boxes

	SpriteBatch batch; //we need this to tell openGL what to draw

	static World world;
	static OrthographicCamera cam;


	//experimental
	PolygonShape groundBox;

	@Override
	public void create () {

		world = new World(new Vector2(0, -50), true); //physics. gravity = -10
		debugRenderer = new Box2DDebugRenderer();

		//camera
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(225 * (w / h), 225 );
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		//cam.zoom = 2;
		cam.update();

		System.out.println("Cam viewport "+cam.viewportWidth + " " + cam.viewportHeight);

		batch = new SpriteBatch();
		backgroundEntities = new ArrayList<>();
		enemyEntities = new ArrayList<>();
		pickupEntities = new ArrayList<>();
		playerEntities = new ArrayList<>();

		mm = new Player();
		playerEntities.add(mm);

		testPhysics();

	}

	void testPhysics(){
//		circle = new CircleShape();
//		circle.setRadius(6f);
//		fixtureDef =new FixtureDef();
//		fixtureDef.shape = circle;
//		fixtureDef.density = 0.5f;
//		fixtureDef.friction = 0.4f;
//		fixtureDef.restitution = 0.6f; // Make it bounce a little bit
//
//
//		bodyDef = new BodyDef();
//// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
//		bodyDef.type = BodyDef.BodyType.DynamicBody;
//		bodyDef.position.set(50, cam.viewportHeight);
//		body = world.createBody(bodyDef);
//		fixture = body.createFixture(fixtureDef);
		//BODY HAS A SHAPE
		//FIXTURE HAS A BODY



		// Create a polygon shape
		groundBox = new PolygonShape();
		groundBox.setAsBox(cam.viewportWidth, 2.0f);
        // Create a f from our polygon shape and add it to our ground body
		// Create a body from the defintion and add it to the world
		BodyDef groundBodyDef =new BodyDef();
		groundBodyDef.position.set(new Vector2(0, 2));// Set its world position
		Body groundBody = world.createBody(groundBodyDef);
		groundBody.createFixture(groundBox, 0.0f);
	}
	@Override
	public void dispose() {
		super.dispose();
		//circle.dispose();
		groundBox.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//batch.draw(img, 0, 0);

//		for (Entity e : backgroundEntities){
//			e.update(backgroundEntities);
//		}
//
		for (EntActor e : playerEntities){
			e.update();
		}

//		cam.zoom -= 0.02;
//		cam.translate(3, 0, 0);
//		cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 100 / cam.viewportWidth);
//		float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
//		float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
//		cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
//		cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);

		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
//
//		for (Entity e : backgroundEntities){ //draw backgrounds first!
//			batch.draw( e.sprite, e.positionX,e.positionY);
//		}
//
		for (EntActor e : playerEntities) { //draw players over everything else that has been drawn so far
			e.sprite.draw(batch);
		}


		batch.end();

		world.step(1/60f, 6, 2); //make a physics step of 1/60 (SYNC with out 60fps screen)
		// 6 and 2 are for the vertical/horizontal physics accuracy iterations.
		// the higher they go, the more accurate physics wil be, but also the more slower it gets,

		debugRenderer.render(world, cam.combined); //show our physics bounding boxes
	}
}
