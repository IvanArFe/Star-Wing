package com.example.p2_miv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Background {
    int[] textureIDs = new int[3];
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private int texIndex = 0;
    private int texID;

    /* Vertices to cover screen in order to load background img */
    private float[] vertices = {
            -1.0f, -1.0f, 0.0f, //Bottom left
            1.0f, -1.0f, 0.0f,  //Bottom right
            -1.0f, 1.0f, 0.0f,  //Top left
            1.0f, 1.0f, 0.0f    //Top right
    };

    private float[] textureCoords = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    private float[] textureBoostCoords = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.857f,
            1.0f, 0.857f
    };

    // Default constructor
    public Background(){
        // Setup vertex-array buffer. Vertices in float. An float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);// Rewind

        // Setup texture-coords-array buffer, in float. An float has 4 bytes (NEW)
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        textureBuffer = tbb.asFloatBuffer();
        if(texIndex == 2){
            textureBuffer.clear();
            textureBuffer.put(textureBoostCoords);
        }
        else {
            textureBuffer.clear();
            textureBuffer.put(textureCoords);
        }

        textureBuffer.position(0);
    }

    // Method to load a texture into an object
    public void loadTexture(GL10 gl, Context context, int filename){
        gl.glGenTextures(1, textureIDs, texIndex); // Generate texture-ID array
        texID = texIndex;

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[texID]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        Bitmap bitmap;
        // Read and decode input as bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), filename);

        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        texIndex++;
    }

    // Method to draw the background or instances of it
    public void draw(GL10 gl) {
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDisable(GL10.GL_LIGHTING);
        gl.glColor4f(1,1,1,1);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[texID]);
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        // Front face
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, 50.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        // Deshabilitar estados
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_LIGHTING);
    }

}

