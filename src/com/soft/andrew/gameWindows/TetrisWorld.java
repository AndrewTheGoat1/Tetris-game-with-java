package com.soft.andrew.gameWindows;

import com.soft.andrew.entity.Tetromino;
import com.soft.andrew.entity.TetrominoType;
import com.soft.helper.SoundPlayer;

import java.awt.*;
import java.util.*;
import java.io.*; // for highest high score save file
import java.util.List;

public class TetrisWorld {
    private enum GameState {
        MENU,
        PLAYING,
        GAME_OVER
    }

    private GameState gameState = GameState.MENU;
    private List<Tetromino> tetrominoList;
    private int[][] collisionData;
    private Color[][] colorData;
    private Random random;
    private boolean isGameOver = false;
    private boolean isPaused = false;
    private TetrominoType nextTetrominoType;
    private final String FILE_NAME = "highscore.txt";

    private boolean isTimeToAnimate;
    private Map<Integer, Color[]> animationColors;
    private int animationBlockSize = 20;
    private int score = 0;
    private int highScore = 0;
    private int level = 1;
    private int totalLinesCleared = 0;

    //animated line clear
    private int animationFrame = 0;
    private final int MAX_ANIMATION_FRAME = 10;

    public TetrisWorld() {
        random = new Random();
        tetrominoList = new ArrayList<>();
        collisionData = new int[30][15];
        colorData = new Color[30][15];
        animationColors = new HashMap<>();

        loadHighScore();

        nextTetrominoType = generateRandomTetromino();
    }

    public void update() {
        if (gameState != GameState.PLAYING) return;
        if (isGameOver || isPaused) return;

        if (!isTimeToAnimate) {
            Iterator<Tetromino> tetrominoIterator = tetrominoList.iterator();
            while (tetrominoIterator.hasNext()) {
                Tetromino t = tetrominoIterator.next();
                t.update();
                if (!t.isActive()) {
                    addToCollisionData(t.getTetrominoData(), t.getX(), t.getY(), t.getTetrominoType());
                    //isTimeToAnimate = true;
                    tetrominoIterator.remove();
                }
            }
            if (tetrominoList.isEmpty()) {

                // 1. current piece comes from next
                TetrominoType currentType = nextTetrominoType;

                // 2. generate NEW next piece
                nextTetrominoType = generateRandomTetromino();

                // 3. adjust spawn Y using offset
                int spawnX = 260;
                int spawnY = 100 - currentType.getOffSetYU();


                // 4. game over check
                if (isGameOverAtSpawn(currentType, spawnX, spawnY)) {
                    isGameOver = true;
                    SoundPlayer.play("gameover");
                    return;
                }

                // 5. spawn current piece
                tetrominoList.add(new Tetromino(spawnX, spawnY, currentType, true, this));
            }

            int removedRows = checkAndRemoveFullRows();
            if (removedRows > 0) {
                totalLinesCleared += removedRows;   // track lines
                addScore(removedRows);
                updateLevel();
                isTimeToAnimate = true;
            }
        }
    }

    public void startGame() {
        gameState = GameState.PLAYING;
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("TETRIS", 180, 200);

        g.setFont(new Font("Arial", Font.PLAIN, 25));
        g.drawString("Click PLAY to start", 150, 300);

        g.setColor(Color.GREEN);
        g.fillRect(200, 350, 120, 50);

        g.setColor(Color.BLACK);
        g.drawString("PLAY", 230, 385);
    }

