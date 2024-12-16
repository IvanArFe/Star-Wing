package com.example.p2_miv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class Animator {
    private ArrayList<AnimatedObject> animatedObjects;
    private ArrayList<GameObject3D> loadedObjects;
    private Random random;
    private float globalSpeed;
    public Animator(Random random, ArrayList<GameObject3D> loadedObj, float initialSpeed){
        this.animatedObjects = new ArrayList<>();
        this.random = random;
        this.loadedObjects = loadedObj;
        this.globalSpeed = initialSpeed;
    }

    // Auxiliar class to handle updates on objects to avoid modifying original references
    private static class AnimatedObject{
        GameObject3D obj;
        float posX, posY, posZ;
        float startY, startZ;

        AnimatedObject(GameObject3D gameObject, float posX, float startY, float startZ){
            this.obj = gameObject;
            this.posX = posX;
            this.posY = startY;
            this.posZ = startZ;
            this.startY = startY;
            this.startZ = startZ;
        }

        public GameObject3D getObj() { return obj; }

        public float getPosX() { return posX; }

        public float getPosY() { return posY; }

        public float getPosZ() { return posZ; }

    }

    public void addObject(GameObject3D gameObject, float startX, float startY, float startZ){
        if(!existObject(gameObject)){
            animatedObjects.add(new AnimatedObject(gameObject, startX, startY, startZ));
        }
    }

    private boolean existObject(GameObject3D gameObject) {
        for(AnimatedObject animObj : animatedObjects){
            if(animObj.getObj() == gameObject){
                return true;
            }
        }
        return false;
    }

    public void updateObject(){
        Iterator<AnimatedObject> it = animatedObjects.iterator();

        while (it.hasNext()){
            AnimatedObject animatedObj = it.next(); // Get every object in list

            // Update position
            animatedObj.posZ += globalSpeed;

            if(isOffScreen(animatedObj.getPosZ())){
                it.remove();
            }
        }
    }

    public void regenerateObject() {

        if(!loadedObjects.isEmpty()){
            GameObject3D newObj = loadedObjects.get(random.nextInt(loadedObjects.size()));

            float newX = random.nextFloat() * 15 - 5;

            if(!existObject(newObj)){
                addObject(newObj, newX, 0.0f, 2.5f);
            }
        }
    }

    private boolean isOffScreen(float z){
        return (z < -25);
    }

    public void drawAnimated(GL10 gl){
        for(AnimatedObject animatedObj : animatedObjects){
            gl.glPushMatrix();
            gl.glTranslatef(animatedObj.getPosX(), animatedObj.getPosY(), animatedObj.getPosZ());

            animatedObj.getObj().draw(gl, 0);
            gl.glPopMatrix();
        }
    }
}
