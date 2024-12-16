package com.example.p2_miv;

import javax.microedition.khronos.opengles.GL10;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Point {
    private float x, y, z, speed;
    private FloatBuffer vertexBuffer;

    private static final float[] vertices = {
            0.0f, 0.0f, 0.0f,   // Bottom left coord
    };

    // Constructor of a point in scene
    public Point(float x, float y, float z, float speed){
        this.x = x; // Horizontal coord
        this.y = y; // Vertical coord
        this.z = z; // Initial depth
        this.speed = speed; // Movement speed to cam

        // Create vertex buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    // Go forward to cam
    public void updatePos(){ z += speed; }

    // Delete point after reaching cam
    public boolean isOffScreen(){ return (this.getZ() >= 15.0f); }

    // Method to draw a point in scene
    public void draw(GL10 gl){
        gl.glPushMatrix();

        gl.glTranslatef(x, y, -z); // Transalate position depending on z coord
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f); // White color
        gl.glPointSize(6.0f);
        gl.glEnable(GL10.GL_POINT_SMOOTH);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY); // Activate buffers
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glDrawArrays(GL10.GL_POINTS, 0, 1);

        gl.glDisable(GL10.GL_POINT_SMOOTH);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glPopMatrix();
    }

    // Needed Getters and Setters
    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

}
