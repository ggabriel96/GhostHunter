package br.edu.uffs.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

/*******************************************************************************
Name:GameObject.java
Authors: Gabriel Batista Galli - g7.galli96@gmail.com
         Vladimir Belinski - vlbelinski@gmail.com

Description: Class GameObject of GameHunter, a 2D game.
*******************************************************************************/

public abstract class GameObject {
    Matrix position;
    protected Bitmap image;
    protected int x, y, width, height;
    protected double centerX, centerY;

    public void setX(int x) { this.x = x; }

    public int getX() { return this.x; }

    public double getCenterX() {
        return this.centerX;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() { return this.y; }

    public double getCenterY() { return this.centerY; }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Rect getRect() {
        return new Rect(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight());
    }

    public Matrix rotateImage(float angle) {
        Matrix newPosition = new Matrix();
        newPosition.postRotate(angle, this.width / 2, this.height / 2);
        newPosition.postTranslate(this.x, this.y);
        return newPosition;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.image, this.position, null);
    }
}
