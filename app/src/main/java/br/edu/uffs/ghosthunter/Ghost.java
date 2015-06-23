package br.edu.uffs.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Ghost extends GameObject {
    private int speed;
    private Random random = new Random();

    public Ghost(Bitmap image, int x, int y, int width, int height, int score) {
        this.image = image;
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.speed = 7 + (int) (random.nextDouble() * score / 30);
        if (this.speed > 40) this.speed = 40;
    }

    public void update() {
        this.x -= this.speed;
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(this.image, this.x, this.y, null);
        }
        catch(Exception e) {
            // ?
        }
    }

    @Override
    public int getWidth() {
        return this.width - 10; // more realistic collision?
    }
}
