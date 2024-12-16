package com.example.p2_miv;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private GameRenderer myGameRenderer;
    private boolean leftMove = false;
    private boolean rightMove = false;
    private boolean upMove = false;
    private boolean downMove = false;
    public boolean camUp = false;
    public boolean restartCam = false;
    private GLSurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Quit title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create OpenGL view and set renderer
        view = new GLSurfaceView(this);
        view.setRenderer(myGameRenderer = new GameRenderer(this));
        setContentView(view);

        // Allow keyboard interaction
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        // Enable immservide mode in order to only see the game
        immersiveMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }

    public void immersiveMode(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Capture keys
        if(keyCode == KeyEvent.KEYCODE_A){
            leftMove = true;
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_D){
            rightMove = true;
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_W){
            upMove = true;
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_S){
            downMove = true;
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_P){
            camUp = true;
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_R){
            restartCam = true;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // Free previus captured key
        if(keyCode == KeyEvent.KEYCODE_A){
            leftMove = false;
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_D){
            rightMove = false;
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_W){
            upMove = false;
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_S){
            downMove = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean isLeftMove() {
        return leftMove;
    }

    public boolean isRightMove() {
        return rightMove;
    }

    public boolean isUpMove() { return upMove; }

    public boolean isDownMove() { return downMove; }

    public boolean isCamUp() { return camUp; }

    public boolean isRestartCam() { return restartCam; }
}
