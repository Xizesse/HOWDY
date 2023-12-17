package server;

import entity.*;
import main.*;
import net.*;
import tile.Tile;

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

    void attackPlayers(NPC_Player player) {

            if (player == null) return;
            int map = player.map;
            //System.out.println("Checking attack for player " + player + " on map " + map);
            //iterate through all the npcs on the map
            for (int npcIndex = 0; npcIndex < npc[map].length; npcIndex++) {
                System.out.println("npc: " + npc[map][npcIndex] + " on map " + map);
                if (npc[map][npcIndex] == null) return;
                int distance = (int) Math.sqrt(Math.pow(player.worldX - npc[map][npcIndex].worldX, 2) + Math.pow(player.worldY - npc[map][npcIndex].worldY, 2));
                //System.out.println("distance: " + distance);
                //System.out.println("range: " + npc[map][npcIndex].attackRange);
                if (distance <= npc[map][npcIndex].attackRange /*&& npc[map][npcIndex].attackCoolDown == 0*/) {
                    System.out.println("Attack");
                    npc[map][npcIndex].attackCoolDown = npc[map][npcIndex].defAttackCoolDown;
                    player.currentHealth -= npc[map][npcIndex].damage;
                    Packet03Attack p3 = new Packet03Attack(npcIndex, 0, map);
                    socketServer.sendData(p3.getData(), player.ipAddress, player.port);

                    Packet05Health p5 = new Packet05Health(-1, npc[map][npcIndex].damage, map);
                    socketServer.sendData(p5.getData(), player.ipAddress, player.port);

                    Packet05Health p5_2 = new Packet05Health(-2, npc[map][npcIndex].damage, map);
                    for(NPC_Player player2 : players){
                        if(player2 != null){
                            if(player2 != player)
                            socketServer.sendData(p5_2.getData(), player2.ipAddress, player2.port);
                        }
                    }




            }

            /*
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
            }*/


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
                    if (bothmap == 1) checkCage();
                    //attackPlayers(players.get(0));
                    //attackPlayers(players.get(1));
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

                                    int distance = (int) Math.sqrt(Math.pow(player.worldX - npc[bothmap][i].worldX, 2) + Math.pow(player.worldY - npc[bothmap][i].worldY, 2));
                                    //System.out.println("distance: " + distance);
                                    //System.out.println("range: " + npc[map][npcIndex].attackRange);
                                    if (distance <= npc[bothmap][i].attackRange /*&& npc[map][npcIndex].attackCoolDown == 0*/)
                                    {
                                        if (npc[bothmap][i].attackCoolDown > 0) continue;

                                        npc[bothmap][i].attackCoolDown = npc[bothmap][i].defAttackCoolDown;
                                        player.currentHealth -= npc[bothmap][i].damage;


                                        Packet05Health p5 = new Packet05Health(-1, player.currentHealth, bothmap);
                                        socketServer.sendData(p5.getData(), player.ipAddress, player.port);

                                        for (NPC_Player player2 : players)
                                        {
                                            Packet03Attack p3 = new Packet03Attack(i, 1, bothmap);
                                            socketServer.sendData(p3.getData(), player2.ipAddress, player2.port);
                                            if (player2 != null) {
                                                if (player2 != player){


                                                    Packet05Health p5_2 = new Packet05Health(-2, player.currentHealth, bothmap);
                                                    socketServer.sendData(p5_2.getData(), player2.ipAddress, player2.port);

                                                }
                                            }
                                        }



                                    }
                                }
                            }
                        }
                    }
                    flagUpdated = true;
                }
                if (players.get(0).map == 2 && players.get(1).map == 2){
                    System.out.println(players.get(0).worldX/tileSize + " , " + players.get(1).worldX/tileSize);
                    if (players.get(0).worldX/tileSize > 26 && players.get(1).worldX/tileSize > 26){
                        System.out.println("ya win"); //TODO: send win packet
                    }
                }
            }

        }
        if (!flagUpdated)
        //se não tiver dado update, ou estão em mapas diferentes ou um deles está null
        {

            for (NPC_Player player : players) {
                if (player == null) continue;
                //System.out.println("Updating map " + player.map);
                //attackPlayers(player);

                int map = player.map;
                if (map == 1)checkCage();
//                        System.out.println("Updating map " + map);
                for (int i = 0; i < npc[map].length; i++) {
                    //System.out.println("Updating NPC " + (i+1));
                    //System.out.println("NPC " + (i+1) + " is " + npc[i]);
                    if (npc[map][i] != null) {
                        npc[map][i].update();
                        Packet02Move packet = new Packet02Move((i + 1), map, npc[map][i].worldX, npc[map][i].worldY, npc[map][i].direction);
                        socketServer.sendData(packet.getData(), player.ipAddress, player.port);

                        int distance = (int) Math.sqrt(Math.pow(player.worldX - npc[map][i].worldX, 2) + Math.pow(player.worldY - npc[map][i].worldY, 2));
                        //System.out.println("distance: " + distance);
                        //System.out.println("range: " + npc[map][npcIndex].attackRange);
                        if (distance <= npc[map][i].attackRange /*&& npc[map][npcIndex].attackCoolDown == 0*/)
                        {
                            System.out.println("Attack ? CoolDown: " + npc[map][i].attackCoolDown);
                            if (npc[map][i].attackCoolDown > 0) continue;
                            npc[map][i].attackCoolDown = npc[map][i].defAttackCoolDown;
                            if (player.currentHealth > 0) player.currentHealth-= npc[map][i].damage;
                            Packet03Attack p3 = new Packet03Attack(i, 1, map);
                            socketServer.sendData(p3.getData(), player.ipAddress, player.port);

                            Packet05Health p5 = new Packet05Health(-1, player.currentHealth, map);
                            socketServer.sendData(p5.getData(), player.ipAddress, player.port);

                            Packet05Health p5_2 = new Packet05Health(-2, player.currentHealth, map);
                            for (NPC_Player player2 : players) {
                                if (player2 != null) {
                                    if (player2 != player)
                                        socketServer.sendData(p5_2.getData(), player2.ipAddress, player2.port);
                                }
                            }



                        }
                    }

                }
            }


        }
        flagUpdated = false;


    }

    private void checkCage() {
        //if both pressure plates are pressed, open the cage
        if(tileM.mapTileNum[1][20][12] == 12 && tileM.mapTileNum[1][12][12] == 12)
        {
            System.out.println("Cage open");
            //Send a packet to all players to open the cage
            ArrayList<TileChange> cage = new ArrayList<>();
            cage.add(new TileChange(1, 16, 12, 10));
            cage.add(new TileChange(1, 16, 13, 10));
            cage.add(new TileChange(1, 16, 14, 10));
            cage.add(new TileChange(1, 17, 14, 0));
            cage.add(new TileChange(1, 15, 14, 0));
            cage.add(new TileChange(1, 15, 14, 0));
            cage.add(new TileChange(1, 14, 15, 0));
            cage.add(new TileChange(1, 18, 15, 0));
            cage.add(new TileChange(1, 14, 16, 0));
            cage.add(new TileChange(1, 18, 16, 0));
            cage.add(new TileChange(1, 14, 17, 0));
            cage.add(new TileChange(1, 18, 17, 0));
            cage.add(new TileChange(1, 14, 18, 0));
            cage.add(new TileChange(1, 15, 18, 0));
            cage.add(new TileChange(1, 16, 18, 0));
            cage.add(new TileChange(1, 17, 18, 0));
            cage.add(new TileChange(1, 18, 18, 0));

            Packet06MapChange p6 = new Packet06MapChange(1, cage);
            //send to all players
            for(NPC_Player player : players)
            {
                if(player != null)
                {
                    socketServer.sendData(p6.getData(), player.ipAddress, player.port);
                }
            }










        }

    }


    public void paintComponent(Graphics g) {
    }
}
