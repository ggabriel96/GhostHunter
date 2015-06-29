package br.edu.uffs.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/*******************************************************************************
Name: Background.java
Authors: Gabriel Batista Galli - g7.galli96@gmail.com
         Vladimir Belinski - vlbelinski@gmail.com

Description: Class Background of GameHunter, a 2D game.
*******************************************************************************/

public class Background {
    private Bitmap image;
    private int x, y;

    public Background(Bitmap image) {
        this.image = image;
        this.x = this.y = 0;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.image, this.x, this.y, null);
    }
}
