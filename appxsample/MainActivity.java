package com.xcorp.appxsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xcorp.appx.xApp;

public class MainActivity extends AppCompatActivity {

    public static xApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup window preferences
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // create app
        app = new xApp(this);

        // create panels
        MainPanel mainPanel = new MainPanel(app, this);
        mainPanel.visible = true;
        mainPanel.active = true;

        SubPanel subPanel = new SubPanel(app, this);

        setContentView(app.layout);
    }

    @Override
    protected void onPause() {
        super.onPause();

        app.thread.stopLoop();
    }
}