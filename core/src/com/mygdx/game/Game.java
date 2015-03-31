package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

public class Game extends ApplicationAdapter {

	static ArrayList<EntEnvironment> backgroundEntities ;
	static ArrayList<EntActor> enemyEntities ;
	static ArrayList<EntActor> playerEntities ;
    static ArrayList<Entity> pickupEntities ;

    static World world;
    static OrthographicCamera cam;
    static MContactListener contactListener;

	Box2DDebugRenderer debugRenderer; //to show our nice collision bounding boxes
	SpriteBatch batch; //we need this to tell openGL what to draw

	//experimental
	PolygonShape groundBox;
    PolygonShape wallBox;

    Player mm; //megaman
    float PPM; //Pixel-to-Meter Conversion rate for Box2D

	@Override
	public void create () {

		world = new World(new Vector2(0, -10f), true); // only use -10f
		contactListener = new MContactListener();
        world.setContactListener(contactListener);
		debugRenderer = new Box2DDebugRenderer();



		//camera
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(225 * (w / h), 225  );
        cam.update();

        System.out.println("Cam viewport "+cam.viewportWidth + " " + cam.viewportHeight);

		batch = new SpriteBatch();
		backgroundEntities = new ArrayList<>();
		enemyEntities = new ArrayList<>();
		pickupEntities = new ArrayList<>();
		playerEntities = new ArrayList<>();

		mm = new Player();
		playerEntities.add(mm);
        backgroundEntities.add(new ObjPlatform(50,50));

		testPhysics();
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

		for (EntEnvironment e : backgroundEntities){
			e.update();
		}

		for (EntActor e : playerEntities){
			e.update();
		}

        updateGlobalCam(mm);

        if(cam.position.x < cam.viewportWidth/2 - 10)
            cam.position.x = cam.viewportWidth/2 - 10;
        if(cam.position.y < cam.viewportHeight/2 - 10)
            cam.position.y = cam.viewportHeight/2 - 10;
		cam.update();

		batch.setProjectionMatrix(cam.combined);
		batch.begin();

//		for (Entity e : backgroundEntities){ //draw backgrounds first!
//			batch.draw( e.sprite, e.positionX,e.positionY);
//		}

        for (EntEnvironment e : backgroundEntities) {
            e.sprite.draw(batch);
        }
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

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = width * (width/height);  //We will see width/32f units!
        cam.viewportHeight = height ;
        cam.update();
    }

    public void updateGlobalCam(EntActor e) {
        Vector2 tempPosition = new Vector2(e.get2DVector());
        Vector3 cameraPosition = new Vector3(cam.position);
        Vector3 target = new Vector3(0, 0, 0);

        if(tempPosition.x > cam.viewportWidth/3) {
            target = new Vector3(tempPosition.x, 0, 0);

            if(tempPosition.y > cam.viewportHeight/3)
                target = new Vector3(tempPosition.x, tempPosition.y, 0);
        }
        else if(tempPosition.y > cam.viewportHeight/3) {
            target = new Vector3(0, tempPosition.y, 0);

            if(tempPosition.x > cam.viewportWidth/3)
                target = new Vector3(tempPosition.x, tempPosition.y, 0);
        }
        cameraPosition.lerp(target, 0.1f);
        cam.position.set(cameraPosition);
    }

    void testPhysics(){
        //Needs Entity class to create these so they can have their own fixture

        // Create a polygon shape
        groundBox = new PolygonShape();
        wallBox = new PolygonShape();
        groundBox.setAsBox(cam.viewportWidth * 10f, 2.0f);
        wallBox.setAsBox(2.0f, cam.viewportHeight * 10f);

        // Create a f from our polygon shape and add it to our ground body
        // Create a body from the defintion and add it to the world
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(0, 0));// Set its world position

        Body groundBody = world.createBody(groundBodyDef);

        groundBody.setUserData("platform");

        groundBody.createFixture(groundBox, 0.0f).setUserData("platform");
        groundBody.createFixture(wallBox, 0.0f).setUserData("platform");
    }
}

