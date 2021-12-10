package com.mofagames.game.remake.clickrunner;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class AnimationPlayer extends Thread {

    private ImageView imageView;
    private Bitmap bitmap;
    private int interval;

    private boolean running = true;
    private boolean paused = true;
    private int frames;
    private int frame;
    private int frameMin;
    private int frameMax;
    private int width;
    private int height;

    public AnimationPlayer(ImageView imageView, Bitmap bitmap, int interval, int frames, int width, int height) {
        this.imageView = imageView;
        this.bitmap = bitmap;
        this.interval = interval;
        this.frames = frames;
        this.width = width;
        this.height = height;
        this.frameMin = 0;
        this.frameMax = this.frames-1;
        this.frame = this.frameMin;
    }

    public AnimationPlayer(ImageView imageView, Bitmap bitmap, int interval, int frames, int frameMin, int frameMax, int width, int height) {
        this.imageView = imageView;
        this.bitmap = bitmap;
        this.interval = interval;
        this.frames = frames;
        this.width = width;
        this.height = height;
        this.frameMin = frameMin;
        this.frameMax = frameMax;
        this.frame = this.frameMin;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        Bitmap partialBitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth()/frames*frame, 0, bitmap.getWidth()/frames, bitmap.getHeight());
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(partialBitmap, width, height, false);
        imageView.post(new Runnable() {
            public void run() {
                imageView.setImageBitmap(scaledBitmap);
            }
        });
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    public void setFrames(int frames) {
        this.frames = frames;
    }
    public void setFrame(int frame) {
        this.frame = frame;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean getRunning() {
        return this.running;
    }
    public boolean getPaused() {
        return this.running;
    }
    public int getFrame() {
        return this.frame;
    }
    public int getFrames() {
        return this.frames;
    }

    @Override
    public void run() {
        super.run();
        while(this.running) {
            if(!this.paused) {
                if(frame > this.frameMax) { frame = frameMin; }
                if(frame < this.frameMin) { frame = frameMin; }
                Bitmap partialBitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth()/frames*frame, 0, bitmap.getWidth()/frames, bitmap.getHeight());
                final Bitmap scaledBitmap = Bitmap.createScaledBitmap(partialBitmap, width, height, false);
                imageView.post(new Runnable() {
                    public void run() {
                        imageView.setImageBitmap(scaledBitmap);
                    }
                });

                try {
                    Thread.sleep(interval);
                } catch (Exception ignored) {}

                frame++;
            }
        }
    }
}
