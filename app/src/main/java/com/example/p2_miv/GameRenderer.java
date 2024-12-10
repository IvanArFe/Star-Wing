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
    private Context context;
    private int width;
    private int height;
    private float[] vertices;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private Scene scene;

    public GameRenderer(Context context){
        this.context = context;
        this.scene = new Scene();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Image Background color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

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
        this.width = width;
        this.height = height;

        updateVerticesForAspectRatio(width, height);
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
        // Limpiar la pantalla
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        GLU.gluLookAt(gl, 0, 1.2f, -5.0f, 0f, 0f, 0f, 0f, 1f, 0f);

        gl.glDisable(GL10.GL_DEPTH_TEST);

        // Draw background
        gl.glPushMatrix();
        background.draw(gl);
        gl.glPopMatrix();

        // Draw and update points
        gl.glPushMatrix();
        scene.updateScene(); // Update point's coords
        scene.draw(gl); // Draw points
        gl.glPopMatrix();

        gl.glEnable(GL10.GL_DEPTH_TEST);

    }

    private void updateVerticesForAspectRatio(int screenWidth, int screenHeight) {
        // Calcular la relación de aspecto
        float aspectRatio = (float) screenWidth / screenHeight;
        float aspectRatioImage = 1.0f;

        float scaleX = 1.0f;
        float scaleY = 1.0f;

        if(aspectRatio > aspectRatioImage){
            scaleX = aspectRatioImage / aspectRatio;
        } else {
            scaleY = aspectRatioImage / aspectRatio;
        }

        // Actualizar las coordenadas de los vértices
        vertices = new float[] {
                -scaleX, -scaleY, 0.0f,  // Inferior izquierdo
                scaleX, -scaleY, 0.0f,  // Inferior derecho
                -scaleX,  scaleY, 0.0f,  // Superior izquierdo
                scaleX,  scaleY, 0.0f   // Superior derecho
        };

        // Actualizar el buffer de vértices
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }
}
