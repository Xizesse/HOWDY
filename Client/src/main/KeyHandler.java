package main;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
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
        if (keysPressed[backKey]) {
            gp.new_gameState = gp.optionsState;
            gp.prev_gameState = gp.titleState;
        }
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
                try {
                    Main.launchServer();
                    gp.ui.commandNum = -1;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("hosting");
                gp.playerIsHoast = true;
                gp.showGameCode = true;
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
                gp.exitgame = true;
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
            if (!gp.playerIsReady) {
                gp.player1Skin++;
                gp.waitingRoomUpdate = true;
            }
            if (gp.player1Skin > 1) {
                gp.player1Skin = 0;
                gp.waitingRoomUpdate = true;
            }
        }

        if (keysPressed[leftKey]) {
            if (!gp.playerIsReady) {
                gp.player1Skin--;
                gp.waitingRoomUpdate = true;
            }

            if (gp.player1Skin < 0) {
                gp.player1Skin = 1;
                gp.waitingRoomUpdate = true;
            }
        }

        if (keysPressed[confirmKey]) {
            gp.playerIsReady = !gp.playerIsReady;
            gp.waitingRoomUpdate = true;
        }
    }

    private void playKeys() {
        if (keysPressed[attackKey]) {
            keysPressed[attackKey] = true;
        } else if (keysPressed[pauseKey]) {
            gp.new_gameState = gp.pauseState;
        } else if (keysPressed[devKey]) {
//            DEV_MODE = !DEV_MODE;
        } else if (keysPressed[lightKey]) {
//            gp.LIGHT = !gp.LIGHT;
        } else if (keysPressed[backKey]) {
            gp.new_gameState = gp.optionsState;
            gp.prev_gameState = gp.gameState;
        } else if (keysPressed[godKey]) {
            gp.GOD = !gp.GOD;
            gp.LIGHT = !gp.LIGHT;
        } else if (keysPressed[KeyEvent.VK_E]) {   //TODO: This is temporary, should star with win packet. Remove this line
//            gp.new_gameState = gp.endState;
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
        if (keysPressed[confirmKey]) {
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
        } else if (gp.gameState == gp.endState) {

            if (keysPressed[confirmKey]) {
                gp.new_gameState = gp.titleState;
            }
        }
        gp.gameState = gp.new_gameState;
    }

    private boolean IPisValid(String ip) {
        if (ip.length() == 7) {
            byte[] bytes = new BigInteger(ip, 36).toByteArray();
            int zeroPrefixLength = zeroPrefixLength(bytes);

            ip = "";

            for (int i = zeroPrefixLength; i < bytes.length; i++) {
                ip += bytes[i] & 0xFF;
                ip += ".";
            }

            ip = ip.substring(0, ip.length() - 1); //remove last dot

            gp.userInputedServerIP = ip;
            return IPisValid(ip);
        }

        if (Objects.equals(ip, "localhost")) {
            return true;
        }

        Pattern p = Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    private int zeroPrefixLength(final byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                return i;
            }
        }
        return bytes.length;
    }

    public String getIPcode() {
        String ip = "";
        try (final DatagramSocket socket = new DatagramSocket()) {
            try {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        InetAddress ip2 = null;

        try {
            ip2 = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        byte[] bytes = ip2.getAddress();
        for (byte b : bytes) {
//            System.out.println(b & 0xFF);
        }


        return new BigInteger(1, bytes).toString(36);
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
