package com.mofagames.game.remake.clickrunner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    public static int RUNWAY_STANDARD_Y;
    public static int METER = 100;
    public static int STANDARD_Y;
    public static int GOAL_DISTANCE = 20;
    public static int runway_size_y;
    public static int scroll = 0;
    public static Point screenSize;
    public static boolean paused;

    private AnimationPlayer gameOverAnimationPlayer;
    private Button retryButton;
    private ConstraintLayout gameOverScreen;
    private TextView gameOverText;
    public static ImageView gameOverImage;
    private ImageView goal;
    private ImageView runway_left;
    private ImageView runway_right;
    private ImageView player1_img;
    private ImageView player2_img;
    private Player player1;
    private Player player2;

    private Bitmap runner1_win_bmp;
    private Bitmap runner2_win_bmp;
    private Bitmap runner1_bmp;
    private Bitmap runner2_bmp;

    AnimationPlayer runner1_idle;
    AnimationPlayer runner2_idle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.layout_game);

        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);

        runway_size_y = 2*screenSize.x;
        STANDARD_Y = screenSize.y/2;
        METER = screenSize.y/40;
        RUNWAY_STANDARD_Y = runway_size_y/2;

        runner1_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.runner1);
        runner2_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.runner2);
        runner1_win_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.runner1_win);
        runner2_win_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.runner2_win);

        gameOverScreen = findViewById(R.id.game_over_screen);
        gameOverText = findViewById(R.id.game_over_text);
        gameOverImage = findViewById(R.id.game_over_image);
        retryButton = findViewById(R.id.retry_button);

        gameOverAnimationPlayer = new AnimationPlayer(gameOverImage, runner1_win_bmp, 500, 2, screenSize.x/2, screenSize.x/2);
        gameOverAnimationPlayer.start();
        gameOverAnimationPlayer.setPaused(true);

        goal = findViewById(R.id.goal);
        runway_left = findViewById(R.id.runway1);
        runway_right = findViewById(R.id.runway2);
        player1_img = findViewById(R.id.player1_img);
        player2_img = findViewById(R.id.player2_img);

        player1 = new Player(player1_img, runner1_bmp);
        player2 = new Player(player2_img, runner2_bmp);

        runner1_idle = new AnimationPlayer(player1_img, runner1_bmp, 1000, 4,0,1,screenSize.x/2,screenSize.x/2);
        runner2_idle = new AnimationPlayer(player2_img, runner2_bmp, 1000, 4,0,1,screenSize.x/2,screenSize.x/2);
        runner1_idle.start();
        runner2_idle.start();
        runner1_idle.setPaused(false);
        runner2_idle.setPaused(false);

        update();
        goal.setY(-screenSize.y);

        runway_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!paused) {
                    runner1_idle.setPaused(true);
                    player1.animated = true;
                    player1.onClick();
                    generalOnClick();
                }
            }
        });
        runway_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!paused) {
                    runner2_idle.setPaused(true);
                    player2.animated = true;
                    player2.onClick();
                    generalOnClick();
                }
            }
        });
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("retryBtn");
            }
        });
    }

    private void generalOnClick() {
        if(!paused) {
            goal.setVisibility(View.VISIBLE);
            scroll+=METER;
            update();
        }
    }

    public void update() {
        if(player1.update()) {gameOver(1);}
        if(player2.update()) {gameOver(2);}
        runway_update();
    }

    public void gameOver(int id) {
        Thread gameOverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (Exception ignored) {}
                retryButton.post(new Runnable() {
                    @Override
                    public void run() {
                        retryButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gameOverAnimationPlayer.setPaused(true);
                                gameOverScreen.setVisibility(View.GONE);
                                player1_img.setVisibility(View.VISIBLE);
                                player2_img.setVisibility(View.VISIBLE);
                                scroll = 0;
                                player1.run_meters = 0;
                                player2.run_meters = 0;
                                player1.animated = false;
                                player2.animated = false;
                                runner1_idle.setPaused(false);
                                runner2_idle.setPaused(false);
                                update();
                                paused = false;

                                retryButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        gameOverThread.start();

        paused = true;

        gameOverAnimationPlayer.setPaused(false);

        player1_img.setVisibility(View.INVISIBLE);
        player2_img.setVisibility(View.INVISIBLE);
        gameOverScreen.setVisibility(View.VISIBLE);
        if(id==1) {
            gameOverText.setText("RED WINS!");
            gameOverText.setTextColor(Color.RED);
            gameOverImage.setImageResource(R.drawable.runner1);
            gameOverAnimationPlayer.setBitmap(runner1_win_bmp);
        } else if(id==2) {
            gameOverText.setText("BLUE WINS!");
            gameOverText.setTextColor(Color.BLUE);
            gameOverImage.setImageResource(R.drawable.runner2);
            gameOverAnimationPlayer.setBitmap(runner2_win_bmp);
        }
    }

    private void runway_update() {
        int runway_scroll = RUNWAY_STANDARD_Y-(scroll%runway_size_y);
        runway_right.setScrollY(runway_scroll);
        runway_left.setScrollY(runway_scroll);
        goal.setY(-(GOAL_DISTANCE*METER)+scroll);
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
        gameOverAnimationPlayer.setPaused(true);
        gameOverScreen.setVisibility(View.GONE);
        player1_img.setVisibility(View.VISIBLE);
        player2_img.setVisibility(View.VISIBLE);
        scroll = 0;
        player1.run_meters = 0;
        player2.run_meters = 0;
        player1.animated = false;
        player2.animated = false;
        runner1_idle.setPaused(false);
        runner2_idle.setPaused(false);
        update();
        goal.setVisibility(View.INVISIBLE);
        paused = false;
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        hideSystemUI();
        gameOverAnimationPlayer.setPaused(true);
        gameOverScreen.setVisibility(View.GONE);
        player1_img.setVisibility(View.VISIBLE);
        player2_img.setVisibility(View.VISIBLE);
        scroll = 0;
        player1.run_meters = 0;
        player2.run_meters = 0;
        player1.animated = false;
        player2.animated = false;
        runner1_idle.setPaused(false);
        runner2_idle.setPaused(false);
        update();
        goal.setVisibility(View.INVISIBLE);
        paused = false;
    }
}
