package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by k.baker on 17/02/2015.
 */
public class BoundedCam extends OrthographicCamera{

    private OrthographicCamera cam;
    //    public Rectangle cameraBounds; //The bounds at which we detect the player
    final float camSpeed = 0.1f, ispeed = 1.0f-camSpeed; //Prolly never gonna use these

    public BoundedCam(){ super(); }

    public BoundedCam(float viewportWidth, float viewportHeight) {
        cam = new OrthographicCamera(viewportWidth, viewportHeight);
        cam.setToOrtho(true, cam.viewportWidth, cam.viewportHeight);
        cam.position.set(cam.viewportWidth /2f, cam.viewportHeight /2f, 0);
        cam.update();
    }

    public void camUpdate() {
        if(cam.position.x < cam.viewportWidth/2 - 10)
            cam.position.x = cam.viewportWidth/2 - 10;
        cam.update();
    }
}
