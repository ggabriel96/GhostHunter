package br.edu.uffs.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import java.util.Random;

/*******************************************************************************
Name: Ghost.java
Authors: Gabriel Batista Galli - g7.galli96@gmail.com
         Vladimir Belinski - vlbelinski@gmail.com

Description: Class Ghost of GameHunter, a 2D game. This class is responsible
            for the features of the character ghost in the game.
*******************************************************************************/

public class Ghost extends GameObject {
    private int speed;
    private Random random = new Random();

    public Ghost(Bitmap image, int x, int y, int width, int height, int score) {
        this.image = image;
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.speed = 10 + score / 3;
        if (this.speed > 50) this.speed = 50;
    }

    public void update() {
        this.x -= this.speed;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.image, this.x, this.y, null);
    }
}
