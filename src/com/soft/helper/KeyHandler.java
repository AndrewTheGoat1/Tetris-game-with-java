package com.soft.helper;

import com.soft.andrew.gameWindows.GameWindow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private GameWindow gameWindow;

    public KeyHandler(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT -> gameWindow.getTetrisWorld().setLeft(true);
            case KeyEvent.VK_RIGHT -> gameWindow.getTetrisWorld().setRight(true);
            case KeyEvent.VK_UP -> gameWindow.getTetrisWorld().switchShape();
            case KeyEvent.VK_DOWN -> gameWindow.getTetrisWorld().setDown(true);
            case KeyEvent.VK_R -> {
                if (gameWindow.getTetrisWorld().isGameOver()) {
                    gameWindow.restartGame();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT -> gameWindow.getTetrisWorld().setLeft(false);
            case KeyEvent.VK_RIGHT -> gameWindow.getTetrisWorld().setRight(false);
            case KeyEvent.VK_DOWN -> gameWindow.getTetrisWorld().setDown(false);
        }
    }
}
