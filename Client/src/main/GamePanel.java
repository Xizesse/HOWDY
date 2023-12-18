package main;

import effects.EffectManager;
import entity.*;
import net.Packet00Login;
import net.Packet01Logout;
import net.Packet07Ready;
import object.SuperObject;
import server.ServerPanel;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.GameClient;

import static main.Main.DEV_MODE;


public class GamePanel extends JPanel implements Runnable {

    // NETWORK
    public GameClient socketClient = null;

    // Screen settings
    public final int originalTileSize = 16; // 16x16 pixels
    public final int scale = 3; // 3x scale
    public final int tileSize = originalTileSize * scale; // 48x48 pixels
    public final int maxScreenCol = 24; // 24 tiles wide
    public final int maxScreenRow = 16; // 16 tiles tall
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
    public final int screenHeight = tileSize * maxScreenRow; // 768 pixels tal
    public boolean fullScreenOn = false;

    // WORLD SETTINGS
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    public final int maxMaps = 10;
    public int currentMap = 0;

    public String userInputedServerIP = "";

    public boolean ipInserted = false;
    //FPS
    final int FPS = 30;

    // SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public UI ui = new UI(this, keyH);
    public boolean Ijustbecameready = false;

    Sound music = new Sound();
    Sound sfx = new Sound();
    public CollisionChecker cCheck = new CollisionChecker(this);
    public AssetSetter aS = new AssetSetter(this);
    public EventHandler eH = new EventHandler(this);
    Config config = new Config(this);
    Thread gameThread;

    // ENTITY AND OBJECTS
    public Player player = new Player(this, keyH, 3, 15, 0);
    public Entity[][] npc = new Entity[maxMaps][10];
    public SuperObject[][] obj = new SuperObject[maxMaps][20];

    public int player1Skin = 0;
    public int player2Skin = 1;
    public boolean playerIsReady = false;
    public boolean player2IsReady = false;
    public boolean playerIsHoast = false;

    //FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2d;

    // Player 2
    public NPC_Player player2 = new NPC_Player(this, 0);
    public String player2Direction = "down";

    public int player2WorldX = 3;
    public int player2WorldY = 15;

    public List<NPC_Player> players = new ArrayList<>();


    // GAME STATE
    public int gameState, prev_gameState, new_gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int readState = 3;
    public final int optionsState = 4;
    public final int joinState = 5;
    public final int waitingState = 6;
    public int endGame = 0;
    public int optionsBack = 0;


    //Effect light
    EffectManager effectManager = new EffectManager(this);
    boolean LIGHT = true;
    public boolean GOD = false;
    public int lightSize = 500;

