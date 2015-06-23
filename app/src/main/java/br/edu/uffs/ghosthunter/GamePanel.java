package br.edu.uffs.ghosthunter;

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
    private ArrayList<Ghost> ghosts;
    private long spellsStartTime;
    private Random random = new Random();

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

        this.background = new Background(Bitmap.createScaledBitmap(tmpBitmap, this.getWidth(), this.getHeight(), true));

        tmpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wand);
        int newX = tmpBitmap.getWidth() * this.getWidth() / tmpBitmap.getWidth() / 8;
        int newY = tmpBitmap.getHeight() * this.getHeight() / tmpBitmap.getHeight() / 10;
        this.player = new Player(Bitmap.createScaledBitmap(tmpBitmap, newX, newY, true), (this.getWidth() / 12), (this.getHeight() / 2), 0, 0);

        this.ghosts = new ArrayList<>();
        spellsStartTime = System.nanoTime();

        this.mainThread.setRunning(true);
        this.mainThread.start();

        tmpBitmap = null;
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

            if (this.ghosts.size() == 0) {
                // get his missiles
                ghosts.add(new Ghost(BitmapFactory.decodeResource(getResources(), R.drawable.char_phantom), this.getWidth() + 10, this.getHeight() / 2, 45, 15, player.getScore()));
            }
            else {
                ghosts.add(new Ghost(BitmapFactory.decodeResource(getResources(), R.drawable.char_phantom), this.getWidth() + 10, (int) (random.nextDouble() * this.getHeight()), 45, 15, player.getScore()));
            }

            this.spellsStartTime = System.nanoTime();
        }

        for (int i = 0; i < ghosts.size(); i++) {
            ghosts.get(i).update();

            if (this.collision(ghosts.get(i), this.player)) {
                ghosts.remove(i);
                player.setPlaying(false);
                break;
            }

            if (ghosts.get(i).getX() < -100) {
                ghosts.remove(i);
            }
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        return Rect.intersects(a.getRect(), b.getRect());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        this.background.draw(canvas);
        this.player.draw(canvas);

        for (Ghost ghost : this.ghosts) {
            ghost.draw(canvas);
        }
    }
}