    private boolean isGameOverAtSpawn(TetrominoType type, int x, int y) {

        int[][] data = type.getTetrominoData();

        for (int j = 0; j < data.length; j++) {
            for (int i = 0; i < data[j].length; i++) {

                if (data[j][i] == 1) {

                    int gridX = (x - 100 + (i * 20)) / 20;
                    int gridY = (y - 100 + (j * 20)) / 20;

                    //  outside or occupied = game over
                    if (gridX < 0 || gridX >= collisionData[0].length ||
                            gridY < 0 || gridY >= collisionData.length) {
                        return true;
                    }

                    if (collisionData[gridY][gridX] == 1) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void updateLevel() {
        level = (totalLinesCleared / 10) + 1; // every 10 lines = new level
        updateSpeed();
    }

    private void updateSpeed() {
        int newSpeed = Math.max(5, 20 - (level - 1) * 2);

        for (Tetromino t : tetrominoList) {
            t.setDownSpeed(newSpeed);
        }
    }

    public int getLevel() {
        return level;
    }

    public TetrominoType getNextTetrominoType() {
        return nextTetrominoType;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void togglePause() {
        isPaused = !isPaused;
    }

    private int checkAndRemoveFullRows() {
        int removedRows = 0;
        for (int y = collisionData.length - 1; y >= 0; y--) {
            if (isRowFull(y)) {
                //remove row
                removeRow(y);
                //shift row
                //shiftRowDown(y);
                removedRows++;
            }
        }
        return removedRows;
    }

    private void shiftRowDown(int row) {
        for (int y = row; y > 0; y--) {
            System.arraycopy(collisionData[y - 1], 0, collisionData[y], 0, collisionData[y].length);
            System.arraycopy(colorData[y - 1], 0, colorData[y], 0, colorData[y].length);
        }
    }

    private void removeRow(int row) {
        Color[] removedColors = new Color[15];
        System.arraycopy(colorData[row], 0, removedColors, 0, removedColors.length);
        animationColors.put(row, removedColors);
        Arrays.fill(collisionData[row], 0);
        Arrays.fill(colorData[row], null);
    }

    private boolean isRowFull(int row) {
        for (int x : collisionData[row]) {
            if (x != 1) {
                return false;
            }
        }
        return true;
    }

    private void addScore(int rows) {
        switch (rows) {
            case 1 -> score += 100;
            case 2 -> score += 300;
            case 3 -> score += 500;
            case 4 -> score += 800;
        }
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }

    public int getHighScore() {
        return highScore;
    }

    private void saveHighScore() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write(String.valueOf(highScore));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHighScore() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            highScore = Integer.parseInt(br.readLine());
        } catch (Exception e) {
            highScore = 0; // if file not found
        }
    }

    private void addToCollisionData(int[][] tetrominoData, int x, int y, TetrominoType tetrominoType) {

        for (int deltaY = 0; deltaY < tetrominoData.length; deltaY++) {
            for (int deltaX = 0; deltaX < tetrominoData[deltaY].length; deltaX++) {

                if (tetrominoData[deltaY][deltaX] == 1) {
                    //calculate position in collision grid
                    int gridX = (x - 100 + (deltaX * 20)) / 20;
                    int gridY = (y - 100 + (deltaY * 20)) / 20;

                    if (gridX >= 0 && gridX < collisionData[0].length && gridY >= 0 && gridY < collisionData.length) {
                        collisionData[gridY][gridX] = 1;
                        colorData[gridY][gridX] = tetrominoType.getColor();
                    }
                    //System.out.println("GridX : "+ gridX + " GridY : " + gridY);
                }
            }
        }
    }

    private TetrominoType generateRandomTetromino() {
        return switch (getRandomNumber(23)) {
            case 2 -> TetrominoType.I1;
            case 3 -> TetrominoType.O;
            case 4 -> TetrominoType.L;
            case 5 -> TetrominoType.L1;
            case 6 -> TetrominoType.L2;
            case 7 -> TetrominoType.L3;
            case 8 -> TetrominoType.J;
            case 9 -> TetrominoType.J1;
            case 10 -> TetrominoType.J2;
            case 11 -> TetrominoType.J3;
            case 12 -> TetrominoType.T;
            case 13 -> TetrominoType.T1;
            case 14 -> TetrominoType.T2;
            case 15 -> TetrominoType.T3;
            case 16 -> TetrominoType.Z;
            case 17 -> TetrominoType.Z1;
            case 18 -> TetrominoType.Z2;
            case 19 -> TetrominoType.Z3;
            case 20 -> TetrominoType.ZR;
            case 21 -> TetrominoType.ZR1;
            case 22 -> TetrominoType.ZR2;
            case 23 -> TetrominoType.ZR3;
            default -> TetrominoType.I;
        };
    }

    private int getRandomNumber(int bound) {
        return random.nextInt(bound);
    }


    public void render(Graphics g) {
        if (gameState == GameState.MENU) {
            drawMenu(g);
            return;
        }
        // 1. DRAW PLACED BLOCKS FIRST
        for (int deltaY = 0; deltaY < collisionData.length; deltaY++) {
            for (int deltaX = 0; deltaX < collisionData[deltaY].length; deltaX++) {

                if (collisionData[deltaY][deltaX] == 1) {
                    g.setColor(colorData[deltaY][deltaX]);
                    g.fillRect(deltaX * 20 + 100, deltaY * 20 + 100, 20, 20);
                    g.setColor(Color.WHITE);
                    g.drawRect(deltaX * 20 + 100, deltaY * 20 + 100, 19, 19);
                } else {
                    //g.setColor(Color.WHITE);
                    //g.drawString("0", deltaX * 20 + 105, deltaY * 20 + 115);
                }
            }
        }

        // 2. DRAW ACTIVE PIECE (with ghost)
        for (Tetromino t : tetrominoList) {
            t.render(g);
        }
        if (isTimeToAnimate) {
            //System.out.println("Is Time to animate");

            for (Map.Entry<Integer, Color[]> entry : animationColors.entrySet()) {
                Integer key = entry.getKey();
                //System.out.println("Key : " + key);
                for (int x = 0; x < entry.getValue().length; x++) {
                    //System.out.println("X :" + x + "Color : " + entry.getValue()[x]);
                    if (entry.getValue()[x] == null) continue;
                    g.setColor(entry.getValue()[x]);

                    //Animation
                    int drawX = 100 + x * 20;
                    int drawY = key * 20 + 100;

                    // Fade effect
                    int alpha = 255 - (animationFrame * 20);
                    alpha = Math.max(0, alpha);

                    // Flash effect (white blink)
                    if (animationFrame < 3) {
                        g.setColor(Color.WHITE);
                    } else {
                        Color base = entry.getValue()[x];
                        if (base != null) {
                            g.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha));
                        }
                    }

                    // slight shrink effect
                    int size = 20 - animationFrame;
                    size = Math.max(5, size);

                    int offset = (20 - size) / 2;

                    g.fillRect(drawX + offset, drawY + offset, size, size);
                }
            }

            animationFrame++;

            if (animationFrame >= MAX_ANIMATION_FRAME) {
                for (Integer key : animationColors.keySet()) {
                    shiftRowDown(key);
                }
                animationColors.clear();
                isTimeToAnimate = false;
                animationFrame = 0;
            }
        }
        if (isGameOver) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER", 180, 350);
            g.drawString("Press R to Restart", 170, 400);
        }
        if (isPaused) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("PAUSED", 200, 300);
        }
    }

    public void hardDrop() {
        for (Tetromino t : tetrominoList) {
            if (t.isActive()) {
                t.setHardDropping(true);
            }
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setLeft(boolean status) {
        for (Tetromino t : tetrominoList) {
            if (t.isActive()) t.setLeft(status);
        }
    }

    public void setRight(boolean status) {
        for (Tetromino t : tetrominoList) {
            if (t.isActive()) t.setRight(status);
        }
    }

    public void switchShape() {
        for (Tetromino t : tetrominoList) {
            if (t.isActive()) t.switchShape();
        }
    }

    public void setDown(boolean status) {
        for (Tetromino t : tetrominoList) {
            if (t.isActive()) t.setDown(status);
        }
    }

    public int[][] getCollisionData() {
        return collisionData;
    }
}