    public GamePanel() throws IOException {
        //new GameClient(this, "localhost"); if not an instance of game server
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);


    }

    public void setupGame() throws IOException {
        aS.setObject();
        aS.setPlayer2();
        aS.setNPC();
        effectManager.setup();
        gameState = titleState;
        playMusic(0);

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) tempScreen.getGraphics();

        if (fullScreenOn) {
            setFullScreen();
        }
    }

    public void setFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();

    }


    @Override
    public void run() {

        double deltaInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / deltaInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                drawToTempScreen();
                drawToScreen();
                delta--;
            }
        }
    }

    public void update() {
        if (gameState == joinState) {
            if (ipInserted) {
                if (!(this instanceof ServerPanel)) {
                    socketClient = new GameClient(this, userInputedServerIP);
                    socketClient.start();
                    Packet00Login loginPacket = new Packet00Login();
                    loginPacket.writeData(socketClient);
                    System.out.println("Client Socket started");
                    //socketClient.sendData("ping".getBytes());
                    ipInserted = false;
                }
            }
        }
        if (gameState == waitingState){
            if (Ijustbecameready){
                Packet07Ready readyPacket = new Packet07Ready(playerIsReady ? 1 : 0, player1Skin, 0);
                System.out.println("Sending ready packet " + readyPacket.getReady());
                readyPacket.writeData(socketClient);
                Ijustbecameready = false;
            }
        }

        if (gameState == playState) {
            player.update();
            //player2.update(); <- This is done by the client thread

            //  will be added upon in the future
        }
        if (gameState == pauseState) {
        }

    }

    public synchronized void updatePlayer2(String direction, int map, int worldX, int worldY) {
        if (gameState == playState) {
            if (player2 != null) {
                player2.map = map;
                player2.direction = direction;
                player2.worldX = worldX;
                player2.worldY = worldY;
            }
        }
        if (gameState == pauseState) {
            //do something ?
        }
    }

    public void drawToTempScreen() {
        g2d.setColor(Color.black);
        g2d.drawRect(0, 0, screenWidth, screenHeight);
        g2d.fillRect(0, 0, screenWidth, screenHeight);
        // DEBUG
        long drawStartTime = 0;
        if (DEV_MODE) {
            drawStartTime = System.nanoTime();
        }
        endGame = 0;
        optionsBack = 0;

        if (gameState == titleState) {
            ui.draw(g2d);
        } else if (gameState == joinState) {
            ui.draw(g2d);
        } else if (gameState == waitingState) {
            ui.draw(g2d);

        } else if (gameState == playState) {

            // TILE
            tileM.draw(g2d);
            // OBJECT
            //if (obj[currentMap] != null)
            for (int i = 0; i < obj[1].length; i++) {
                if (obj[currentMap] != null) {
                    if (obj[currentMap][i] != null) {
                        obj[currentMap][i].draw(g2d, this);

                    }
                }

            }
            // NPC
            for (Entity entity : npc[currentMap]) {
                if (entity != null) {
                    entity.draw(g2d);
                }
            }

            // PLAYER1
            player.draw(g2d);
            // PLAYER2
            if (player2 != null) {
                if (player2.map == currentMap) {
                    player2.draw(g2d);
                }
            }

            if (LIGHT) effectManager.draw(g2d);

            ui.draw(g2d);
        } else if (gameState == pauseState) {
            // TILE
            tileM.draw(g2d);
            // OBJECT
            for (SuperObject superObject : obj[currentMap]) {
                if (superObject != null) {
                    superObject.draw(g2d, this);
                }
            }
            // NPC
            for (Entity entity : npc[currentMap]) {
                if (entity != null) {
                    entity.draw(g2d);
                }
            }

            // PLAYER1
            player.draw(g2d);
            // PLAYER2
            if (player2 != null) {
                player2.draw(g2d);
            }


            if (LIGHT) effectManager.draw(g2d);
            ui.draw(g2d);
        } else if (gameState == readState) {
            // TILE
            tileM.draw(g2d);
            // OBJECT
            for (SuperObject superObject : obj[currentMap]) {
                if (superObject != null) {
                    superObject.draw(g2d, this);
                }
            }
            // NPC
            for (Entity entity : npc[currentMap]) {
                if (entity != null) {
                    entity.draw(g2d);
                }
            }

            // PLAYER1
            player.draw(g2d);
            // PLAYER2
            if (player2 != null) {
                player2.draw(g2d);
            }
            if (LIGHT) effectManager.draw(g2d);
            ui.draw(g2d);
        } else if (gameState == optionsState) {
            if (prev_gameState == playState) {

                // TILE
                tileM.draw(g2d);
                // OBJECT
                for (SuperObject superObject : obj[currentMap]) {
                    if (superObject != null) {
                        superObject.draw(g2d, this);
                    }
                }
                // NPC
                for (Entity entity : npc[currentMap]) {
                    if (entity != null) {
                        entity.draw(g2d);
                    }
                }

                // PLAYER1
                player.draw(g2d);
                // PLAYER2
                if (player2 != null) {
                    player2.draw(g2d);
                }

                if (LIGHT) effectManager.draw(g2d);

            } else if (prev_gameState == pauseState) {
                // TILE
                tileM.draw(g2d);
                // OBJECT
                for (SuperObject superObject : obj[currentMap]) {
                    if (superObject != null) {
                        superObject.draw(g2d, this);
                    }
                }
                // NPC
                for (Entity entity : npc[currentMap]) {
                    if (entity != null) {
                        entity.draw(g2d);
                    }
                }


                // PLAYER1
                player.draw(g2d);
                // PLAYER2
                if (player2 != null) {
                    player2.draw(g2d);
                }


                if (LIGHT) effectManager.draw(g2d);
            }
            ui.draw(g2d);
            /*if(endGame == 1){
                new_gameState = titleState;
                prev_gameState = optionsState;
            }
            if(optionsBack == 1){
                new_gameState = prev_gameState;
                prev_gameState = optionsState;
            }*/
        }
        // DEBUG
        if (DEV_MODE) {
            long drawEndTime = System.nanoTime();
            long drawTime = drawEndTime - drawStartTime;
            g2d.setColor(Color.WHITE);
            System.out.println("Draw Time: " + (float) drawTime / 1000000 + "ms");
        }
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        sfx.setFile(i);
        sfx.play();
    }

    public void performClosingActions() {

        if (socketClient != null) {
            Packet01Logout logoutPacket = new Packet01Logout();
            logoutPacket.writeData(socketClient);
            System.out.println("DISCONNECTING");
            socketClient.close();
        }
    }
}
