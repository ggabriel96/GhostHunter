package br.edu.uffs.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public abstract class GameObject {
    Matrix position;
    protected Bitmap image;
    protected int x, y, dx, dy, width, height;
    protected double centerX, centerY;

    public void setX(int x) { this.x = x; }

    public int getX() { return this.x; }

    public double getCenterX() {
        return this.centerX;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public double getCenterY() {
        return this.centerY;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDx() {
        return this.dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDy() {
        return this.dy;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Rect getRect() {
        return new Rect(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight());
    }

    public void rotateImage(float angle) {
        Matrix newPosition = new Matrix();
        newPosition.postRotate(angle, this.width / 2, this.height / 2);
        newPosition.postTranslate(this.x, this.y);
        this.position.set(newPosition);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.image, this.position, null);
    }
}
