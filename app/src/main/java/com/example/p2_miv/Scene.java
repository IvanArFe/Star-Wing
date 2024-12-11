package com.example.p2_miv;

import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Scene {
    private ArrayList<Point> points = new ArrayList<>();
    private static final int POINTS_PER_COLUMN = 13; // Número de columnas de la matriz
    private static final int POINTS_PER_ROW = 16; // Número de puntos por columna
    private static final float ROW_SPACING = 1.0f; // Espaciado vertical
    private static final float HORIZON_Z = 5.0f; // Coordenada Z del horizonte

    public Scene(){
        generateColumns();
    }

    private void generateColumns() {
        float x = 15.0f; // Coordenada fija X
        float y = -0.45f; // Coordenada fija Y
        float z = HORIZON_Z; // Coordenada inicial Z

        // Crear puntos en una única posición inicial
        for (int row = 0; row < POINTS_PER_COLUMN; row++) {
            for(int col = 0; col < POINTS_PER_ROW; col++){
                points.add(new Point(x, y, z, 0.08f)); // Velocidad fija
                z += ROW_SPACING; // Espaciado entre puntos
            }
            x -= 2.5f;
            z = HORIZON_Z;
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
