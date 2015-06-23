package br.edu.uffs.ghosthunter;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private static final int FPS_CAP = 60;
    private double avgFps;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas; // static?

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

            // try locking the canvas for pixel editing ?
            try {
                MainThread.canvas = this.surfaceHolder.lockCanvas();
//                synchronized (this.surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(MainThread.canvas);
//                }
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

            totalTime += System.nanoTime() - startTime; // millisTime?
            frameCount++;

            if (frameCount == MainThread.FPS_CAP) {
                this.avgFps = 1_000 / ((totalTime / frameCount) / 1_000_000);
                totalTime = frameCount = 0;
//                System.out.println("avgFps: " + this.avgFps);
            }
        }
    }
}
