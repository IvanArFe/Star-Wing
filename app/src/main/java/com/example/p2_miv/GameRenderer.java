package com.example.p2_miv;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameRenderer implements Renderer {

    private Background background;
    private MainActivity mainActivity;
    private Context context;
    private int width;
    private int height;
    private float[] vertices;
    private FloatBuffer vertexBuffer;

    private Scene scene;
    private Starship starship;
    private float starshipX = 0.0f;

    public GameRenderer(Context context){
        this.context = context;
        this.scene = new Scene();
        this.starship = new Starship(context, R.raw.starwing);
        this.mainActivity = (MainActivity) context;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Image Background color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Load and draw background
        background = new Background();
        background.loadTexture(gl, context);
    }

    private void setOrthographicProjection(GL10 gl){
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(-1, 1, -1, 1, 0.1f, 10); //Coordenadas estandard de OpenGl

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if(height == 0){ height = 1; }
        this.width = width;
        this.height = height;

        gl.glViewport(0, 0, width, height);
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

    @Override
    public void onDrawFrame(GL10 gl) {
        //setOrthographicProjection(gl); // Usar para HUD 2D
        setPerspectiveProjection(gl);
        // Clear color and depth buffers using clear-value set earlier
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        GLU.gluLookAt(gl, 0, 2.5f, -20.0f, 0f, 2.5f, 0f, 0f, 1f, 0f);

        gl.glPushMatrix();
        gl.glScalef(28,25,0);
        gl.glRotatef(180,0,1,0);
        gl.glTranslatef(0,0.375f,50);

        background.draw(gl);
        gl.glPopMatrix();

        // Draw and update points
        gl.glPushMatrix();
        scene.updateScene(); // Update point's coords
        scene.draw(gl); // Draw points
        gl.glPopMatrix();

        /*
        updateMovement();

        // Draw starship
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glTranslatef(starshipX, 0.3f, -8.0f);
        starship.draw(gl);
        gl.glPopMatrix();*/
    }

    public void handleMovement(char input){
        float move = 0.05f;
        float maxHorizontal = 1.0f;

        switch(input){
            case 'A':
                starshipX += move;
                if(starshipX > maxHorizontal){
                    starshipX = maxHorizontal;
                }
                break;

            case 'D':
                starshipX -= move;
                if(starshipX < maxHorizontal){
                    starshipX = -maxHorizontal;
                }
                break;
        }
    }

    private void updateMovement(){
        // Continuous movement
        if(mainActivity.isLeftMove()){
            handleMovement('A');
        } else if (mainActivity.isRightMove()){
            handleMovement('D');
        }
    }

}
