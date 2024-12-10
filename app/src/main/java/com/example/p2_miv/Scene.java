package com.example.p2_miv;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

public class Scene {
    private ArrayList<Point> points = new ArrayList<>();
    private static final int COLUMNS = 10; // Número de columnas de la matriz
    private static final int POINTS_PER_COLUMN = 5; // Número de puntos por columna
    private static final float ROW_SPACING = 0.2f; // Espaciado vertical
    private static final float HORIZON_Z = 5.0f; // Coordenada Z del horizonte
    private static final float MARGIN_X = 0.05f;

    public Scene(){
        generateColumns();
    }

    private void generateColumns() {
        for (int column = 0; column < COLUMNS; column++) {
            // Columna inicial en la posición X
            float x = -1.0f + MARGIN_X + column * (2.0f - 2 * MARGIN_X) / (COLUMNS - 1);

            // Generar los puntos dentro de la columna
            for (int i = 0; i < POINTS_PER_COLUMN; i++) {
                // Asignar la posición Z y Y a cada punto
                float z = HORIZON_Z - (i * ROW_SPACING);
                float y = -0.45f - (i * ROW_SPACING); // Fijamos Y para todos los puntos de la columna (puedes hacerlo variable si lo deseas)
                points.add(new Point(x, y, z, 0.05f));  // Velocidad fija
            }
        }
    }

    public void updateScene(){
        Log.d("Scene", "Points count: "+points.size());
        // Update points position
        Iterator<Point> it = points.iterator();
        while(it.hasNext()){
            Point p = it.next();
            p.updatePos();
            Log.d("Scene", "Updated point: X=" + p.getX() + ", Y=" + p.getY() + ", Z=" + p.getZ());
            if(p.isOffScreen()){
                it.remove(); // Delete point when reaching cam
            }
        }

        // Generar una nueva fila de puntos en el horizonte
        if (points.isEmpty() || points.get(points.size() -1).getZ() < HORIZON_Z - ROW_SPACING) {
            generateColumns();
        }
    }
    public void draw(GL10 gl){
        for(Point p : points){
            p.draw(gl);
        }
    }
}
