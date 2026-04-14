package com.soft.helper;

import com.soft.andrew.gameWindows.GameWindow;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener , MouseMotionListener {
    private GameWindow gameWindow;

    public MouseHandler(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // PLAY button area
        if (x >= 200 && x <= 320 && y >= 350 && y <= 400) {
            gameWindow.getTetrisWorld().startGame();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
