package br.edu.uffs.ghosthunter;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/*******************************************************************************
Name: MainThread.java
Authors: Gabriel Batista Galli - g7.galli96@gmail.com
         Vladimir Belinski - vlbelinski@gmail.com

Description: Class MainThread of GameHunter, a 2D game. Responsible for the
            main thread of the game.
*******************************************************************************/

public class MainThread extends Thread {
    private double avgFps;
    private boolean running;
    private GamePanel gamePanel;
    public static Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private static final int FPS_CAP = 60;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
        this.avgFps = 0.0;
        this.running = false;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long startTime, millisTime, waitTime, totalTime = 0, targetTime = 1_000 / MainThread.FPS_CAP;
        int frameCount = 0;

        while (this.running) {
            startTime = System.nanoTime();
            MainThread.canvas = null;

            // try locking the canvas for pixel editing (?)
            try {
                MainThread.canvas = this.surfaceHolder.lockCanvas();
                synchronized (this.surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(MainThread.canvas);
                    this.gamePanel.checkCollision();
                }
            }
            catch (Exception e) {
                // ?
            }
            finally {
                if (MainThread.canvas != null) {
                    try {
                        this.surfaceHolder.unlockCanvasAndPost(MainThread.canvas);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            millisTime = (System.nanoTime() - startTime) / 1_000_000;
            waitTime = targetTime - millisTime;

            try {
                MainThread.sleep(waitTime);
            }
            catch (Exception e) {
                // ?
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if (frameCount == MainThread.FPS_CAP) {
                this.avgFps = 1_000 / ((totalTime / frameCount) / 1_000_000);
                totalTime = frameCount = 0;
//                System.out.println("avgFps: " + this.avgFps);
            }
        }
    }
}
