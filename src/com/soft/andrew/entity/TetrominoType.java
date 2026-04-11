package com.soft.andrew.entity;

import java.awt.*;

public enum TetrominoType {

    I(new int[][]{
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    }, Color.BLUE, 0,0,20,40 ) {
        public TetrominoType next() {
            return I1;
        }
    },
    I1(new int[][]{
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0}
    }, Color.BLUE,20,40,0,0) {
        public TetrominoType next() {
            return I;
        }
    },

    O(new int[][]{
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
    }, Color.YELLOW,20,20,20,20){
        public TetrominoType next() {
            return O;
        }
    },

    L(new int[][]{
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
    }, Color.RED,20,20,0,20) {
        public TetrominoType next() {
            return L1;
        }
    },
    L1(new int[][]{
            {0, 0, 0, 0},
            {1, 1, 1, 0},
            {1, 0, 0, 0},
            {0, 0, 0, 0}
    }, Color.RED,0,20,20,20){
        public TetrominoType next() {
            return L2;
        }
    },
    L2(new int[][]{
            {1, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0}
    }, Color.RED,0,40,0,20){
        public TetrominoType next() {
            return L3;
        }
    },
    L3(new int[][]{
            {0, 0, 1, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    }, Color.RED,0,20,0,40){
        public TetrominoType next() {
            return L;
        }
    },

    J(new int[][]{
            {0, 0, 1, 0},
            {0, 0, 1, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
    }, Color.GREEN,20,20,0,20){
        public TetrominoType next() {
            return J1;
        }
    },
    J1(new int[][]{
            {0, 1, 0, 0},
            {0, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    }, Color.GREEN,20,0,0,40){
        public TetrominoType next() {
            return J2;
        }
    },
    J2(new int[][]{
            {0, 0, 1, 1},
            {0, 0, 1, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 0}
    }, Color.GREEN,40,0,0,20){
        public TetrominoType next() {
            return J3;
        }
    },
    J3(new int[][]{
            {0, 0, 0, 0},
            {0, 1, 1, 1},
            {0, 0, 0, 1},
            {0, 0, 0, 0}
    }, Color.GREEN,20,0,20,20){
        public TetrominoType next() {
            return J;
        }
    },

    T(new int[][]{
            {0, 0, 0, 0},
            {1, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0}
    }, Color.PINK,0,20,20,20){
        public TetrominoType next() {
            return T1;
        }
    },
    T1(new int[][]{
            {0, 1, 0, 0},
            {1, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0}
    }, Color.PINK,0,40,0,20){
        public TetrominoType next() {
            return T2;
        }
    },
    T2(new int[][]{
            {0, 1, 0, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    }, Color.PINK,0,20,0,40){
        public TetrominoType next() {
            return T3;
        }
    },
    T3(new int[][]{
            {0, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0}
    }, Color.PINK,20,20,0,20){
        public TetrominoType next() {
            return T;
        }
    },

    Z(new int[][]{
            {0, 0, 0, 0},
            {1, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
    }, Color.ORANGE,0,20,20,20){
        public TetrominoType next() {
            return Z1;
        }
    },
    Z1(new int[][]{
            {0, 1, 0, 0},
            {1, 1, 0, 0},
            {1, 0, 0, 0},
            {0, 0, 0, 0}
    }, Color.ORANGE,0,40,0,20){
        public TetrominoType next() {
            return Z2;
        }
    },
    Z2(new int[][]{
            {1, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    }, Color.ORANGE,0,20,0,40){
        public TetrominoType next() {
            return Z3;
        }
    },
    Z3(new int[][]{
            {0, 0, 1, 0},
            {0, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0}
    }, Color.ORANGE,20,20,0,20){
        public TetrominoType next() {
            return Z;
        }
    },

    ZR(new int[][]{
            {0, 0, 0, 0},
            {0, 0, 1, 1},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
    }, Color.CYAN,20,0,20,20){
        public TetrominoType next() {
            return ZR1;
        }
    },
    ZR1(new int[][]{
            {0, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 0}
    }, Color.CYAN,20,20,0,20){
        public TetrominoType next() {
            return ZR2;
        }
    },
    ZR2(new int[][]{
            {0, 0, 1, 1},
            {0, 1, 1, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    }, Color.CYAN,20,0,0,40){
        public TetrominoType next() {
            return ZR3;
        }
    },
    ZR3(new int[][]{
            {0, 0, 1, 0},
            {0, 0, 1, 1},
            {0, 0, 0, 1},
            {0, 0, 0, 0}
    }, Color.CYAN,40,0,0,20){
        public TetrominoType next() {
            return ZR;
        }
    };

    private final int[][] tetrominoData;
    private final Color color;
    private final int offSetXL,offSetXR,offSetYU,offSetYD;
    public  abstract TetrominoType next();

    TetrominoType(int[][] tetrominoData, Color color, int offSetXL, int offSetXR, int offSetYU, int offSetYD){
        this.tetrominoData = tetrominoData;
        this.color = color;
        this.offSetXL = offSetXL;
        this.offSetXR = offSetXR;
        this.offSetYU = offSetYU;
        this.offSetYD = offSetYD;
    }

    public int[][] getTetrominoData() {
        return tetrominoData;
    }

    public Color getColor() {
        return color;
    }

    public int getOffSetXL() {
        return offSetXL;
    }

    public int getOffSetXR() {
        return offSetXR;
    }

    public int getOffSetYU() {
        return offSetYU;
    }

    public int getOffSetYD() {
        return offSetYD;
    }
}
