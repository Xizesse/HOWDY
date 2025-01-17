package net;

import entity.NPC_Player;
import server.ServerPanel;

import java.net.*;
import java.io.IOException;
import java.util.List;


//00 for login
//01 for logout


public class GameServer extends Thread {
    private DatagramSocket socket;

    private volatile boolean kamikazeRequest = false;


    private ServerPanel game;


    public GameServer(ServerPanel game) {

        try {
            this.socket = new DatagramSocket(1331);
            int port = socket.getLocalPort();
            System.out.println("Server started on port: " + port);
            this.game = game;


        } catch (BindException e) {
            System.err.println("Port unavailable");

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Thread heartbeatThread = new Thread(() -> {
            while (!kamikazeRequest) {
                System.out.println("Server running");
                //print ips from players connected
                for (NPC_Player player : game.players) {
                    if (player == null) {
                        continue;
                    }
                    System.out.println("[" + player.ipAddress.getHostAddress() + "] port: " + player.port + " on map " + player.map);
                }
                try {
                    Thread.sleep(5000); // Sleep for 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("BOOM KAMIKAZE ON THE Hearthbeat THREAD");
        });
        heartbeatThread.start();

        //initialize 2 elements in connectedPlayers
        game.players.add(null);
        game.players.add(null);

        while (!kamikazeRequest) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);

            } catch (IOException e) {
                e.printStackTrace();
            }
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

            //String message = new String(packet.getData(),0, packet.getLength());
            //System.out.println("CLIENT ["+packet.getAddress().getHostAddress()+"] > " + message);
            //if(message.trim().equalsIgnoreCase("ping")){
            //    System.out.println("SERVER > pong");
            //    sendData("pong".getBytes(), packet.getAddress(), packet.getPort());

        }
        socket.close();
        System.out.println("BOOM KAMIKAZE ON THE GAME SERVER");
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        //System.out.println("Parsing packet");
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        switch (type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                //print all objects
                if (game.players.get(0) == null) {
                    game.players.set(0, new NPC_Player(address, port, 0, 0, "down", game, 0));
                    System.out.println("LOGIN player 1 from [" + game.players.get(0).ipAddress.getHostAddress() + "] port: " + game.players.get(0).port);
                    //confirm with the same packet to the player
                    sendData(data, address, port);
                    break;
                }
                if (game.players.get(1) == null) {
                    game.players.set(1, new NPC_Player(address, port, 0, 0, "down", game, 0));
                    System.out.println("LOGIN player 2 from [" + game.players.get(1).ipAddress.getHostAddress() + "] port: " + game.players.get(1).port);
                    //confirm with the same packet to the player
                    sendData(data, address, port);
                    game.gameState = game.playState;
                    break;

                }
                System.out.println("Server full");

                break;


            case LOGOUT:
                //if a player loged out
                //broadcast logout packet
                Packet01Logout p1 = new Packet01Logout(0);
                sendDataToAllClients(p1.getData());

                //remove all players
                game.players.set(0, null);
                game.players.set(1, null);

                //start the kamikaze of the server
                this.requestShutdown();


                break;

            case MOVE:
                Packet02Move p = new Packet02Move(data);
                updatePlayer(address, port, p.getMap(), p.getX(), p.getY(), p.getDirection());
                sendDataToAllClientsExceptOne(p.getData(), address, port);
                break;
            case ATTACK:
                Packet03Attack p3 = new Packet03Attack(data);
                System.out.println("[" + address.getHostName() + "] port: " + port + ", entity" + p3.getEntityID() + " attacked " + p3.getAttacks() + " times");
                sendDataToAllClientsExceptOne(p3.getData(), address, port);
                for (int i = 0; i < game.players.size(); i++) {
                    if (game.players.get(i) != null && game.players.get(i).ipAddress.equals(address) && game.players.get(i).port == port) {
                        int map = game.players.get(i).map;
//                        int damage = game.players.get(i).damage;
                        int damage = 1;
                        if (game.players.get(i).weapon != null) {
                            damage += game.players.get(i).weapon.tier;
                        }
                        for (int j = 0; j < game.npc[map].length; j++) {
                            if (game.npc[map][j] != null) {
                                //check colision with player
                                if (game.cCheck.checkAttack(game.players.get(i), game.npc[map][j])) {
                                    game.npc[map][j].currentHealth -= damage;
                                    Packet05Health p5 = new Packet05Health(j, game.npc[map][j].currentHealth, map);
                                    sendDataToAllClients(p5.getData());
                                    System.out.println("npc " + j + " health: " + game.npc[map][j].currentHealth);
                                    if (game.npc[map][j].currentHealth <= 0) {
                                        game.npc[map][j].alive = false;
                                        game.npc[map][j] = null;
                                        System.out.println("npc " + j + " died");
                                    }
                                }

                            }
                        }


                        //players npcplayers
                        //monsters entities


                    }
                }

                break;
            case OBJECT:
                Packet04Object p4 = new Packet04Object(data);

                //check witch player is sending the packet
                for (int i = 0; i < game.players.size(); i++) {
                    if (game.players.get(i) != null && game.players.get(i).ipAddress.equals(address) && game.players.get(i).port == port) {
                        int map = game.players.get(i).map;
                        if (game.obj[map][p4.getitemIndex()] != null) {
                            //check if the item is already given
                            //send packet back to the player -> he receives the item
                            Packet04Object p4_2 = new Packet04Object(map, p4.getitemIndex(), true);
                            System.out.println("item from map " + map + " index " + p4.getitemIndex() + " given to player " + i);
                            if (game.obj[map][p4.getitemIndex()].equippable) {
                                game.players.get(i).giveItem(game.obj[map][p4.getitemIndex()]);
                            }
                            sendData(p4_2.getData(), address, port);
                            //send packet back to the other player -> he does not receive the item
                            Packet04Object p4_3 = new Packet04Object(map, p4.getitemIndex(), false);
                            sendDataToAllClientsExceptOne(p4_3.getData(), address, port);
                            //remove the item from the game
                            game.obj[map][p4.getitemIndex()] = null;      //DONE: change to handle currentMap

                        } else {
                            //send packet back to the player -> he does not recieve the item
                            System.out.println("item from map " + map + " index " + p4.getitemIndex() + " given to player " + i);
                            Packet04Object p4_2 = new Packet04Object(map, p4.getitemIndex(), false);
                            sendData(p4_2.getData(), address, port);
                        }

                    }
                }

                break;
            case MAPCHANGE:
                Packet06MapChange p6 = new Packet06MapChange(data);
                sendDataToAllClients(p6.getData());
                //change on the server
                List<TileChange> changes = p6.getChanges();

                for (TileChange change : changes) {
                    this.game.tileM.updateMap(p6.getLevel(), change.getX(), change.getY(), change.getNewTile());
                }

                break;

            case HEALTH:
                Packet05Health p5 = new Packet05Health(data);
                Packet05Health p5_1 = new Packet05Health(-1, p5.getHealth(), p5.getMap());
                Packet05Health p5_2 = new Packet05Health(-2, p5.getHealth(), p5.getMap());

                //update the health for the NPC Player that sent this
                for (NPC_Player player : game.players) {
                    if (player != null && player.ipAddress.equals(address) && player.port == port) {
                        player.currentHealth = p5.getHealth();
                        sendData(p5_1.getData(), address, port);
                        System.out.println("[" + address.getHostName() + "] port: " + port + ", entity" + p5.getEntityID() + " health " + p5.getHealth());
                        break;
                    } else {
                        sendData(p5_2.getData(), address, port);
                        System.out.println("[" + address.getHostName() + "] port: " + port + ", entity" + p5.getEntityID() + " health " + p5.getHealth());
                    }
                }
                break;
            case READY:
                Packet07Ready p7 = new Packet07Ready(data);
                System.out.println("[" + address.getHostName() + "] ready " + p7.getReady());
                //i received a ready packet from a player
                //check witch player is sending the packet

                for (NPC_Player player : game.players) {
                    if (player != null) {
                        if (player.ipAddress.equals(address) && player.port == port) {
                            //update the ready status for the player
                            player.ready = p7.getReady();
                            //player.character = p7.getCharacter();
                            //player.start = p7.getStart();
                            //System.out.println("[" + address.getHostName() + " ready " + p7.getReady());
                            sendDataToAllClientsExceptOne(p7.getData(), address, port);
                            break;
                        }
                    }
                }
                //if both players are ready
                if (game.players.get(0) == null || game.players.get(1) == null) {
                    break;
                }
                if (game.players.get(0).ready == 1 && game.players.get(1).ready == 1) {
                    //ready equals 0 again
                    game.players.get(0).ready = 0;
                    game.players.get(1).ready = 0;
                    //send a packet to both players to start the game
                    Packet07Ready p7_1 = new Packet07Ready(1, 0, 1);
                    sendDataToAllClients(p7_1.getData());
                    System.out.println("[" + address.getHostName() + "] port: " + port + ", game started");
                    game.gameState = game.playState;
                }
                break;
//                case LEAVE:
            //broadcast that packet
            //Packet10Leave p10 = new Packet10Leave();
            //sendDataToAllClients(p10.getData());
            //System.out.println("[" + address.getHostName() + "] port: " + port + ", left the game");
            //remove both players from the game
            //for(int i = 0; i < game.players.size(); i++)
            //{
            //    if(game.players.get(i) != null && game.players.get(i).ipAddress.equals(address) && game.players.get(i).port == port)
            //    {
            //        game.players.set(i, null);
            //        break;
            //    }
            //}


        }


    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (NPC_Player player : game.players) {
            if (player == null) {
                continue;
            }
            sendData(data, player.ipAddress, player.port);
        }
    }

    public void sendDataToAllClientsExceptOne(byte[] data, InetAddress ipAddress, int port) {
        for (NPC_Player player : game.players) {
            if (player == null) {
                continue;
            }
            if ((player.port != port) && (player.ipAddress != ipAddress)) {
                sendData(data, player.ipAddress, player.port);
            }
        }
    }

    private void updatePlayer(InetAddress address, int port, int map, int x, int y, String direction) {
        for (NPC_Player player : game.players) {
            if (player != null && player.ipAddress.equals(address) && player.port == port) {
                player.map = map;
                player.worldX = x;
                player.worldX = x;
                player.worldY = y;
                player.direction = direction;
                return;
            }
        }
    }

    public void requestShutdown() {
        this.kamikazeRequest = true;
    }
}
