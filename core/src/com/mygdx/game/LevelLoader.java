package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.logging.Level;

/**
 * Created by Keno on 5/25/2015.
 */
//Were just making it an OrthogonalMaps by default and expand later
public class LevelLoader {

    private World world;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private ChainShape cs;
    private PolygonShape polyShape;

    private BodyDef bodyDef;
    private FixtureDef fixtureDef;

    public float tileSize;
    public float PPM;
    public int numofLayers;

    public LevelLoader(World w, String tmxDir, float pixelPerUnit) {

        this.world = w;
        this.PPM = pixelPerUnit;

        tiledMap = new TmxMapLoader().load(tmxDir);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    //This will only be done with TMX files for now so were only working with tiles for the moment
    public void createLevelFixtures() {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("floor");

        tileSize = layer.getTileWidth();

        BodyDef fBodyDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();

        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {
                //get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // Make a check
                if(cell == null) continue;
                if(cell.getTile() == null) continue;

                // create a body + fixture
                fBodyDef.type = BodyDef.BodyType.StaticBody;
                fBodyDef.position.set((col + 0.5f) * tileSize / PPM,
                        (row + 0.5f) * tileSize / PPM);
                fBodyDef.allowSleep = true;

                cs = new ChainShape();

                fDef.friction = 0.8f;
                fDef.shape = cs;
                fDef.filter.categoryBits = 1;
                fDef.filter.maskBits = -1;
                fDef.isSensor = false;

                Vector2[] v = new Vector2[5];
                v[0] = new Vector2( -tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[1] = new Vector2( -tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[2] = new Vector2( tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[3] = new Vector2( tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[4] = new Vector2( -tileSize / 2 / PPM, -tileSize / 2 / PPM);

                cs.createChain(v);
                world.createBody(fBodyDef).createFixture(fDef).setUserData("platform");
            }
        }
    }

    public void levelRender() { //Note we can load our level without the fixtures once instantiated
        tiledMapRenderer.setView(Game.cam);
        tiledMapRenderer.render();
    }

    //In case you only want to render certain laters
    public void levelRender(int[] selectedLayers){
        tiledMapRenderer.setView(Game.cam);
        tiledMapRenderer.render(selectedLayers);
    }

    //In case we only want certain layers rendered
    public void levelRender(int[] foregroundLayers, int[] backgroundLayers) {
        tiledMapRenderer.render(backgroundLayers);
        tiledMapRenderer.setView(Game.cam);
        tiledMapRenderer.render(foregroundLayers);
    }

    public void dispose() {
        cs.dispose();
    }
}
