package main;

import entity.Entity;
import entity.Player;
import entity.PlayerMP;
import net.Packet00Login;
import net.Packet02Move;
import object.SuperObject;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.GameClient;
import net.GameServer;
import static main.Main.DEV_MODE;


public class GamePanel extends JPanel implements Runnable{

    public GameClient socketClient = new GameClient(this, "localhost");



    // Screen settings

    final int originalTileSize = 16; // 16x16 pixels
    final int scale = 3; // 3x scale
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
    final int FPS = 60;

    // SYSTEM
    public UI ui = new UI(this);
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(ui.gp);
    public CollisionChecker cCheck = new CollisionChecker(this);
    public AssetSetter aS = new AssetSetter(this);
    Thread gameThread;

    // ENTITY AND OBJECTS
    public Player player = new Player(this, keyH, 1,1);

    public Entity player2 = new Entity(this);
    public Entity[] npc = new Entity[10];
    public SuperObject[] obj = new SuperObject[10];

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;



    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);


    }

    public void setupGame() {
        aS.setObject();
        aS.setNPC();
        gameState = titleState;
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

        double deltaInterval = (double)1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / deltaInterval;
            lastTime = currentTime;
            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {

        if(gameState == playState){
            player.update();
            if(player2 != null) {
                player2.update();
            }

        }
        if(gameState == pauseState){
        }


        Packet02Move packet = new Packet02Move(player.worldX, player.worldY);
        packet.writeData(socketClient);
//        System.out.println("Sending data to server: "+player.worldX+","+player.worldY);

    }

//    public synchronized void addRemotePlayer(PlayerMP newPlayer) {
//        this.player2 = newPlayer;
//        repaint();
//    }

//    public synchronized void updatePlayer2(int x, int y) {
//        if(player2 != null) {
//            player2.update(x, y);
//        }
//
//    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // DEBUG
        long drawStartTime = 0;
        if(DEV_MODE) {
            drawStartTime = System.nanoTime();
        }



        if (gameState == titleState) {
            ui.draw(g2d);
        } else if (gameState == playState) {
            // TILE
            tileM.draw(g2d);
            // OBJECT
            for(int i = 0; i < obj.length; i++) {
                if(obj[i] != null) {
                    obj[i].draw(g2d, this);
                }
            }
            // NPC
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].draw(g2d);
                }
            }

            // PLAYER1
            player.draw(g2d);
            // PLAYER2
            if (player2 != null) {
                player2.draw(g2d);
            }
        } else if (gameState == pauseState) {
            ui.draw(g2d);
        }

        // DEBUG
        if(DEV_MODE) {
            long drawEndTime = System.nanoTime();
            long drawTime = drawEndTime - drawStartTime;
            g2d.setColor(Color.WHITE);
            System.out.println("Draw Time: " + (float) drawTime / 1000000 + "ms");
        }

        g2d.dispose();
    }
}
