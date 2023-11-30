package server;

import entity.*;
import main.*;
import net.*;
import object.*;
import tile.TileManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static main.Main.DEV_MODE;


//Server panel class, similar to the game panel class, but with a few differences, implementing
//just what the server needs. No graphic part

public class ServerPanel extends GamePanel {
    //public GameClient socketClient = new GameClient(this, "localhost");

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

    //NET
    public GameServer socketServer = new GameServer(this);
    //FPS
    final int FPS = 30;

    // SYSTEM
    //public UI ui = new UI(this);
    //public TileManager tileM = new TileManager(this);

    //public CollisionChecker cCheck = new CollisionChecker(this);
    //public AssetSetter aS = new AssetSetter(this);
    //public EventHandler eH = new EventHandler(this);
    Thread gameThread;

    // ENTITY AND OBJECTS

    //public Entity[] npc = new Entity[10]; //created everywhere but controlled here
    //public SuperObject[] obj = new SuperObject[10]; //created everwhere but controlled here
    //public Entity monster[] = new Entity[10];

    // Players
    //Array of NPC_Player2
    public List<NPC_Player2> players = new ArrayList<>(); //created and controlled by the clients

    // GAME STATE


    public ServerPanel() {
    }

    public void setupGame() {
        System.out.println("Setting up game");
        socketServer.start();
        aS.setObject();
        System.out.println("Setting up objects from asset setter");
        this.obj[0] = new OBJ_Helmet(this);
        this.obj[0].worldX = 8 * this.tileSize;
        this.obj[0].worldY = 15 * this.tileSize;

        System.out.println("Object 0 is "+this.obj[0]);
        this.obj[1] = new OBJ_Axe(this);
        this.obj[1].worldX = 6 * this.tileSize;
        this.obj[1].worldY = 14 * this.tileSize;
        System.out.println("Object 1 is "+this.obj[1]);
        this.obj[2] = new OBJ_Book(this);
        this.obj[2].worldX = 6 * this.tileSize;
        this.obj[2].worldY = 17 * this.tileSize;
        System.out.println("Object 2 is "+this.obj[2]);
        this.obj[3] = new OBJ_PP(this);
        this.obj[3].worldX = 14 * this.tileSize;
        this.obj[3].worldY = 17 * this.tileSize;
        System.out.println("Object 3 is "+this.obj[3]);
        //print all objects and their iD
        for(int j = 0; j < this.obj.length; j++){
            if(this.obj[j] != null){
                System.out.println("Object index: "+j+" Object ID: "+this.obj[j].id);
            }
        }
        //aS.setPlayer2();
        //aS.setPlayers
        aS.setNPC();


        for (int i = 0; i < npc.length; i++) {
            //System.out.println("Setting up NPC " + (i+1));
            if (npc[i] != null) {
                //System.out.println("NPC " + (i+1) + " is " + npc[i]);
                Packet02Move packet = new Packet02Move( (i+1),npc[i].worldX, npc[i].worldY, npc[i].direction);
                //System.out.println("Moving NPC " + (i+1) + " to " + npc[i].worldX + ", " + npc[i].worldY + " facing " + npc[i].direction);
                //socketServer.sendDataToAllClients(packet.getData());
            }
        }
        aS.setMonster();
        for (int i = 0; i < npc.length; i++) {
            //System.out.println("Setting up NPC " + (i+1));
            if (npc[i] != null) {
              //  System.out.println("NPC " + (i+1) + " is " + npc[i]);
                Packet02Move packet = new Packet02Move( (i+1),npc[i].worldX, npc[i].worldY, npc[i].direction);
                //System.out.println("Moving NPC " + (i+1) + " to " + npc[i].worldX + ", " + npc[i].worldY + " facing " + npc[i].direction);
            }
        }
        gameState = titleState;
        //playMusic(0);
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        System.out.println("Client Socket started");
        //socketClient.sendData("ping".getBytes());
    }



    @Override
    public void run() {
        gameState = playState;
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
                //repaint();
                delta--;
            }
        }
    }

    public void update() {

        if(gameState == playState){
            player.update();
            //player2.update(); <- This is done by the client thread

            for (int i = 0; i < npc.length; i++) {
                //System.out.println("Updating NPC " + (i+1));
                //System.out.println("NPC " + (i+1) + " is " + npc[i]);
                if (npc[i] != null) {
                    npc[i].update();
                    Packet02Move packet = new Packet02Move( (i+1),npc[i].worldX, npc[i].worldY, npc[i].direction);
                    //System.out.println("Moving NPC " + (i+1) + " to " + npc[i].worldX + ", " + npc[i].worldY + " facing " + npc[i].direction);
                }
            }
            //  will be added upon in the future
        }
        if(gameState == pauseState){
        }

    }

    public synchronized void updatePlayer2(String direction, int worldX, int worldY) {
        if(gameState == playState){
            if(player2 != null) {
                player2Direction = direction;
                player2WorldX = worldX;
                player2WorldY = worldY;
            }
        }
        if(gameState == pauseState){
        }
    }




    public void paintComponent(Graphics g) {
    }
}
