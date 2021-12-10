package com.mofagames.game.remake.clickrunner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Menu extends AppCompatActivity {

    AnimationThread backgroundAnimation;
    ImageView runway_left;
    ImageView runway_right;

    Button play_btn;

    int runway_size_y;
    int RUNWAY_STANDARD_Y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_menu);

        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        runway_size_y = 2*screenSize.x;
        RUNWAY_STANDARD_Y = runway_size_y/2;

        System.out.print("runway_size_y: ");
        System.out.println(runway_size_y);

        runway_left = findViewById(R.id.imageView);
        runway_right = findViewById(R.id.imageView3);

        play_btn = findViewById(R.id.button);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, GameActivity.class);
                startActivity(intent);
            }
        });

        backgroundAnimation = new AnimationThread(runway_left, runway_right, RUNWAY_STANDARD_Y, runway_size_y);
        backgroundAnimation.start();
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        hideSystemUI();
    }
}

class AnimationThread extends Thread {
    boolean running = true;
    boolean paused = false;

    ImageView runway_left;
    ImageView runway_right;

    int scroll;
    int RUNWAY_STANDARD_Y;
    int runway_size_y;

    public AnimationThread(ImageView runway_left, ImageView runway_right, int RUNWAY_STANDARD_Y, int runway_size_y) {
        this.runway_left = runway_left;
        this.runway_right = runway_right;
        this.RUNWAY_STANDARD_Y = RUNWAY_STANDARD_Y;
        this.runway_size_y = runway_size_y;
    }

    @Override
    public void run() {
        super.run();
        while(this.running) {
            if(!this.paused) {
                scroll += 10;
                final int runway_scroll = RUNWAY_STANDARD_Y-(scroll%runway_size_y);
                runway_left.post(new Runnable() {
                    @Override
                    public void run() {
                        runway_left.setScrollY(runway_scroll);
                    }
                });
                runway_right.post(new Runnable() {
                    @Override
                    public void run() {
                        runway_right.setScrollY(runway_scroll);
                    }
                });
                try {
                    Thread.sleep(1000/32);
                } catch (Exception ignored) {}
            }
        }
    }
}
