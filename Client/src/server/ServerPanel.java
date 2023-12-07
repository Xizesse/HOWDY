package server;
import entity.*;
import main.*;
import net.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//Server panel class, similar to the game panel class, but with a few differences, implementing
//just what the server needs. No graphic part
public class ServerPanel extends GamePanel {
    //public GameClient socketClient = new GameClient(this, "localhost");
    // Screen settings
    // WORLD SETTINGS
    //NET
    public GameServer socketServer = new GameServer(this);
    //FPS
    final int FPS = 30;
    Thread gameThread;

    // Players
    //Array of NPC_Player2
    public List<NPC_Player> players = new ArrayList<>(); //created and controlled by the clients
    // GAME STATE
    public ServerPanel() throws IOException {
        super();
    }
    public void setupGame() {
        System.out.println("Setting up game");
        socketServer.start();
        aS.setObject();
        System.out.println("Setting up objects from asset setter");
        //aS.setPlayer2();
        //aS.setPlayers
        aS.setNPC();
        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null) {
                System.out.println("NPC " + (i+1) + " is " + npc[i]);
            }
        }



        for (int i = 0; i < npc.length; i++) {

            if (npc[i] != null) {

                Packet02Move packet = new Packet02Move( (i+1),npc[i].worldX, npc[i].worldY, npc[i].direction);
                //System.out.println("Moving NPC " + (i+1) + " to " + npc[i].worldX + ", " + npc[i].worldY + " facing " + npc[i].direction);
                for (NPC_Player player : players) {
                    if (player != null) {
                        //System.out.println("Sending packet to player ");
                        socketServer.sendData(packet.getData(), player.ipAddress, player.port);

                    }
                }

            }
        }

        for (int i = 0; i < npc.length; i++) {

            if (npc[i] != null) {

                Packet02Move packet = new Packet02Move( (i+1),npc[i].worldX, npc[i].worldY, npc[i].direction);
                //System.out.println("Moving NPC " + (i+1) + " to " + npc[i].worldX + ", " + npc[i].worldY + " facing " + npc[i].direction);
                for (NPC_Player player : players) {
                    if (player != null) {
                        System.out.println("Sending packet to player ");
                        socketServer.sendData(packet.getData(), player.ipAddress, player.port);

                    }
                }
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
                    for (NPC_Player player : players) {
                        if (player != null) {
                            //System.out.println("Sending packet to player ");
                            socketServer.sendData(packet.getData(), player.ipAddress, player.port);

                        }
                    }
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
