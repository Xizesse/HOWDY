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
    //public List<NPC_Player> players = new ArrayList<>(); //created and controlled by the clients
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
                //repaint();
                delta--;
            }
        }
    }

    void attackPlayers() {

        for (NPC_Player player : players) {
            if (player == null) return;
            int map = player.map;
            for (int npcIndex = 0; npcIndex < npc[map].length; npcIndex++) {
                System.out.println("npc: " + npc[map][npcIndex] + " on map " + map);
                if (npc[map][npcIndex] == null) return;
                int distance = (int) Math.sqrt(Math.pow(player.worldX - npc[map][npcIndex].worldX, 2) + Math.pow(player.worldY - npc[map][npcIndex].worldY, 2));
                if (distance <= npc[map][npcIndex].attackRange && npc[map][npcIndex].attackCoolDown == 0) {
                    System.out.println("Attack");
                    npc[map][npcIndex].attackCoolDown = npc[map][npcIndex].defAttackCoolDown;
                    player.currentHealth -= npc[map][npcIndex].damage;
                    Packet05Health p5 = new Packet05Health(-1, npc[map][npcIndex].damage, map);
                    p5.writeData(socketServer);
                    Packet05Health p5_2 = new Packet05Health(-2, npc[map][npcIndex].damage, map);
                    p5_2.writeData(socketServer);
                }
            }


        }
    }

    public void update() {

        if (gameState == playState) {
            //player.update();
            //player2.update(); <- This is done by the client thread


            if (players.get(0) != null && players.get(1) != null) {
                if (players.get(0).map == players.get(1).map)
                //both on the same map
                {
                    //we update only once
                    int bothmap = players.get(0).map;
                    attackPlayers();
                    //all the npcs on the both map
                    for (int i = 0; i < npc[bothmap].length; i++) {
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


            }
            flagUpdated = false;

        }
        if (!flagUpdated)
        //se não tiver dado update, ou estão em mapas diferentes ou um deles está null
        {
            attackPlayers();
            for (NPC_Player player : players) {
                if (player != null) {
                    int map = player.map;
//                        System.out.println("Updating map " + map);
                    for (int i = 0; i < npc[map].length; i++) {
                        //System.out.println("Updating NPC " + (i+1));
                        //System.out.println("NPC " + (i+1) + " is " + npc[i]);
                        if (npc[map][i] != null) {
                            npc[map][i].update();
                            Packet02Move packet = new Packet02Move((i + 1), map, npc[map][i].worldX, npc[map][i].worldY, npc[map][i].direction);
                            //if is a shark print

                            //System.out.println("Moving NPC " + (i+1) + " to " + npc[i].worldX + ", " + npc[i].worldY + " facing " + npc[i].direction);
                            socketServer.sendData(packet.getData(), player.ipAddress, player.port);

                        }
                    }

                }
            }


        }
        flagUpdated = false;


    }


    public void paintComponent(Graphics g) {
    }
}
