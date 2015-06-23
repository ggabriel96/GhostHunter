package br.edu.uffs.ghosthunter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Smoke extends GameObject {
    public int radius;

    public Smoke(int x, int y) {
        this.radius = 5;
        this.x = x;
        this.y = y;
    }

    public void update() {
        this.x -= 10;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(this.x - this.radius + 2, this.y - this.radius + 2, this.radius, paint);
        canvas.drawCircle(this.x - this.radius, this.y - this.radius, this.radius, paint);
        canvas.drawCircle(this.x - this.radius - 2, this.y - this.radius - 2, this.radius, paint);
    }
}
