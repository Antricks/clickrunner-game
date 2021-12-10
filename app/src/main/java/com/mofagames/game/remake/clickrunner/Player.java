package com.mofagames.game.remake.clickrunner;


import android.graphics.Bitmap;
import android.widget.ImageView;

import static com.mofagames.game.remake.clickrunner.GameActivity.METER;
import static com.mofagames.game.remake.clickrunner.GameActivity.STANDARD_Y;

class Player {
    public boolean animated = false;
    private Bitmap bitmap;
    private ImageView imageView;
    public int run_meters;
    private int frame = 2;

    Player(ImageView imageView, Bitmap bitmap) {
        this.imageView = imageView;
        this.bitmap = bitmap;
        this.run_meters = 0;
        animated = false;
        System.out.print("bitmap width: ");
        System.out.print(bitmap.getWidth());
        System.out.print(" | height: ");
        System.out.println(bitmap.getHeight());
        update();
    }

    void onClick() {
        this.run_meters++;
        this.frame = (this.frame+1)%2+2;
    }

    boolean update() {
        this.imageView.setY(STANDARD_Y-run_meters*METER*2+GameActivity.scroll);
        if(animated) setAnimationFrame(frame);
        return run_meters > GameActivity.GOAL_DISTANCE;
    }

    void setAnimationFrame(int frame) {
        Bitmap partialBitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth()/4*frame, 0, bitmap.getHeight(),bitmap.getHeight());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(partialBitmap, GameActivity.screenSize.x/2, GameActivity.screenSize.x/2, false);
        this.imageView.setImageBitmap(scaledBitmap);
    }
}