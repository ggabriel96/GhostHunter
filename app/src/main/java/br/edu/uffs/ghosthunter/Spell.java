package br.edu.uffs.ggabriel96.arrow;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Spell extends GameObject {
    private int speed, score;
    private Random random = new Random();
    private Animation animation = new Animation();

    public Spell(Bitmap image, int x, int y, int width, int height, int score, int numFrames) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
        this.score = score;

        this.speed = 7 + (int) (random.nextDouble() * this.score / 30);
        if (this.speed > 40) this.speed = 40;

        Bitmap[] images = new Bitmap[numFrames];
        for (int i = 0; i < images.length; i++) {
            images[i] = Bitmap.createBitmap(image, 0, i * this.height, this.width, this.height);
        }

        animation.setFrames(images);
        animation.setDelay(100 - this.speed);
    }

    public void update() {
        this.x -= this.speed;
        this.animation.update();
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(this.animation.getImage(), this.x, this.y, null);
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
