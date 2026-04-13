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

    public void render(Graphics g){
        drawFpsTracker(g);
        drawGrids(g);

        drawNextBlock(g);
        drawScorePane(g);
        drawHighestScorePane(g);
        drawLevelPane(g);
        drawHints(g);

        tetrisWorld.render(g);
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

    public void drawNextBlock(Graphics g){
        int x = 450;
        int y = 100;

        g.setColor(Color.WHITE);
        g.drawRect(x, y, 150, 120);
        g.drawString("Next", x + 10, y + 20);

        TetrominoType next = tetrisWorld.getNextTetrominoType();
        int[][] data = next.getTetrominoData();

        int startX = x + 40;
        int startY = y + 30;

        for (int i = 0; i < data.length; i++){
            for (int j = 0; j < data[i].length; j++){
                if (data[i][j] == 1){
                    g.setColor(next.getColor());
                    g.fillRect(startX + j * 20, startY + i * 20, 20, 20);
                    g.setColor(Color.WHITE);
                    g.drawRect(startX + j * 20, startY + i * 20, 19, 19);
                }
            }
        }
    }

    public void drawScorePane(Graphics g){
        int x = 450;
        int y = 240;

        g.setColor(Color.WHITE);
        g.drawRect(x, y, 150, 80);
        g.drawString("Score", x + 10, y + 20);
        g.drawString(String.valueOf(tetrisWorld.getScore()), x + 10, y + 50);
    }

    public void drawHighestScorePane(Graphics g){
        int x = 450;
        int y = 340;

        g.setColor(Color.WHITE);
        g.drawRect(x, y, 150, 80);
        g.drawString("High Score", x + 10, y + 20);
        g.drawString(String.valueOf(tetrisWorld.getHighScore()), x + 10, y + 50);
    }

    public void drawHints(Graphics g){
        int x = 450;
        int y = 540;

        g.setColor(Color.WHITE);
        g.drawRect(x, y, 150, 100);

        g.drawString("Controls", x + 10, y + 20);
        g.drawString("← → Move", x + 10, y + 40);
        g.drawString("↑ Rotate", x + 10, y + 55);
        g.drawString("↓ Drop", x + 10, y + 70);
        g.drawString("P Pause", x + 10, y + 85);
    }

    public void drawLevelPane(Graphics g){
        int x = 450;
        int y = 440;

        g.setColor(Color.WHITE);
        g.drawRect(x, y, 150, 80);
        g.drawString("Level", x + 10, y + 20);
        g.drawString(String.valueOf(tetrisWorld.getLevel()), x + 10, y + 50);
    }

    public void drawFpsTracker(Graphics graphics){
        graphics.setColor(Color.GREEN);
        graphics.drawString("FPS : " + fpsTracker ,15,15);
    }

    public TetrisWorld getTetrisWorld(){
        return tetrisWorld;
    }

}
