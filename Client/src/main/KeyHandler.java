package main;

import effects.Light;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import static main.Main.DEV_MODE;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;

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
        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 2;
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) gp.ui.commandNum = 0;

            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.new_gameState = gp.playState;
                } else if (gp.ui.commandNum == 1) {
                    // LOAD GAME
                } else if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }
        //PLAY STATE
        else if (gp.gameState == gp.playState) {

            if (code == KeyEvent.VK_UP) {
                upPressed = true;
            } else if (code == KeyEvent.VK_DOWN) {
                downPressed = true;
            } else if (code == KeyEvent.VK_LEFT) {
                leftPressed = true;
            } else if (code == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            } else if (code == KeyEvent.VK_SPACE) {
                spacePressed = true;
            } else if (code == KeyEvent.VK_P) {
                gp.new_gameState = gp.pauseState;
            } else if (code == KeyEvent.VK_T) {
                DEV_MODE = !DEV_MODE;
            } else if (code == KeyEvent.VK_L) {
                gp.LIGHT = !gp.LIGHT;
            } else if (code == KeyEvent.VK_ESCAPE) {
                gp.new_gameState = gp.optionsState;
                gp.prev_gameState = gp.playState;
            }
            else if (code == KeyEvent.VK_G) {
                gp.GOD = !gp.GOD;
                gp.LIGHT = !gp.LIGHT;
            }
        }
        //PAUSE
        else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_P) {
                gp.new_gameState = gp.playState;
            } else if (code == KeyEvent.VK_ESCAPE) {
                gp.new_gameState = gp.optionsState;
                gp.prev_gameState = gp.pauseState;
            }
        }
        //READ
        else if (gp.gameState == gp.readState) {
            if (code == KeyEvent.VK_SPACE) {
                gp.new_gameState = gp.playState;
            }
        }
        //OPTIONS STATE
        else if (gp.gameState == gp.optionsState) {
            if (code == KeyEvent.VK_ESCAPE) {
                gp.new_gameState = gp.prev_gameState;
                gp.prev_gameState = gp.optionsState;
            }
            if (code == KeyEvent.VK_SPACE) {
                spacePressed = true;
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
            if (code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                //gp.playSE(9);
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = maxcommandNum;
                }
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                //gp.playSE(9);
                if (gp.ui.commandNum > maxcommandNum) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_LEFT) {
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

            if (code == KeyEvent.VK_RIGHT) {
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
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }

    }
}
