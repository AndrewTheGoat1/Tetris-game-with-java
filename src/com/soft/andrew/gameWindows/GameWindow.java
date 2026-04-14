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

    //UI Color
    private final Color PANEL_BG = new Color(30, 30, 30);
    private final Color PANEL_BORDER = new Color(70, 70, 70);
    private final Color TEXT_MAIN = Color.WHITE;
    private final Color TEXT_ACCENT = new Color(0, 200, 255);

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
        //Smooth Font
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


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
        int x = 450, y = 100;

        drawPanel(g, x, y, 150, 120, "NEXT");

        TetrominoType next = tetrisWorld.getNextTetrominoType();
        int[][] data = next.getTetrominoData();

        int startX = x + 45;
        int startY = y + 35;

        for (int i = 0; i < data.length; i++){
            for (int j = 0; j < data[i].length; j++){
                if (data[i][j] == 1){
                    g.setColor(next.getColor());
                    g.fillRect(startX + j * 18, startY + i * 18, 18, 18);
                }
            }
        }
    }

    public void drawScorePane(Graphics g){
        int x = 450, y = 240;

        drawPanel(g, x, y, 150, 90, "SCORE");

        g.setColor(TEXT_MAIN);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(String.valueOf(tetrisWorld.getScore()), x + 10, y + 60);
    }

    public void drawHighestScorePane(Graphics g){
        int x = 450, y = 350;

        drawPanel(g, x, y, 150, 90, "HIGH SCORE");

        g.setColor(TEXT_MAIN);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(String.valueOf(tetrisWorld.getHighScore()), x + 10, y + 60);
    }

    public void drawLevelPane(Graphics g){
        int x = 450, y = 460;

        drawPanel(g, x, y, 150, 80, "LEVEL");

        g.setColor(TEXT_MAIN);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(String.valueOf(tetrisWorld.getLevel()), x + 10, y + 55);
    }
    public void drawHints(Graphics g){
        int x = 450, y = 560;

        drawPanel(g, x, y, 150, 140, "CONTROLS");

        g.setColor(TEXT_MAIN);
        g.setFont(new Font("Arial", Font.PLAIN, 12));

        g.drawString("← → Move", x + 10, y + 40);
        g.drawString("↑ Rotate", x + 10, y + 55);
        g.drawString("↓ Drop", x + 10, y + 70);
        g.drawString("P Pause", x + 10, y + 85);
        g.drawString("Space Hard drop", x + 10, y + 100);
    }

    public void drawFpsTracker(Graphics graphics){
        graphics.setColor(Color.GREEN);
        graphics.drawString("FPS : " + fpsTracker ,15,15);
    }

    public TetrisWorld getTetrisWorld(){
        return tetrisWorld;
    }

    private void drawPanel(Graphics g, int x, int y, int w, int h, String title) {
        // background
        g.setColor(PANEL_BG);
        g.fillRect(x, y, w, h);

        // border
        g.setColor(PANEL_BORDER);
        g.drawRect(x, y, w, h);

        // title
        g.setColor(TEXT_ACCENT);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(title, x + 10, y + 20);
    }

}
