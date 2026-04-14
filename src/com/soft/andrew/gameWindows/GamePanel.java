package com.soft.andrew.gameWindows;

import com.soft.helper.KeyHandler;
import com.soft.helper.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.soft.andrew.constants.GameConstants.GAME_SCREEN_HEIGHT;
import static com.soft.andrew.constants.GameConstants.GAME_SCREEN_WIDTH;

public class GamePanel extends JPanel {

    private GameWindow gameWindow;

    public GamePanel(GameWindow gameWindow){
        this.gameWindow = gameWindow;
        setPreferredSize(new Dimension(GAME_SCREEN_WIDTH, GAME_SCREEN_HEIGHT));
        setBackground(new Color(15, 15, 15));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyHandler(gameWindow));
        MouseHandler mouseHandler = new MouseHandler(gameWindow);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameWindow.render(g);
    }


}


