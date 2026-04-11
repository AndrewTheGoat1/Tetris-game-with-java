package com.soft.helper;

import com.soft.andrew.entity.TetrominoType;

import java.awt.*;

public class CollisionHelper {

    public static boolean canMove(int[][] collisionData, int x, int y, TetrominoType tetrominoType){
        int[][] tetrominoData = tetrominoType.getTetrominoData();
        for (int j = 0; j < tetrominoData.length; j++){
            for (int i = 0; i < tetrominoData[j].length; i++){

                if (tetrominoData[j][i] == 1){
                    int gridX = (x - 100 + (i * 20)) / 20;
                    int gridY = (y - 100 + (j * 20)) / 20;

                    if (gridX < 0 || gridX >= collisionData[0].length || gridY < 0 || gridY >= collisionData.length){
                        return false;
                    }

                    if (collisionData[gridY][gridX] == 1){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
