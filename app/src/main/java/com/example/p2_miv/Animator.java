package com.example.p2_miv;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

public class Animator {
    private ArrayList<AnimatedObject> animatedObjects;
    public Animator(){
        animatedObjects = new ArrayList<>();
    }

    // Auxiliar class to handle updates on objects to avoid modifying original references
    private static class AnimatedObject{
        GameObject3D obj;
        float speed;
        float posX, posY, posZ;
        float startY, startZ;

        AnimatedObject(GameObject3D gameObject, float speed, float posX, float startY, float startZ){
            this.obj = gameObject;
            this.speed = speed;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.startY = startY;
            this.startZ = startZ;
        }

        public float getSpeed() {
            return speed;
        }

        public GameObject3D getObj() {
            return obj;
        }

        public float getPosX() {
            return posX;
        }

        public float getPosY() {
            return posY;
        }

        public float getPosZ() {
            return posZ;
        }

        public void resetPos(){
            this.posY = startY;
            this.posZ = startZ;
        }
    }

    public void addObject(GameObject3D gameObject, float speed, float startX, float startY, float startZ){
        animatedObjects.add(new AnimatedObject(gameObject, speed, startX, startY, startZ));
    }

    public void updateObject(){
        Iterator<AnimatedObject> it = animatedObjects.iterator();
        while (it.hasNext()){
            AnimatedObject animatedObj = it.next(); // Get every object in list

            // Update position
            animatedObj.posZ += animatedObj.getSpeed();

            if(isOffScreen(animatedObj.getPosZ())){
                //animatedObj.resetPos();
                it.remove();
                animatedObj.posZ = animatedObj.startZ;
            }
        }
    }

    private boolean isOffScreen(float z){
        return (z < -30);
    }

    public void drawAnimated(GL10 gl){
        for(AnimatedObject animatedObj : animatedObjects){
            gl.glPushMatrix();
            gl.glTranslatef(animatedObj.getPosX(), animatedObj.getPosY(), animatedObj.getPosZ());

            animatedObj.getObj().draw(gl);
            gl.glPopMatrix();
        }
    }
}
