package com.soft.andrew.gameWindows;

import com.soft.andrew.entity.TetrominoType;
import com.soft.helper.KeyHandler;

import java.awt.*;

import static com.soft.andrew.constants.GameConstants.*;

public class GameWindow implements Runnable {

    private GameFrame gameFrame;
    private GamePanel gamePanel;
    private Thread gameThread;
    private boolean isGameRunning;
    private int fpsTracker = 0;
    private TetrisWorld tetrisWorld;

    public GameWindow(){
        initTetrisWorld();
        gamePanel = new GamePanel(this);
        gameFrame = new GameFrame(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        initGameLoop();
    }

    private void initTetrisWorld(){
        tetrisWorld = new TetrisWorld();
    }

    private void initGameLoop(){
        gameThread = new Thread(this);
        isGameRunning = true;
        gameThread.start();
    }

    @Override
    public void run() {

        double targetFps = 1_000_000_000 / 60;
        long currentTimeForFps = 0;
        long lastTimeForFps = System.nanoTime();

        long currentTimeForFpsTracker = 0;
        long lastTimeForFpsTRacker = System.currentTimeMillis();
        int fps = 0;

    while (isGameRunning){

        currentTimeForFps = System.nanoTime();
        if(currentTimeForFps - lastTimeForFps >= targetFps){
            lastTimeForFps = currentTimeForFps;
            update();
            gamePanel.repaint();
            fps++;
        }

        currentTimeForFpsTracker = System.currentTimeMillis();
        if( currentTimeForFpsTracker - lastTimeForFpsTRacker >= 1_000){
            lastTimeForFpsTRacker = currentTimeForFpsTracker;
            //System.out.println("FPS : " + fps);
            fpsTracker = fps;
            fps = 0;
        }
    }
    }

    public void update(){
        //update game components
        tetrisWorld.update();
    }

    public void render(Graphics graphics){
        // render game ui
        drawFpsTracker(graphics);
        drawGrids(graphics);
        drawNextBlock(graphics);
        drawScorePane(graphics);
        drawHighestScorePane(graphics);
        tetrisWorld.render(graphics);
    }

    public void drawGrids(Graphics graphics) {
        // 10 * 20
        graphics.setColor(Color.WHITE);
        graphics.drawRect(100,100, (GRID_SIZE * GRID_WIDTH), (GRID_SIZE * GRID_HEIGHT));

        for (int i = 0; i < GRID_HEIGHT; i++ ){
            for (int j = 0; j < GRID_WIDTH ; j++){
                //graphics.drawRect(100 + ( j * GRID_SIZE), 100 + (i * GRID_SIZE), GRID_SIZE, GRID_SIZE);
            }
        }

    }

    public void restartGame() {
        tetrisWorld = new TetrisWorld();
    }

    public void drawNextBlock(Graphics graphics){
        graphics.setColor(Color.WHITE);
        graphics.drawRect(450,100, 150, 100);
        graphics.drawString("Next : ", 460, 120);

        TetrominoType next = tetrisWorld.getNextTetrominoType();
        int[][] data = next.getTetrominoData();

        int startX = 480; // position inside box
        int startY = 140;

        for (int y = 0; y < data.length; y++){
            for (int x = 0; x < data[y].length; x++){
                if (data[y][x] == 1){
                    graphics.setColor(next.getColor());
                    graphics.fillRect(startX + (x * 20), startY + (y * 20), 20, 20);

                    graphics.setColor(Color.WHITE);
                    graphics.drawRect(startX + (x * 20), startY + (y * 20), 19, 19);
                }
            }
        }
    }

    public void drawScorePane(Graphics graphics){
        graphics.setColor(Color.WHITE);
        graphics.drawRect(450,250, 150, 100);
        graphics.drawString("Score : ", 460, 270);

        graphics.drawString(String.valueOf(tetrisWorld.getScore()), 460, 300);
    }

    public void drawHighestScorePane(Graphics graphics){
        graphics.setColor(Color.WHITE);
        graphics.drawRect(450,400, 150, 100);
        graphics.drawString("Highest Score : ", 460, 420);
    }

    public void drawFpsTracker(Graphics graphics){
        graphics.setColor(Color.GREEN);
        graphics.drawString("FPS : " + fpsTracker ,15,15);
    }

    public TetrisWorld getTetrisWorld(){
        return tetrisWorld;
    }

}
