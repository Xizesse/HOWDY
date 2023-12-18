package main;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static main.Main.DEV_MODE;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean[] keysPressed = new boolean[999];

    public boolean invalidIPinserted = false;

    //User defined key bindings
    public int upKey = KeyEvent.VK_UP;
    public int downKey = KeyEvent.VK_DOWN;
    public int leftKey = KeyEvent.VK_LEFT;
    public int rightKey = KeyEvent.VK_RIGHT;
    public int backKey = KeyEvent.VK_ESCAPE;
    public int attackKey = KeyEvent.VK_SPACE;
    public int confirmKey = KeyEvent.VK_ENTER;
    public int godKey = KeyEvent.VK_G;
    public int lightKey = KeyEvent.VK_L;
    public int pauseKey = KeyEvent.VK_P;

    public static int devKey = KeyEvent.VK_T;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    private void titleKeys() {
        if (keysPressed[upKey]) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) gp.ui.commandNum = 4;
        }
        if (keysPressed[downKey]) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 4) gp.ui.commandNum = 0;

        }
        if (keysPressed[confirmKey]) {
            //host
            if (gp.ui.commandNum == 0) {
                gp.playerIsHoast = true;
                gp.ipInserted = true;
                gp.userInputedServerIP = "localhost";
                //wait for game panel to update gamestate
            }
            //join
            else if (gp.ui.commandNum == 1) {
                gp.new_gameState = gp.joinState;

            }
            //exit
            else if (gp.ui.commandNum == 4) {
                System.exit(0);
            }
        }

    }

    private void joinKeys() {
        if (keysPressed[backKey]) {
            gp.new_gameState = gp.optionsState;
            gp.prev_gameState = gp.gameState;
        }
        for (int i = KeyEvent.VK_A; i <= KeyEvent.VK_Z; i++) {
            if (keysPressed[i]) {
                gp.userInputedServerIP
                        += (char) (i + (int) 'a' - (int) 'A');
                keysPressed[i] = false;
            }

        }

        for (int i = KeyEvent.VK_0; i <= KeyEvent.VK_9; i++) {
            if (keysPressed[i]) {
                gp.userInputedServerIP
                        += (char) i - KeyEvent.VK_0;
                keysPressed[i] = false;
            }
        }

        for (int i = KeyEvent.VK_NUMPAD0; i <= KeyEvent.VK_NUMPAD9; i++) {
            if (keysPressed[i]) {
                gp.userInputedServerIP
                        += (char) i - KeyEvent.VK_NUMPAD0;
                keysPressed[i] = false;
            }
        }

        if (keysPressed[KeyEvent.VK_DECIMAL] || keysPressed[KeyEvent.VK_PERIOD]) {
            gp.userInputedServerIP
                    += ".";
            keysPressed[KeyEvent.VK_SPACE] = false;
        }

        if (keysPressed[KeyEvent.VK_BACK_SPACE]) {
            if (gp.userInputedServerIP.length() > 0) {
                gp.userInputedServerIP
                        = gp.userInputedServerIP
                        .substring(0, gp.userInputedServerIP
                                .length() - 1);
            }
        }

        if (keysPressed[confirmKey]) {
            if (!IPisValid(gp.userInputedServerIP)) {
                invalidIPinserted = true;
                return;
            }

            invalidIPinserted = false;

            System.out.println("IP is valid");

            gp.ipInserted = true;

//            gp.new_gameState = gp.waitingState;
        }
    }

    private void waitingKeys() {
        if (keysPressed[backKey]) {
            gp.new_gameState = gp.optionsState;
            gp.prev_gameState = gp.gameState;
        }
        if (keysPressed[rightKey]) {
            gp.player1Skin++;
            gp.Ijustbecameready = true;
            if (gp.player1Skin > 1) {
                gp.player1Skin = 0;
            }
        }

        if (keysPressed[leftKey]) {
            gp.player1Skin--;
            gp.Ijustbecameready = true;

            if (gp.player1Skin < 1) {
                gp.player1Skin = 1;
            }
        }

        if (keysPressed[confirmKey]) {
            gp.playerIsReady = !gp.playerIsReady;
            gp.Ijustbecameready = true;
        }
    }

    private void playKeys() {
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
            gp.prev_gameState = gp.gameState;
        } else if (keysPressed[godKey]) {
            gp.GOD = !gp.GOD;
            gp.LIGHT = !gp.LIGHT;
        }
    }

    private void pauseKeys() {
        if (keysPressed[pauseKey]) {
            gp.new_gameState = gp.playState;
        } else if (keysPressed[backKey]) {
            gp.new_gameState = gp.optionsState;
            gp.prev_gameState = gp.pauseState;
        }
    }

    private void optionsKeys() {
        if (keysPressed[backKey]) {
            gp.new_gameState = gp.prev_gameState;
            gp.prev_gameState = gp.optionsState;
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

    private void readKeys() {
        if (keysPressed[attackKey]) {
            gp.new_gameState = gp.playState;
        }
    }

    private void handleKeys() {
        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            titleKeys();
        }
        // JOIN STATE
        else if (gp.gameState == gp.joinState) {
            joinKeys();

        }
        // WAITING STATE
        else if (gp.gameState == gp.waitingState) {
            waitingKeys();
        }
        //PLAY STATE
        else if (gp.gameState == gp.playState) {

            playKeys();
        }
        //PAUSE
        else if (gp.gameState == gp.pauseState) {
            pauseKeys();
        }
        //READ
        else if (gp.gameState == gp.readState) {
            readKeys();
        }
        //OPTIONS STATE
        else if (gp.gameState == gp.optionsState) {
            optionsKeys();
        }
        gp.gameState = gp.new_gameState;
    }

    private boolean IPisValid(String ip) {
        if (Objects.equals(ip, "localhost")) {
            return true;
        }

        Pattern p = Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
        Matcher m = p.matcher(gp.userInputedServerIP);
        return m.find();
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
