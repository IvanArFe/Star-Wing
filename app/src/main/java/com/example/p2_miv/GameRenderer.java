package com.example.p2_miv;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameRenderer implements Renderer {
    private Background background;
    private MainActivity mainActivity;
    private Context context;
    private int width;
    private int height;
    private Scene scene;
    private Light light;
    private Starship starship;
    private GameObject3D lighthouse;
    private GameObject3D seaShell;
    private ArrayList<GameObject3D> loadedObjects;
    private HPhud hudHP;
    private float starshipX = 0.0f;
    private float starshipY = 0.3f;
    private float inclX = 0.0f;
    private float inclZ = 0.0f;
    private float eyeCameraY = 2.5f;
    private float centerCameraY = 2.5f;
    private float cameraInclZ = 0.0f;
    private float cameraPosX = 0.0f;
    private float maxCameraShiftX = 2.0f;
    private float cameraShiftSpeed = 0.1f;
    private Random random;
    private Animator animator;
    private long lastSpawnTime = 0;
    private int spawnInterval = 6000;

    public GameRenderer(Context context){
        this.context = context;
        this.scene = new Scene();
        this.starship = new Starship(context, R.raw.armwing2);
        this.mainActivity = (MainActivity) context;
        this.random = new Random();
        this.loadedObjects = new ArrayList<>();

        // Pre load objects
        preLoadObjects();
        this.animator = new Animator(random, loadedObjects, -0.1f);

    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Image Background color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Enable lights
        gl.glEnable(GL10.GL_LIGHTING);

        // Enable Normalize
        gl.glEnable(GL10.GL_NORMALIZE);

        // Load and draw background
        gl.glPushMatrix();
        background = new Background();
        background.loadTexture(gl, context, R.raw.background);
        gl.glPopMatrix();

        // Load hud and its texture
        gl.glPushMatrix();
        hudHP = new HPhud(gl, context);
        //starship.loadTexture(gl, context, R.raw.colors);
        gl.glPopMatrix();


        // Set scene light
        light = new Light(gl, GL10.GL_LIGHT0);
        light.setPosition(new float[]{0.0f, 0.0f, -20, 0.0f});

        light.setAmbientColor(new float[]{0.8f, 0.8f, 0.8f});
        light.setDifusseColor(new float[]{1, 1, 1});

        int[] textureIds = {R.raw.woodtexture, R.raw.colors, R.raw.colors};
        for(int i = 0; i < loadedObjects.size(); i++){
            if(loadedObjects.get(i).textureEnabled){
                loadedObjects.get(i).loadTexture(gl, context, textureIds[i]);
            }
        }

    }
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if(height == 0){ height = 1; }
        this.width = width;
        this.height = height;

        gl.glViewport(0, 0, width, height);
    }
    @Override
    public void onDrawFrame(GL10 gl) {
        setPerspectiveProjection(gl);
        // Clear color and depth buffers using clear-value set earlier
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        light.setPosition(new float[]{0, -8, -10, 0});

        GLU.gluLookAt(gl, cameraPosX, eyeCameraY, -20.0f, 0f, centerCameraY, 0f,
                (float) Math.sin(Math.toRadians(cameraInclZ)), (float) Math.cos(Math.toRadians(cameraInclZ)), 0f);

        // Draw background
        gl.glPushMatrix();
        gl.glScalef(95f,27.5f,0);
        gl.glRotatef(180,0,1,0);
        gl.glTranslatef(0,0.375f,50);
        background.draw(gl);
        gl.glPopMatrix();

        // Draw and update points
        gl.glPushMatrix();
        scene.updateScene(); // Update point's coords
        scene.draw(gl); // Draw points
        gl.glPopMatrix();

        updateMovement();

        // Draw starship
        gl.glPushMatrix();
        gl.glRotatef(180, 0, 1, 0);
        gl.glScalef(2.5f, 2.5f, 2.5f);
        gl.glTranslatef(starshipX, starshipY, 5f);

        gl.glRotatef(inclZ,0,0,1);
        gl.glRotatef(inclX,1,0,0);

        starship.draw(gl);
        gl.glPopMatrix();

        // ------ Draw 3D Objects -------
        // Update positions
        animator.updateObject();
        // Regenerate objects in scene
        animator.regenerateObject();
        // Draw animated objects
        animator.drawAnimated(gl);


        // -----------  HUD  ----------------
        setOrthographicProjection(gl); // HUD will always be in same perspective

        // HP Bar
        gl.glPushMatrix();
        gl.glTranslatef(-3.9f,-3.5f,0);
        gl.glScalef(1.0f,0.4f,0.0f);
        hudHP.draw(gl);
        gl.glPopMatrix();
    }

    public void handleMovement(char input){
        float move = 0.05f;
        float maxHorizontal = 2.8f;
        float maxBottom = -0.8f;
        float maxTop = 2.0f;

        // Set rotations to 0 at the beginning
        inclZ = 0.0f;
        inclX = 0.0f;

        switch(input){
            case 'A':
                starshipX -= move;
                if(starshipX < -maxHorizontal){
                    starshipX = -maxHorizontal;
                }
                inclZ = 15.0f;
                break;

            case 'D':
                starshipX += move;
                if(starshipX > maxHorizontal){
                    starshipX = maxHorizontal;
                }
                inclZ = -15.0f;
                break;

            case 'W':
                starshipY += move;
                if(starshipY > maxTop){
                    starshipY = maxTop;
                }
                inclX = -15.0f;
                break;

            case 'S':
                starshipY -= move;
                if(starshipY < maxBottom){
                    starshipY = 0.3f;
                }
                inclX = 22.0f;
                break;

            case 'P':
                eyeCameraY = 15.0f;
                centerCameraY = 0.0f;
                break;

            case 'R':
                eyeCameraY = 2.5f;
                centerCameraY = 2.5f;

            default:
                break;
        }
    }

    private void updateMovement(){
        boolean isMoving = false;
        float resetSpeed = 1.35f;
        float camTilt = 0.5f;
        float maxCamIncl = 2.f;

        // Continuous movement
        if(mainActivity.isLeftMove()){
            handleMovement('A');
            cameraInclZ = Math.min(cameraInclZ + camTilt, maxCamIncl);
            cameraPosX = Math.max(-maxCameraShiftX, cameraPosX - cameraShiftSpeed);
            isMoving = true;
        } else if(mainActivity.isRightMove()){
            handleMovement('D');
            cameraInclZ = Math.max(cameraInclZ - camTilt, -maxCamIncl);
            cameraPosX = Math.min(maxCameraShiftX, cameraPosX + cameraShiftSpeed);
            isMoving = true;
        } else if(mainActivity.isUpMove()){
            handleMovement('W');
            isMoving = true;
        } else if(mainActivity.isDownMove()){
            handleMovement('S');
            isMoving = true;
        } else if(mainActivity.isCamUp()){
            handleMovement('P');
            mainActivity.camUp = false;
        } else if(mainActivity.isRestartCam()){
            handleMovement('R');
            mainActivity.restartCam = false;
        }

        if(!isMoving){
            // Reduce tilt angle gradually
            if(inclX > 0.0f){
                inclX = Math.max(0.0f, inclX - resetSpeed);
            } else if(inclX < 0.0){
                inclX = Math.min(0.0f, inclX + resetSpeed);
            }

            if(inclZ > 0.0f){
                inclZ = Math.max(0.0f, inclZ - resetSpeed);
            } else if(inclZ < 0.0f){
                inclZ = Math.min(0.0f, inclZ + resetSpeed);
            }
            cameraInclZ = Math.max(0.0f, cameraInclZ - resetSpeed);
            cameraPosX = Math.abs(cameraPosX) > 0.05f
                    ? cameraPosX -Math.signum(cameraPosX) * resetSpeed * 0.05f : 0.0f;
        }
    }

    private void setPerspectiveProjection(GL10 gl) {
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance
        gl.glDepthMask(true);  // disable writes to Z-Buffer

        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix

        // Use perspective projection
        GLU.gluPerspective(gl, 60, (float) width / height, 0.1f, 100.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset
    }
    private void setOrthographicProjection(GL10 gl){
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        //gl.glOrthof(-1, 1, -1, 1, 0.1f, 10);
        gl.glOrthof(-5, 5, -4, 4, -5, 5);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    private void preLoadObjects() {
        // Pre-load of models to be more efficient
        int[] objectIDs = {R.raw.boat, R.raw.lighthouse, R.raw.lifepreserver};

        for(int i = 0; i < objectIDs.length; i++){
            GameObject3D obj = new GameObject3D(context, objectIDs[i]);

            if(i == 0){
                obj.setRotation(-90, 0,1,0);
                obj.setScale(1.5f,1.5f,1.5f);
            } if(i == 1){
                obj.setRotation(90, 0,1,0);
                obj.setScale(2.0f, 2.0f, 2.0f);
            } if(i == 2){
                obj.setRotation(90, 0,1,0);
                obj.setScale(0.5f,0.5f,0.5f);
            }

            // Add loaded objects to animator
            loadedObjects.add(obj);
        }
    }

}
