package br.edu.uffs.ggabriel96.arrow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mainThread;
    private Background background;
    private Player player;
    private ArrayList<Smoke> smoke;
    private ArrayList<Spell> spells;
    private long spellsStartTime;
    private Random random = new Random();
//    public static final int MOVESPEED = -5; // for scrolling the background...

    public GamePanel(Context context) {
        super(context);

        // add the callback to the SurfaceHolder to intercept events
        getHolder().addCallback(this);

        this.mainThread = new MainThread(getHolder(), this);

        // making GamePanel focusable so it can handle events
        this.setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;

        while (retry && counter < 1_000) {
            try {
                this.mainThread.setRunning(false);
                this.mainThread.join();
                retry = false;
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            counter++;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap tmpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        int newX = tmpBitmap.getWidth() * this.getWidth() / tmpBitmap.getWidth();
        int newY = tmpBitmap.getHeight() * this.getHeight() / tmpBitmap.getHeight();
        this.background = new Background(Bitmap.createScaledBitmap(tmpBitmap, newX, newY, true));

        tmpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.weapon);
        newX = tmpBitmap.getWidth() * this.getWidth() / tmpBitmap.getWidth() / 18;
        newY = tmpBitmap.getHeight() * this.getHeight() / tmpBitmap.getHeight() / 4;
        this.player = new Player(Bitmap.createScaledBitmap(tmpBitmap, newX, newY, true), (this.getWidth() / 12), (this.getHeight() / 2), 0, 0);

        this.smoke = new ArrayList<>();

        this.spells = new ArrayList<>();
        spellsStartTime = System.nanoTime();

        this.mainThread.setRunning(true);
        this.mainThread.start();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            this.player.update(event);

            return true;
        }
        else return super.onTouchEvent(event);
    }

    public void update() {
        long spellsElapsedTime = (System.nanoTime() - this.spellsStartTime) / 1_000_000;

        if (spellsElapsedTime > (2_000 - player.getScore() / 4)) {

            if (this.spells.size() == 0) {
                // get his missiles
                spells.add(new Spell(BitmapFactory.decodeResource(getResources(), R.drawable.missile), this.getWidth() + 10, this.getHeight() / 2, 45, 15, player.getScore(), 13));
            }
            else {
                spells.add(new Spell(BitmapFactory.decodeResource(getResources(), R.drawable.missile), this.getWidth() + 10, (int) (random.nextDouble() * this.getHeight()), 45, 15, player.getScore(), 13));
            }

            this.spellsStartTime = System.nanoTime();
        }

        for (int i = 0; i < spells.size(); i++) {
            spells.get(i).update();

            if (this.collision(spells.get(i), this.player)) {
                spells.remove(i);
                player.setPlaying(false);
                break;
            }

            if (spells.get(i).getX() < -100) {
                spells.remove(i);
            }
        }
//        if (player.isPlaying()) {
//            this.background.update();
//        }
    }

    public boolean collision(GameObject a, GameObject b) {
        return Rect.intersects(a.getRect(), b.getRect());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        this.background.draw(canvas);
        this.player.draw(canvas);

        for (Spell spell: this.spells) {
            spell.draw(canvas);
        }
    }
}
