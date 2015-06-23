package br.edu.uffs.ggabriel96.arrow;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
    private Bitmap image;
    private int x, y;
//    private int dx;

    public Background(Bitmap image) {
        this.image = image;
        this.x = this.y = 0;
//        this.dx = GamePanel.MOVESPEED;
    }

    public int getWidth() {
        return this.image.getWidth();
    }

    public int getHeight() {
        return this.image.getHeight();
    }

    public void update() {
//        scrolling
//        this.x += this.dx;
//        if (this.x + this.getWidth() < 0 || this.x > this.getWidth()) {
//            this.x = 0;
//        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.image, this.x, this.y, null);
//        for occupying empty space when scrolling
//        if (this.x < 0) {
//            canvas.drawBitmap(image, this.x + this.getWidth(), this.y, null);
//        }
    }
}
