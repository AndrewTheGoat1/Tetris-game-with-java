package com.soft.andrew.gameWindows;

import javax.swing.*;

public class GameFrame {

    private JFrame jFrame;

    public GameFrame(GamePanel gamePanel){
        jFrame = new JFrame();
        jFrame.setTitle("Tetris Clone");
        jFrame.add(gamePanel);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // can use 3 as WindowConstants.EXIT_ON_CLOSE
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
    }
}
