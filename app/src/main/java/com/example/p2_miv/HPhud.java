package com.example.p2_miv;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
public class HPhud {
    private float vertices[] = {
            -0.5f, -0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f,
            0.5f,  0.5f, 0.0f,
            0.5f, -0.5f, 0.0f};
    private short faces[] = { 0, 1, 2, 0, 2, 3 };

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;

    // Our index buffer.
    private ShortBuffer indexBuffer;
    private Background healthBar;

    // HP bar constructor
    public HPhud(GL10 gl, Context context) {
        //Move the vertices list into a buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        //Move the faces list into a buffer
        ByteBuffer ibb = ByteBuffer.allocateDirect(faces.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(faces);
        indexBuffer.position(0);

        healthBar = new Background();
        healthBar.loadTexture(gl, context, R.raw.healthbar);
    }

    // Method to draw HP bar
    public void draw(GL10 gl) {
        healthBar.draw(gl);
    }

}
