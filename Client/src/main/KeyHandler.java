package main;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import static main.Main.DEV_MODE;

public class KeyHandler implements KeyListener {

    GamePanel gp;


    public static boolean[] keysPressed = new boolean[999];

    //User defined key bindings
    public static int upKey = KeyEvent.VK_UP;
    public static int downKey = KeyEvent.VK_DOWN;
    public static int leftKey = KeyEvent.VK_LEFT;
    public static int rightKey = KeyEvent.VK_RIGHT;
    public static int backKey = KeyEvent.VK_ESCAPE;
    public static int attackKey = KeyEvent.VK_SPACE;
    public static int confirmKey = KeyEvent.VK_ENTER;
    public static int godKey = KeyEvent.VK_G;
    public static int lightKey = KeyEvent.VK_L;
    public static int pauseKey = KeyEvent.VK_P;

    public static int devKey = KeyEvent.VK_T;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    private void handleKeys() {
        if (gp.gameState == gp.titleState) {
            if (keysPressed[upKey]) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 2;
            }
            if (keysPressed[downKey]) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) gp.ui.commandNum = 0;

            }
            if (keysPressed[confirmKey]) {
                if (gp.ui.commandNum == 0) {
                    gp.new_gameState = gp.playState;
                } else if (gp.ui.commandNum == 1) {
                    gp.new_gameState = gp.joinState;
                    // LOAD GAME
                } else if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }
        // JOIN STATE
        else if (gp.gameState == gp.joinState) {

        }
        //PLAY STATE
        else if (gp.gameState == gp.playState) {

            if (keysPressed[attackKey]) {
                keysPressed[attackKey] = true;
            } else if (keysPressed[pauseKey]) {
                gp.new_gameState = gp.pauseState;
            } else if (keysPressed[devKey]) {
                DEV_MODE = !DEV_MODE;
            } else if (keysPressed[lightKey]) {
                gp.LIGHT = !gp.LIGHT;
            } else if (keysPressed[backKey]) {
                gp.new_gameState = gp.optionsState;
                gp.prev_gameState = gp.playState;
            } else if (keysPressed[godKey]) {
                gp.GOD = !gp.GOD;
                gp.LIGHT = !gp.LIGHT;
            }
        }
        //PAUSE
        else if (gp.gameState == gp.pauseState) {
            if (keysPressed[pauseKey]) {
                gp.new_gameState = gp.playState;
            } else if (keysPressed[backKey]) {
                gp.new_gameState = gp.optionsState;
                gp.prev_gameState = gp.pauseState;
            }
        }
        //READ
        else if (gp.gameState == gp.readState) {
            if (keysPressed[attackKey]) {
                gp.new_gameState = gp.playState;
            }
        }
        //OPTIONS STATE
        else if (gp.gameState == gp.optionsState) {
            if (keysPressed[backKey]) {
                gp.new_gameState = gp.prev_gameState;
                gp.prev_gameState = gp.optionsState;
            }
            if (keysPressed[attackKey]) {
                keysPressed[attackKey] = true;
            }

            int maxcommandNum = 0;
            switch (gp.ui.subState) {
                case 0:
                    maxcommandNum = 5;
                    break;
                case 3:
                    maxcommandNum = 1;
                    break;
            }
            if (keysPressed[upKey]) {
                gp.ui.commandNum--;
                //gp.playSE(9);
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = maxcommandNum;
                }
            }
            if (keysPressed[downKey]) {
                gp.ui.commandNum++;
                //gp.playSE(9);
                if (gp.ui.commandNum > maxcommandNum) {
                    gp.ui.commandNum = 0;
                }
            }
            if (keysPressed[leftKey]) {
                if (gp.ui.subState == 0) {
                    if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                        gp.music.volumeScale--;
                        gp.music.checkVolume();
                        //gp.playSE(9);
                    }
                    /*if(gp.ui.commandNum == 2 && gp.se.volumeScale > 0){
                        gp.se.volumeScale--;
                        gp.playSE(9);
                    }*/
                }
            }

            if (keysPressed[rightKey]) {
                if (gp.ui.subState == 0) {
                    if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                        gp.music.volumeScale++;
                        gp.music.checkVolume();
                        //gp.playSE(9);
                    }
                    /*if(gp.ui.commandNum == 2 && gp.se.volumeScale < 5){
                        gp.se.volumeScale++;
                        gp.playSE(9);
                    }*/
                }
            }
        }
        gp.gameState = gp.new_gameState;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        keysPressed[e.getKeyCode()] = true;

        handleKeys();
        // TITLE STATE

    }

    @Override
    public void keyReleased(KeyEvent e) {

        keysPressed[e.getKeyCode()] = false;

    }
}
