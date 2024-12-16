package com.example.p2_miv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class Animator {
    private ArrayList<AnimatedObject> animatedObjects;
    private ArrayList<GameObject3D> loadedObjects;
    private Random random;
    public Animator(Random random, ArrayList<GameObject3D> loadedObj){
        this.animatedObjects = new ArrayList<>();
        this.random = random;
        this.loadedObjects = loadedObj;
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
            this.posY = startY;
            this.posZ = startZ;
            this.startY = startY;
            this.startZ = startZ;
        }

        public float getSpeed() { return speed; }

        public GameObject3D getObj() { return obj; }

        public float getPosX() { return posX; }

        public float getPosY() { return posY; }

        public float getPosZ() { return posZ; }

    }

    public void addObject(GameObject3D gameObject, float speed, float startX, float startY, float startZ){
        if(!existObject(gameObject)){
            animatedObjects.add(new AnimatedObject(gameObject, speed, startX, startY, startZ));
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
            animatedObj.posZ += animatedObj.getSpeed();

            if(isOffScreen(animatedObj.getPosZ())){
                it.remove();
            }
        }
    }

    public void regenerateObject() {

        if(!loadedObjects.isEmpty()){
            GameObject3D newObj = loadedObjects.get(random.nextInt(loadedObjects.size()));

            float newX = random.nextFloat() * 10 - 5;
            float startY = 0.0f;
            float startZ = 2.5f;

            if(!existObject(newObj)){
                addObject(newObj, -0.1f, newX, startY, startZ);
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

            animatedObj.getObj().draw(gl);
            gl.glPopMatrix();
        }
    }
}
