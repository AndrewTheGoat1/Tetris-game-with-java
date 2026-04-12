package com.soft.andrew.gameWindows;

import com.soft.andrew.entity.Tetromino;
import com.soft.andrew.entity.TetrominoType;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TetrisWorld {
    private List<Tetromino> tetrominoList;
    private int[][] collisionData;
    private Color[][] colorData;
    private Random random;
    private boolean isGameOver = false;
    private boolean isPaused = false;
    private TetrominoType nextTetrominoType;

    private boolean isTimeToAnimate;
    private Map<Integer, Color[]> animationColors;
    private int animationBlockSize = 20;
    private int score = 0;

    public TetrisWorld() {
        random = new Random();
        tetrominoList = new ArrayList<>();
        collisionData = new int[30][15];
        colorData = new Color[30][15];
        animationColors = new HashMap<>();

        nextTetrominoType = generateRandomTetromino();

        Tetromino tetromino3 = new Tetromino(300, 200, TetrominoType.I, true, this);
        tetrominoList.add(tetromino3);
    }

    public void update() {
        if (isGameOver) return;
        if (isPaused) return;

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

                // 3. game over check
                if (!com.soft.helper.CollisionHelper.canMove(collisionData, 260, 100, currentType)) {
                    isGameOver = true;
                    return;
                }

                // 4. spawn current piece
                tetrominoList.add(new Tetromino(260, 100, currentType, true, this));
            }

            int removedRows = checkAndRemoveFullRows();
            if (removedRows > 0) {
                addScore(removedRows);
                isTimeToAnimate = true;
            }
        }
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
        for (Tetromino t : tetrominoList) {
            t.render(g);
        }

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

        if (isTimeToAnimate) {
            //System.out.println("Is Time to animate");

            for (Map.Entry<Integer, Color[]> entry : animationColors.entrySet()) {
                Integer key = entry.getKey();
                //System.out.println("Key : " + key);
                for (int x = 0; x < entry.getValue().length; x++) {
                    //System.out.println("X :" + x + "Color : " + entry.getValue()[x]);
                    g.setColor(entry.getValue()[x]);
                    g.fillRect(100 + x * 20, key * 20 + 100, animationBlockSize, animationBlockSize);
                }
            }

            animationBlockSize--;
            if (animationBlockSize <= 0) {
                for (Integer key : animationColors.keySet()) {
                    shiftRowDown(key);
                }
                animationColors = new HashMap<>();
                isTimeToAnimate = false;
                animationBlockSize = 20;
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
