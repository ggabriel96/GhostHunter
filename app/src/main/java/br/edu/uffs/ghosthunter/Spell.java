package br.edu.uffs.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/*******************************************************************************
Name:Spell.java
Authors: Gabriel Batista Galli - g7.galli96@gmail.com
         Vladimir Belinski - vlbelinski@gmail.com

Description: Class Spell of GameHunter, a 2D game. This class is responsible
            for the spells used in the game.
*******************************************************************************/

public class Spell extends GameObject {
    private double speed, speedX, speedY, speedMultiplier;

    public Spell(Bitmap image, int x, int y) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();

        this.centerX = x;
        this.centerY = y;
        this.x = x - (this.width / 2);
        this.y = y - (this.height / 2);

        this.speed = 0;
        this.speedMultiplier = 10;

        this.position = new Matrix();
        this.position.postTranslate(this.x, this.y);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.image, this.x, this.y, null);
    }

    public void update() {
        this.centerX += (this.speedX / this.speed) * this.speedMultiplier;
        this.centerY += (this.speedY / this.speed) * this.speedMultiplier;

        this.x += (this.speedX / this.speed) * this.speedMultiplier;
        this.y += (this.speedY / this.speed) * this.speedMultiplier;
//        this.position.postTranslate(this.x, this.y);
    }
}
