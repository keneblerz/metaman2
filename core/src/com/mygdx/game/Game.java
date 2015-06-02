package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Enemies.EnemyMmxbee;

import java.util.ArrayList;
import java.util.logging.Level;

/*
Goals/Machanics For Megaman



TODO 3.Player Physics (running/jumping)
TODO 4.Bounding box collisions
TODO 7.Tile Mapping (developing a level)d

TODO 11.Particle Effects

TODO 5.Fix Animations (Tie them to player motion [Running/Jumping])
TODO 6.Cameras (Movement throughout the level)

TODO 9.Advanced Game Mechanics?? (Shooting/Dashing?)

TODO 10.Enemies and AI

TODO 8.State Machines (LevelLoader Selects/Game Over/Game Won/Menus)

TODO Ajillion.InputProcessor will be needed (maybe)

TODO 13.Tie user data to creation of level objects ie. Enemies/Objects/Powerups
TODO 14. (BRB)


* */

public class Game extends ApplicationAdapter {


	static ArrayList<EntEnvironment> backgroundEntities ;
	static ArrayList<EntActor> enemyEntities ;
	static ArrayList<EntActor> playerEntities ;
    static ArrayList<Entity> pickupEntities ;

    public static World world;
    static OrthographicCamera cam;
    static MContactListener contactListener;

//    ///////////////////////
//
//    TiledMap tiledMap;
//    TiledMapRenderer tiledMapRenderer;
//
//    ///////////////////////

	Box2DDebugRenderer debugRenderer; //to show our nice collision bounding boxes
	SpriteBatch batch; //we need this to tell openGL what to draw

	//experimental
	PolygonShape groundBox;
    PolygonShape wallBox;
    ChainShape cs;
    public static Player mm; //megaman
    EnemyMmxbee sampleBeeEnemy;
    LevelLoader levelLoader;

    public float tileSize;
    public final float VIEWPORT = 350f;
    public float PPM = 1f;

	@Override
	public void create () {

		world = new World(new Vector2(0, -10f), true); // only use -10f
		contactListener = new MContactListener();
        world.setContactListener(contactListener);
		debugRenderer = new Box2DDebugRenderer();

        AudioManager.changeMusic(AudioManager.mMusic.sChameleon);

		//camera
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera( VIEWPORT * (w / h), VIEWPORT );
        cam.update();

        //////////////////////

        levelLoader = new LevelLoader(world, "core/assets/TileMaps/TestTMX.tmx", 1 );
        levelLoader.createLevelFixtures();

        ///////////////////////

        System.out.println("Cam viewport " + cam.viewportWidth + " " + cam.viewportHeight);

		batch = new SpriteBatch();
		backgroundEntities = new ArrayList<>();
		enemyEntities = new ArrayList<>();
		pickupEntities = new ArrayList<>();
		playerEntities = new ArrayList<>();

		mm = new Player();
        sampleBeeEnemy = new EnemyMmxbee();

		playerEntities.add(mm);
        backgroundEntities.add(new ObjPlatform(200,200));
        enemyEntities.add(sampleBeeEnemy);

		//testPhysics();
	}

	@Override
	public void dispose() {
		super.dispose();
		groundBox.dispose();
        wallBox.dispose();
        cs.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for (EntEnvironment e : backgroundEntities){
			e.update();
		}

        for (EntActor e : enemyEntities){
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

        levelLoader.levelRender();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

//		for (Entity e : backgroundEntities){ //draw backgrounds first!
//			batch.draw( e.sprite, e.positionX,e.positionY);
//		}

        for (EntEnvironment e : backgroundEntities) {
            e.sprite.draw(batch);
        }

        for (EntActor e : enemyEntities) { //draw enemies
            e.sprite.draw(batch);
        }

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
//        cam.viewportWidth = width * (VIEWPORT/height);  //We will see width/32f units!
        cam.viewportHeight = (VIEWPORT / width) * height ; //I REALLY LIKE THIS CONFIG
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

    public void testPhysics(){
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

        groundBody.createFixture(groundBox, 0.0f).setUserData("platform");
        groundBody.createFixture(wallBox, 0.0f).setUserData("wall");
    }
}

