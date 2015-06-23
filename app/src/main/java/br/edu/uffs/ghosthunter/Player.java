package br.edu.uffs.ggabriel96.arrow;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

public class Player extends GameObject {
    private int score;
    private boolean playing;

    public Player(Bitmap image, int x, int y, int dx, int dy) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();

        this.centerX = x;
        this.centerY = y;
        this.x = x - (this.width / 2);
        this.y = y - (this.height / 2);

        this.position = new Matrix();
        this.position.postTranslate(this.x, this.y);

        this.dx = dx;
        this.dy = dy;
        this.score = 0;
        this.playing = false;
    }

    public int getScore() {
        return this.score;
    }

    public void resetScore() {
        this.score = 0;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public void update(@NonNull MotionEvent event) {
        // calculating vectors from bow to touch
        double tx = event.getAxisValue(MotionEvent.AXIS_X) - this.centerX;
        double ty = event.getAxisValue(MotionEvent.AXIS_Y) - this.centerY;

        float angle = (float)Math.toDegrees(Math.atan2(ty, tx));
        this.rotateImage(angle);
    }
}
