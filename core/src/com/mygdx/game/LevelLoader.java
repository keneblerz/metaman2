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
    World world;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    ChainShape cs;
    PolygonShape polyShape;

    BodyDef bodyDef;
    FixtureDef fixtureDef;

    float tileSize;
    float PPM;

    public LevelLoader(World w, String tmxDir, float pixelPerUnit) {

        this.world = w;
        this.PPM = pixelPerUnit;

        tiledMap = new TmxMapLoader().load(tmxDir);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void updateRender() {
        tiledMapRenderer.setView(Game.cam);
        tiledMapRenderer.render();
    }

    public void dispose() {
        cs.dispose();
    }
}
