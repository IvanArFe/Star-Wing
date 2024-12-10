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

    public void updatePos(){
        z -= speed; // Go forward to cam
    }

    public boolean isOffScreen(){
        //Log.d("Point", "Punto eliminado: X: " + x + ", Y: " + y + ", Z: " + z);
        return (this.getZ() <= 2.0f);  // Delete point after reaching cam
    }

    public void draw(GL10 gl){
        gl.glPushMatrix();

        gl.glTranslatef(x, y, -z); // Transalate position depending on z coord
        gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f); // White color
        gl.glPointSize(12.0f);
        gl.glEnable(GL10.GL_POINT_SMOOTH);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY); // Activate buffers
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glDrawArrays(GL10.GL_POINTS, 0, 1);

        gl.glDisable(GL10.GL_POINT_SMOOTH);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glPopMatrix();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
