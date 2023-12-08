package main;

import effects.EffectManager;
import entity.*;
import net.Packet00Login;
import net.Packet01Logout;
import object.SuperObject;
import server.ServerPanel;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
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
    public final int maxScreenCol = 16; // 16 tiles wide
    public final int maxScreenRow = 16; // 16 tiles tall
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
    public final int screenHeight = tileSize * maxScreenRow; // 768 pixels tal

    // WORLD SETTINGS
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;


    //FPS
    final int FPS = 30;

    // SYSTEM
    public UI ui = new UI(this);
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(ui.gp);
    Sound music = new Sound();
    Sound sfx = new Sound();
    public CollisionChecker cCheck = new CollisionChecker(this);
    public AssetSetter aS = new AssetSetter(this);
    public EventHandler eH = new EventHandler(this);
    Thread gameThread;

    // ENTITY AND OBJECTS
    public Player player = new Player(this, keyH, 3, 15);
    public Entity[] npc = new Entity[10];
    public SuperObject[] obj = new SuperObject[10];


    // Player 2
    public NPC_Player player2 = new NPC_Player(this);
    public String player2Direction = "down";
    public int player2WorldX = 3;
    public int player2WorldY = 15;

    public List<NPC_Player> players = new ArrayList<>();


    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int readState = 3;


    //Effect light
    EffectManager effectManager = new EffectManager(this);
    boolean LIGHT = true;
    public int lightsize = 400;

    public GamePanel() throws IOException {
        //new GameClient(this, "localhost"); if not an instance of game server
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        if (!(this instanceof ServerPanel)) {
            socketClient = new GameClient(this, "localhost");
        }

    }

    public void setupGame() throws IOException {
        aS.setObject();
        aS.setPlayer2();
        aS.setNPC();
        effectManager.setup();
        gameState = titleState;
        //playMusic(0);
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        socketClient.start();
        Packet00Login loginPacket = new Packet00Login();
        loginPacket.writeData(socketClient);
        System.out.println("Client Socket started");
        //socketClient.sendData("ping".getBytes());
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
                repaint();
                delta--;
            }
        }
    }

    public void update() {

        if (gameState == playState) {
            player.update();
            //player2.update(); <- This is done by the client thread


            //  will be added upon in the future
        }
        if (gameState == pauseState) {
        }

    }

    public synchronized void updatePlayer2(String direction, int worldX, int worldY) {
        if (gameState == playState) {
            if (player2 != null) {
                player2.direction = direction;
                player2.worldX = worldX;
                player2.worldY = worldY;
            }
        }
        if (gameState == pauseState) {
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // DEBUG
        long drawStartTime = 0;
        if (DEV_MODE) {
            drawStartTime = System.nanoTime();
        }


        if (gameState == titleState) {
            ui.draw(g2d);
        } else if (gameState == playState) {

            // TILE
            tileM.draw(g2d);
            // OBJECT
            for (SuperObject superObject : obj) {
                if (superObject != null) {
                    superObject.draw(g2d, this);
                }
            }
            // NPC
            for (Entity entity : npc) {
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
        } else if (gameState == pauseState) {
            // TILE
            tileM.draw(g2d);
            // OBJECT
            for (SuperObject superObject : obj) {
                if (superObject != null) {
                    superObject.draw(g2d, this);
                }
            }
            // NPC
            for (Entity entity : npc) {
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
            for (SuperObject superObject : obj) {
                if (superObject != null) {
                    superObject.draw(g2d, this);
                }
            }
            // NPC
            for (Entity entity : npc) {
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
        }

        // DEBUG
        if (DEV_MODE) {
            long drawEndTime = System.nanoTime();
            long drawTime = drawEndTime - drawStartTime;
            g2d.setColor(Color.WHITE);
            System.out.println("Draw Time: " + (float) drawTime / 1000000 + "ms");
        }

        g2d.dispose();
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
        // Insert the code you want to execute before closing the window
        // For example: saving game state, releasing resources, logging, etc.
        Packet01Logout logoutPacket = new Packet01Logout();
        logoutPacket.writeData(socketClient);
        System.out.println("DISCONNECTING");
        socketClient.close();

    }
}
