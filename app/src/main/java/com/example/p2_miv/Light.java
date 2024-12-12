package com.example.p2_miv;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
public class Light {
    private ByteBuffer vtbb;
    private FloatBuffer posicio;
    private FloatBuffer ambient;
    private FloatBuffer difuse;
    private FloatBuffer specular;
    private GL10 gl;
    private int lightID;
    private FloatBuffer fb_pos;

    public Light(GL10 gl, int lightID){
        this.gl = gl;
        this.lightID = lightID;
        gl.glEnable(lightID);
    }

    /* Enable and disable lights */
    public void enable(){ gl.glEnable(lightID); }
    public void disable(){ gl.glDisable(lightID); }

    /* Light position */
    public void setPosition(float[] pos){
        fb_pos = FloatBuffer.wrap(pos);
        gl.glLightfv(lightID, GL10.GL_POSITION, fb_pos);
    }

    public void setPosition(){ // After any transformation, call this method again
        if(fb_pos != null){
            gl.glLightfv(lightID, GL10.GL_POSITION, fb_pos);
        }
    }

    /* Set light colors */
    public void setAmbientColor(float[] color){
        FloatBuffer fb = FloatBuffer.wrap(color);
        gl.glLightfv(lightID, GL10.GL_AMBIENT, fb);
    }

    public void setDifusseColor(float[] color){
        FloatBuffer fb = FloatBuffer.wrap(color);
        gl.glLightfv(lightID, GL10.GL_DIFFUSE, fb);
    }

    public void setSpecularColor(float[] color){
        FloatBuffer fb = FloatBuffer.wrap(color);
        gl.glLightfv(lightID, GL10.GL_SPECULAR, fb);
    }

}
