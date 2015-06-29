package br.edu.uffs.ghosthunter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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
    private Player player;
    private MainThread mainThread;
    private Background background;
    private ArrayList<Ghost> ghosts;
    private ArrayList<Spell> spells;
    private Random random = new Random();
    private boolean gameOver, instructions;
    private long ghostsStartTime, spellStartTime;
    private Paint livesPaint, scorePaint, finalScorePaint;
    private Typeface tf = Typeface.create("Roboto", Typeface.BOLD);

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
        Bitmap tmpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_main_menu);
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

        this.livesPaint = new Paint();
        this.livesPaint.setColor(Color.RED);
        this.livesPaint.setTextSize(65);
        this.livesPaint.setTypeface(this.tf);

        this.scorePaint = new Paint();
        this.scorePaint.setColor(Color.CYAN);
        this.scorePaint.setTextSize(65);
        this.scorePaint.setTypeface(this.tf);

        this.finalScorePaint = new Paint();
        this.finalScorePaint.setColor(Color.WHITE);
        this.finalScorePaint.setTextSize(130);
        this.finalScorePaint.setTypeface(this.tf);

        tmpBitmap = null;

        this.gameOver = false;
        this.instructions = false;
        this.player.setPlaying(false);
    }

    private void reset() {
        this.background.setImage(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background), this.getWidth(), this.getHeight(), true));
        this.gameOver = false;
        this.instructions = false;
        this.player.reset();
        this.player.setPlaying(true);
        this.ghosts = new ArrayList<>();
        this.spells = new ArrayList<>();
        this.ghostsStartTime = System.nanoTime();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (this.player.isPlaying()) {
                this.player.update(event);
            }

            return true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!this.player.isPlaying()) {
                if (this.gameOver || this.instructions) {
                    this.background.setImage(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu_main_menu), this.getWidth(), this.getHeight(), true));
                    this.gameOver = false;
                    this.instructions = false;
                    this.player.setPlaying(false);
                }
                else {
                    if (event.getAxisValue(MotionEvent.AXIS_X) >= this.getWidth() / 2 - 190 && event.getAxisValue(MotionEvent.AXIS_X) <= this.getWidth() / 2 + 220 && event.getAxisValue(MotionEvent.AXIS_Y) >= this.getHeight() / 2 - 65 && event.getAxisValue(MotionEvent.AXIS_Y) <= this.getHeight() / 2) {
                        // play
                        this.reset();
                    }
                    else if (event.getAxisValue(MotionEvent.AXIS_X) >= this.getWidth() / 2 - 350 && event.getAxisValue(MotionEvent.AXIS_X) <= this.getWidth() / 2 + 420 && event.getAxisValue(MotionEvent.AXIS_Y) >= this.getHeight() / 2 + 300 - 65 && event.getAxisValue(MotionEvent.AXIS_Y) <= this.getHeight() / 2 + 300) {
                        this.background.setImage(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu_instructs), this.getWidth(), this.getHeight(), true));
                        this.instructions = true;
                    }
                }
            }
            else if ((System.nanoTime() - this.spellStartTime) / 1_000_000 > 1_500) {
                Spell s = new Spell(
                        this.spellBitmap,
                        (int)(this.player.getCenterX() + this.player.getWidth() / 2),
                        (int)(this.player.getCenterY()),
                        this.player.getScore()
                );

                // calculating vectors from weapon to touch
                double tx = event.getAxisValue(MotionEvent.AXIS_X) - s.getCenterX();
                double ty = event.getAxisValue(MotionEvent.AXIS_Y) - s.getCenterY();

                s.setSpeedX(tx);
                s.setSpeedY(ty);
                s.setSpeed(Math.sqrt(tx * tx + ty * ty));

                this.spells.add(s);
                this.spellStartTime = System.nanoTime();
            }

            return true;
        }
        else return super.onTouchEvent(event);
    }

    private void checkAndAddGhost() {
        long ghostsElapsedTime = (System.nanoTime() - this.ghostsStartTime) / 1_000_000;

        if (ghostsElapsedTime > (3_500 - player.getScore() * 50)) {

            ghosts.add(new Ghost(this.ghostBitmap, this.getWidth(), (int) (random.nextDouble() * this.getHeight() % (this.getHeight() - 256)), this.ghostWidth, this.ghostHeight, player.getScore()));

            this.ghostsStartTime = System.nanoTime();
        }
    }

    private void updateGhosts() {
        for (int i = 0; i < this.ghosts.size(); i++) {
            this.ghosts.get(i).update();

            if (this.ghosts.get(i).getX() + this.ghosts.get(i).getWidth() < 0) {
                this.ghosts.remove(i);
                this.player.miss();
            }
        }
    }

    private void updateSpells() {
        for (int i = 0, max = this.spells.size(); i < max; i++) {
            Spell spell = this.spells.get(i);
            spell.update();

            if (this.outOfScreen(spell)) {
                this.spells.remove(i);
                continue;
            }
        }
    }

    public void checkCollision() {
        for (int i = 0, imax = this.spells.size(); i < imax; i++) {
            for (int j = 0, jmax = this.ghosts.size(); i < jmax; j++) {
                if (this.collision(this.spells.get(i), this.ghosts.get(j))) {
                    this.spells.remove(i);
                    this.ghosts.remove(j);

                    this.player.hit();

                    break;
                }
            }
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        return Rect.intersects(a.getRect(), b.getRect());
    }

    public void update() {
        if (this.player.isPlaying() && !this.gameOver) {
            if (this.player.getLives() <= 0) {
                this.background.setImage(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lvl3_ruins), this.getWidth(), this.getHeight(), true));
                this.player.setPlaying(false);
                this.gameOver = true;
            }
            else {
                this.updateGhosts();
                this.updateSpells();
                this.checkAndAddGhost();
            }
        }
    }

    public boolean outOfScreen(GameObject o) {
        if (o.getX() + o.getWidth() < 0 || o.getY() + o.getHeight() < 0 || o.getX() > this.getWidth() || o.getY() > this.getHeight()) {
            return true;
        }
        else return false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (this.player.isPlaying() && !this.gameOver) {
            this.background.draw(canvas);

            for (Ghost ghost : this.ghosts) {
                ghost.draw(canvas);
            }

            this.player.draw(canvas);

            for (Spell spell : this.spells) {
                spell.draw(canvas);
            }

            canvas.drawText("Vidas: " + this.player.getLives(), this.getWidth() / 40, 70, this.livesPaint);
            canvas.drawText("Pontos: " + this.player.getScore(), this.getWidth() - 320, 70, this.scorePaint);
        }
        else if (this.gameOver) {
            this.background.draw(canvas);
            canvas.drawText("PONTUAÇÃO: " + this.player.getScore(), this.getWidth() / 2 - 420, this.getHeight() / 2, this.finalScorePaint);
            canvas.drawText("CLIQUE PARA CONTINUAR", this.getWidth() / 2 - 800, this.getHeight() / 2 + 300, this.finalScorePaint);
        }
        else if (this.instructions) {
            this.background.draw(canvas);
        }
        else {
            this.background.draw(canvas);
            canvas.drawText("Ghost Hunter", this.getWidth() / 2 - 350, this.getHeight() / 5, this.finalScorePaint);
            canvas.drawText("JOGAR", this.getWidth() / 2 - 185, this.getHeight() / 2, this.finalScorePaint);
            canvas.drawText("INSTRUÇÕES", this.getWidth() / 2 - 350, this.getHeight() / 2 + 300, this.finalScorePaint);
        }
    }
}
