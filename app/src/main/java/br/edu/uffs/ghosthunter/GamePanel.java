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

/*******************************************************************************
Name:GamePanel.java
Authors: Gabriel Batista Galli - g7.galli96@gmail.com
         Vladimir Belinski - vlbelinski@gmail.com

Description: Class GamePanel of GameHunter, a 2D game.
*******************************************************************************/


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mainThread;
    private Background background;
    private Player player;
    private ArrayList<Ghost> ghosts;
    private ArrayList<Spell> spells;
    private long spellsStartTime;
    private Random random = new Random();

    private Bitmap spellBitmap, ghostBitmap;
    private int spellWidth, spellHeight, ghostWidth, ghostHeight;

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
        this.player = new Player(Bitmap.createScaledBitmap(tmpBitmap, newX, newY, true), (this.getWidth() / 12), (this.getHeight() / 2));

        this.spellBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.power1_transparent);
        this.spellWidth = spellBitmap.getWidth() * this.getWidth() / spellBitmap.getWidth() / 10;
        this.spellHeight = spellBitmap.getHeight() * this.getHeight() / spellBitmap.getHeight() / 10;
        this.spellBitmap = Bitmap.createScaledBitmap(this.spellBitmap, this.spellWidth, this.spellHeight, true);

        this.ghostBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.char_phantom);
        this.ghostWidth = ghostBitmap.getWidth() * this.getWidth() / ghostBitmap.getWidth() / 7;
        this.ghostHeight = ghostBitmap.getHeight() * this.getHeight() / ghostBitmap.getHeight() / 5;
        this.ghostBitmap = Bitmap.createScaledBitmap(this.ghostBitmap, this.ghostWidth, this.ghostHeight, true);

        this.ghosts = new ArrayList<>();
        this.spells = new ArrayList<>();

        this.mainThread.setRunning(true);
        this.mainThread.start();

        tmpBitmap = null;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (!this.player.isPlaying()) {
            }
            else {
                this.player.update(event);
            }

            return true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!this.player.isPlaying()) {
            }
            else {
                Spell s = new Spell(
                        this.spellBitmap,
                        (int)(this.player.getCenterX() + this.player.getWidth() / 2),
                        (int)(this.player.getY() + this.player.getHeight())
                );

                // calculating vectors from weapon to touch
                double tx = event.getAxisValue(MotionEvent.AXIS_X) - s.getCenterX();
                double ty = event.getAxisValue(MotionEvent.AXIS_Y) - s.getCenterY();

                s.setSpeedX(tx);
                s.setSpeedY(ty);
                s.setSpeed(Math.sqrt(tx * tx + ty * ty));

                this.spells.add(s);
            }

            return true;
        }
        else return super.onTouchEvent(event);
    }

    private void checkAndAddGhost() {
        long ghostsElapsedTime = (System.nanoTime() - this.spellsStartTime) / 1_000_000;

        if (ghostsElapsedTime > (3_500 - player.getScore() * 5)) {

            ghosts.add(new Ghost(this.ghostBitmap, this.getWidth(), (int) (random.nextDouble() * this.getHeight() % (this.getHeight() - 256)), this.ghostWidth, this.ghostHeight, player.getScore()));

            this.spellsStartTime = System.nanoTime();
        }
    }

    private void updateGhosts() {
        for (int i = 0; i < this.ghosts.size(); i++) {
            this.ghosts.get(i).update();

            if (this.ghosts.get(i).getX() + this.ghosts.get(i).getWidth() < 0) {
                this.ghosts.remove(i);
            }
        }
    }

    private void updateSpells() {
        for (int i = 0; i < this.spells.size(); i++) {
            this.spells.get(i).update();

            if (this.outOfScreen(this.spells.get(i))) {
                this.spells.remove(i);
                continue;
            }

//            for (int j = 0; i < this.ghosts.size(); j++) {
//                if (this.collision(this.spells.get(i), this.ghosts.get(j))) {
//                    this.spells.remove(i);
//                    this.ghosts.remove(j);
//
//                    this.player.hit();
//
//                    break;
//                }
//            }
        }
    }

    public void update() {
        if (!this.player.isPlaying()) {
            // if player hits play
            this.player.setPlaying(true);
            this.spellsStartTime = System.nanoTime();
        }
        else {
//            System.out.println("before adding ghost");
            this.checkAndAddGhost();

//            System.out.println("before updating ghosts");
            this.updateGhosts();

//            System.out.println("before updating spells");
            this.updateSpells();
        }
    }

    public boolean outOfScreen(GameObject o) {
        if (o.getX() + o.getWidth() < 0 || o.getY() + o.getHeight() < 0 || o.getX() > this.getWidth() || o.getY() > this.getHeight()) {
            return true;
        }
        else return false;
    }

    public boolean collision(GameObject a, GameObject b) {
        return Rect.intersects(a.getRect(), b.getRect());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        this.background.draw(canvas);

        for (Ghost ghost: this.ghosts) {
            ghost.draw(canvas);
        }

        this.player.draw(canvas);

        for (Spell spell: this.spells) {
            spell.draw(canvas);
        }
    }
}
