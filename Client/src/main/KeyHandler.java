package main;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import static main.Main.DEV_MODE;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        // TITLE STATE
        if(gp.gameState == gp.titleState) {
            if(code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) gp.ui.commandNum = 2;
            }
            if(code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2) gp.ui.commandNum = 0;

            }
            if (code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                }
                else if(gp.ui.commandNum == 1) {
                    // LOAD GAME
                }
                else if(gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }

        else if(gp.gameState == gp.playState) {

            if(code == KeyEvent.VK_UP) {
                upPressed = true;
            }
            else if(code == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            else if(code == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            else if(code == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            else if(code == KeyEvent.VK_P) {
                gp.gameState = gp.pauseState;
            }
            else if (code == KeyEvent.VK_T) {
                DEV_MODE = !DEV_MODE;
            }
        }
        else if(gp.gameState == gp.pauseState) {
            if(code == KeyEvent.VK_P) {
                gp.gameState = gp.playState;
            }
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();


        if(code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }

    }
}
