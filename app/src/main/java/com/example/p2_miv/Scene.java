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

    // Scene constructor
    public Scene(){
        generateColumns();
    }

    // Method to generate all points in scene in order to simulate starship progress
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

    // Method to update points cords to simulate forward movement
    public void updateScene(){
        // Update points position
        for(Point p : points){
            p.updatePos();
            if(p.isOffScreen()){
                p.setZ(HORIZON_Z); // Delete point when reaching cam
            }
        }
    }

    // Method to draw all points in scene
    public void draw(GL10 gl){
        for(Point p : points){
            p.draw(gl);
        }
    }
}
