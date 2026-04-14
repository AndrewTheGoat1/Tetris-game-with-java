package com.soft.andrew.entity;

import com.soft.andrew.gameWindows.TetrisWorld;

import java.awt.*;

import static com.soft.helper.CollisionHelper.canMove;

public class Tetromino {
    private int x,y;
    private int blockWidth = 20;
    private int blockHeight = 20;
    private TetrominoType tetrominoType;
    private TetrisWorld tetrisWorld;
    private int downSpeed = 20;
    private int updateCount;
    private int leftRightSpeed = 1;
    private int leftRightUpdateCount;
    private boolean isActive;
    private boolean left,right,up,down;
    private boolean isHardDropping = false;

    public Tetromino(int x, int y, TetrominoType tetrominoType, boolean isActive, TetrisWorld tetrisWorld){
        this.tetrisWorld = tetrisWorld;
        this.x = x;
        this.y = y;
        this.tetrominoType = tetrominoType;
        this.isActive = isActive;
    }

    public void update(){

        if (leftRightUpdateCount > leftRightSpeed){
            move();
            leftRightUpdateCount=0;
        }else {
            leftRightUpdateCount++;
        }

        int speed = isHardDropping ? 2 : downSpeed;

        if (updateCount >= speed) {

            if (canMove(tetrisWorld.getCollisionData(), x, y + 20, tetrominoType)) {
                y += 20;
            } else {
                isActive = false;
                isHardDropping = false;
            }

            updateCount = 0;
        } else {
            updateCount++;
        }

        checkBoundaries();
    }

    private void checkBoundaries(){
        //check left boundary
        if (x < 100 - tetrominoType.getOffSetXL()){
            x = 100 - tetrominoType.getOffSetXL();
        }
        //check right boundary
        if ((x+ (blockWidth * 4)) - tetrominoType.getOffSetXR() > 400){
            x = (400 - (blockWidth * 4)) + tetrominoType.getOffSetXR();
        }
        //check down boundary
        if (( y + ( blockHeight * 4 )) - tetrominoType.getOffSetYD() > 700){
            y = (700 - (blockHeight * 4) + tetrominoType.getOffSetYD());
            isActive = false;
        }
    }

    private void move(){
        if (left && !right) {
            if (canMoveLeft()){
                x -= 20;
            }else if (!canMoveRight()) {
                isActive = false;
            }
        }

        if (right && !left) {
            if (canMoveRight()){
                x += 20;
            }else if (!canMoveLeft()) {
                isActive = false;
            }
        }

        if (down && !up){
            if (canMoveDown()){
                y += 20;
            }else {
                isActive = false;
            }
        }
    }

    private boolean canMoveRight(){
        return canMove(tetrisWorld.getCollisionData(), x + 20, y, tetrominoType);
    }

    private boolean canMoveLeft(){
        return canMove(tetrisWorld.getCollisionData(), x - 20, y, tetrominoType);
    }

    private boolean canMoveDown(){
        return canMove(tetrisWorld.getCollisionData(), x, y + 20, tetrominoType);
    }

    public void switchShape(){
        TetrominoType nextType = tetrominoType.next();
        if(canMove(tetrisWorld.getCollisionData(),x,y,tetrominoType)){
            tetrominoType = nextType;
        }
        //tetrominoType = tetrominoType.next();
    }

    //Ghost Piece
    public int getGhostY() {
        int ghostY = y;

        while (canMove(tetrisWorld.getCollisionData(), x, ghostY + 20, tetrominoType)) {
            ghostY += 20;
        }

        return ghostY;
    }

    public void setDownSpeed(int downSpeed) {
        this.downSpeed = downSpeed;
    }

    public void setHardDropping(boolean hardDropping) {
        this.isHardDropping = hardDropping;
    }

    public void render(Graphics g){
        if (!isActive) return;
        Graphics2D g2 = (Graphics2D) g;
        int[][] tetrominoData = tetrominoType.getTetrominoData();
        // Draw GHOST first
        int ghostY = getGhostY();


        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        for (int deltaY = 0; deltaY < tetrominoData.length; deltaY++){
            for (int deltaX = 0; deltaX < tetrominoData[deltaY].length; deltaX++){
                if (tetrominoData[deltaY][deltaX] == 1){
                    g2.setColor(tetrominoType.getColor());
                    g2.fillRect(
                            x + (deltaX * blockWidth),
                            ghostY + (deltaY * blockHeight),
                            blockWidth,
                            blockHeight
                    );
                }
            }
        }
        // restore opacity
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // 🧱 2. DRAW REAL PIECE (NORMAL)
        for (int deltaY = 0; deltaY < tetrominoData.length ; deltaY++){
            for (int deltaX = 0; deltaX < tetrominoData[deltaY].length ; deltaX++){
                if (tetrominoData[deltaY][deltaX] == 1){
                    g.setColor(tetrominoType.getColor());
                    g.fillRect(
                            x + (deltaX * blockWidth),
                            y + (deltaY * blockHeight),
                            blockWidth,
                            blockHeight
                    );

                    g.setColor(Color.WHITE);
                    g.drawRect(
                            x + (deltaX * blockWidth),
                            y + (deltaY * blockHeight),
                            blockWidth - 1,
                            blockHeight - 1
                    );
                }
            }
        }
    }

    //hard drop
    public void setY(int y) {
        this.y = y;
    }
    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isActive(){
        return isActive;
    }

    public TetrominoType getTetrominoType() {
        return tetrominoType;
    }

    public int[][] getTetrominoData() {
        return tetrominoType.getTetrominoData();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
