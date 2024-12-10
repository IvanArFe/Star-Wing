package com.example.p2_miv;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

public class Scene {
    private ArrayList<Point> points = new ArrayList<>();
    private static final int COLUMNS = 10; // Número de columnas de la matriz
    private static final int POINTS_PER_COLUMN = 8; // Número de puntos por columna
    private static final float ROW_SPACING = 1.0f; // Espaciado vertical
    private static final float HORIZON_Z = 5.0f; // Coordenada Z del horizonte

    public Scene(){
        generateColumns();
    }

    private void generateColumns() {
        float x = 0.0f; // Coordenada fija X
        float y = -0.3f; // Coordenada fija Y
        float z = HORIZON_Z; // Coordenada inicial Z

        // Crear puntos en una única posición inicial
        for (int i = 0; i < POINTS_PER_COLUMN; i++) {
            //points.add(new Point(x, y, z, 0.05f)); // Velocidad fija
            //z -= ROW_SPACING; // Espaciado entre puntos
            points.add(new Point(0, 0.75f, 0.0f, 0.02f));
        }
    }

    public void updateScene(){
        //Log.d("Scene", "Points count: "+points.size());
        // Update points position
        for(Point p : points){
            p.updatePos();
            //Log.d("Scene", "Updated point: X=" + p.getX() + ", Y=" + p.getY() + ", Z=" + p.getZ());
            if(p.isOffScreen()){
                Log.d("Scene", "Deleted point: Z = " + p.getZ());
                p.setZ(HORIZON_Z); // Delete point when reaching cam
            }
        }
    }

    public void draw(GL10 gl){
        for(Point p : points){
            p.draw(gl);
        }
    }
}
