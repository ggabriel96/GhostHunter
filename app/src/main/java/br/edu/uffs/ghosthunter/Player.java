package br.edu.uffs.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

/*******************************************************************************
Name:Player.java
Authors: Gabriel Batista Galli - g7.galli96@gmail.com
         Vladimir Belinski - vlbelinski@gmail.com

Description: Class Player of GameHunter, a 2D game. This class is responsible
            for the features of a player into the game.
*******************************************************************************/

public class Player extends GameObject {
    private int score;
    private boolean playing;

    public Player(Bitmap image, int x, int y) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();

        this.centerX = x;
        this.centerY = y;
        this.x = x - (this.width / 2);
        this.y = y - (this.height / 2);

        this.position = new Matrix();
        this.position.postTranslate(this.x, this.y);

        this.score = 0;
        this.playing = false;
    }

    public int getScore() { return this.score; }

    public void resetScore() { this.score = 0; }

    public void hit() { this.score++; }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isPlaying() { return this.playing; }

    public void update(@NonNull MotionEvent event) {
        // calculating vectors from weapon to touch
        double tx = event.getAxisValue(MotionEvent.AXIS_X) - this.centerX;
        double ty = event.getAxisValue(MotionEvent.AXIS_Y) - this.centerY;

        float angle = (float)Math.toDegrees(Math.atan2(ty, tx));
        this.rotateImage(angle);
    }
}
