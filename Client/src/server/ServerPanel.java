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

    // Screen settings
    // WORLD SETTINGS
    //NET
    public GameServer socketServer = new GameServer(this);
    //FPS
    final int FPS = 30;
    boolean flagUpdated = false;
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


        /*
        for (int i = 0; i < npc[0].length; i++) { //TODO: Não sei o que é que isto estava a fazer

            if (npc[0][i] != null) {

                Packet02Move packet = new Packet02Move( (i+1), 0,npc[0][i].worldX, npc[0][i].worldY, npc[0][i].direction);
                //System.out.println("Moving NPC " + (i+1) + " to " + npc[i].worldX + ", " + npc[i].worldY + " facing " + npc[i].direction);
                for (NPC_Player player : players) {
                    if (player != null) {
                        //System.out.println("Sending packet to player ");
                        socketServer.sendData(packet.getData(), player.ipAddress, player.port);

                    }
                }

            }
        }

        for (int i = 0; i < npc[0].length; i++) { //TODO: Nem isto

            if (npc[0][i] != null) {

                Packet02Move packet = new Packet02Move( (i+1),0,npc[0][i].worldX, npc[0][i].worldY, npc[0][i].direction);
                //System.out.println("Moving NPC " + (i+1) + " to " + npc[i].worldX + ", " + npc[i].worldY + " facing " + npc[i].direction);
                for (NPC_Player player : players) {
                    if (player != null) {
                        System.out.println("Sending packet to player ");
                        socketServer.sendData(packet.getData(), player.ipAddress, player.port);

                    }
                }
            }
        }

         */
        gameState = titleState;
        //playMusic(0);
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        //System.out.println("Client Socket started");
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
            //player.update();
            //player2.update(); <- This is done by the client thread

            //TODO: EVERYONE READ THIS
            //update is called for the maps where players are
            //IF THERE ARE TWO PLAYERS, if the map is the same, update only once, and set the flagUpdated to true
            //if flag not true update both maps
            //é spaghetti mas funciona

            if (players.get(0)!=null && players.get(1)!=null)
            {
                if (players.get(0).map == players.get(1).map)
                {
                    //we update only once

                    int bothmap = players.get(0).map;
                    for (int i = 0; i < npc[bothmap].length; i++) {
                        //System.out.println("Updating NPC " + (i+1));
                        //System.out.println("NPC " + (i+1) + " is " + npc[i]);
                        if (npc[bothmap][i] != null) {
                            npc[bothmap][i].update();
                            Packet02Move packet = new Packet02Move((i + 1), bothmap, npc[bothmap][i].worldX, npc[bothmap][i].worldY, npc[bothmap][i].direction);
                            //System.out.println("Moving NPC " + (i+1) + " to " + npc[i].worldX + ", " + npc[i].worldY + " facing " + npc[i].direction);
                            for (NPC_Player player : players) {
                                if (player != null) {
                                    //System.out.println("Sending packet to player ");
                                    socketServer.sendData(packet.getData(), player.ipAddress, player.port);
                                }
                            }
                        }
                    }
                    flagUpdated = true;
                }

                if (!flagUpdated)
                {
                    for (NPC_Player player : players)
                    {
                        if (player != null) {
                            int map = player.map;
                            for (int i = 0; i < npc[map].length; i++) {
                                //System.out.println("Updating NPC " + (i+1));
                                //System.out.println("NPC " + (i+1) + " is " + npc[i]);
                                if (npc[map][i] != null) {
                                    npc[map][i].update();
                                    Packet02Move packet = new Packet02Move((i + 1), map , npc[map][i].worldX, npc[map][i].worldY, npc[map][i].direction);
                                    //if is a shark print
                                    if (npc[map][i] instanceof NPC_Shark)
                                    {
                                        System.out.println("Moving Shark " + (i+1) + " to " + npc[map][i].worldX + ", " + npc[map][i].worldY + " facing " + npc[map][i].direction);
                                    }
                                    //System.out.println("Moving NPC " + (i+1) + " to " + npc[i].worldX + ", " + npc[i].worldY + " facing " + npc[i].direction);
                                    socketServer.sendData(packet.getData(), player.ipAddress, player.port);

                                }
                            }

                        }
                    }
                }
                flagUpdated = false;

            }




            }




            //  will be added upon in the future



    }




    public void paintComponent(Graphics g) {
    }
}
