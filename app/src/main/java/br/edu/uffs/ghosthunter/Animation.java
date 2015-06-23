package br.edu.uffs.ggabriel96.arrow;

import android.graphics.Bitmap;

public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime, delay;
    private boolean playedOnce;

    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
        this.currentFrame = 0;
        this.startTime = System.nanoTime();
    }

    public void setDelay(long delay) { this.delay = delay; }
    public void setFrame(int frame) { this.currentFrame = frame; }

    public void update() {
        long elapsed = (System.nanoTime() - this.startTime) / 1_000_000;

        if (elapsed > this.delay) {
            this.currentFrame++;
            this.startTime = System.nanoTime();
        }
        if (this.currentFrame == this.frames.length){
            this.currentFrame = 0;
            this.playedOnce = true;
        }
    }

    public Bitmap getImage() {
        return this.frames[this.currentFrame];
    }

    public int getFrame() { return this.currentFrame; }
    public boolean playedOnce() { return this.playedOnce; }
}