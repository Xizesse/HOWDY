package net;

import entity.NPC_Player;
import server.ServerPanel;

import java.net.*;
import java.io.IOException;


//00 for login
//01 for logout


public class GameServer extends Thread {
    private DatagramSocket socket;


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
            while (true) {
                System.out.println("Server running");
                //print ips from players connected
                for (NPC_Player player : game.players) {
                    if (player == null) {
                        continue;
                    }
                    System.out.println("[" + player.ipAddress.getHostAddress() + "] port: " + player.port);
                }
                try {
                    Thread.sleep(5000); // Sleep for 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        heartbeatThread.start();

        //initialize 2 elements in connectedPlayers
        game.players.add(null);
        game.players.add(null);

        while (true) {
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
                System.out.println("Objects in the game: ");
                for (int i = 0; i < game.obj.length; i++) {
                    if (game.obj[i] != null) {
                        System.out.println("Object " + i + "has ID: " + game.obj[i].id);
                    }
                }
                if (game.players.get(0) == null) {
                    game.players.set(0, new NPC_Player(address, port, 0, 0, "down", game));
                    System.out.println("LOGIN player 1 from [" + game.players.get(0).ipAddress.getHostAddress() + "] port: " + game.players.get(0).port);
                    break;
                }
                if (game.players.get(1) == null) {
                    game.players.set(1, new NPC_Player(address, port, 0, 0, "down", game));
                    System.out.println("LOGIN player 2 from [" + game.players.get(1).ipAddress.getHostAddress() + "] port: " + game.players.get(1).port);
                    game.gameState = game.playState;
                    break;

                }
                System.out.println("Server full");

                break;


            case LOGOUT:
                //take the player out of the game
                //System.out.println("["+address.getHostName()+"] port: "+port+", has left the game...");
                for (int i = 0; i < game.players.size(); i++) {
                    if (game.players.get(i) != null && game.players.get(i).ipAddress.equals(address) && game.players.get(i).port == port) {
                        game.players.set(i, null);
                        System.out.println("[" + address.getHostName() + "] port: " + port + ", has left the game...");
                        break;
                    }
                }

                break;

            case MOVE:
                Packet02Move p = new Packet02Move(data);
                updatePlayer(address, port, p.getX(), p.getY(), p.getDirection());
                //System.out.println("["+address.getHostName()+"] port: "+port+", entity" + p.getEntityID() + " moved to " + p.getX() + "," + p.getY() + " direction: " + p.getDirection());
                sendDataToAllClientsExceptOne(p.getData(), address, port);
                //System.out.println("["+address.getHostName()+"] port: "+port+", entity " + p.getEntityID() + " moved to " + p.getX() + "," + p.getY() + " direction: " + p.getDirection());
                break;
            case ATTACK:
                Packet03Attack p3 = new Packet03Attack(data);
                System.out.println("[" + address.getHostName() + "] port: " + port + ", entity" + p3.getEntityID() + " attacked " + p3.getAttacks() + " times");
                sendDataToAllClientsExceptOne(p3.getData(), address, port);
                //System.out.println("["+address.getHostName()+"] port: "+port+", entity " + p3.getEntityID() + " attacked " + p3.getAttacks() + " times");
                break;
            case OBJECT:
                Packet04Object p4 = new Packet04Object(data);

                //System.out.println("REQUEST FROM ["+address.getHostName()+"]: "+port+", object " + p4.getitemIndex() + " that is " + game.obj[p4.getitemIndex()].name);
                //check if that item is available
                //print all items addresses

                if (game.obj[p4.getitemIndex()] != null) {
                    //check if the item is already given
                    //send packet back to the player -> he recieves the item
                    System.out.println("GIVE ITEM TO [" + address.getHostName() + "] port: " + port + ", object" + (char) p4.getitemIndex() + " " + game.obj[p4.getitemIndex()].name);

                    Packet04Object p4_2 = new Packet04Object(p4.getitemIndex(), true);
                    sendData(p4_2.getData(), address, port);
                    //send packet back to the other player -> he does not recieve the item
                    //System.out.println("["+address.getHostName()+"] port: "+port+", object" +  p4.getitemIndex() + " already given");
                    Packet04Object p4_3 = new Packet04Object(p4.getitemIndex(), false);
                    sendDataToAllClientsExceptOne(p4_3.getData(), address, port);
                    //remove the item from the game
                    game.obj[p4.getitemIndex()] = null;

                }
                break;
            case MAPCHANGE:
                Packet06MapChange p6 = new Packet06MapChange(data);
                //System.out.println("["+address.getHostName()+"] port: "+port+", mapchange" + p6.getLevel() + " " + p6.getnChanges());
                sendDataToAllClients(p6.getData());


                break;
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
        //System.out.println("Sending to all clients");
        for (NPC_Player player : game.players) {
            if (player == null) {
                continue;
            }
            sendData(data, player.ipAddress, player.port);
        }
    }

    public void sendDataToAllClientsExceptOne(byte[] data, InetAddress ipAddress, int port) {
        System.out.println("Sending to: " + ipAddress.getHostAddress() + " port: " + port);
        for (NPC_Player player : game.players) {
            if (player == null) {
                continue;
            }
            if ((player.port != port) && (player.ipAddress != ipAddress)) {
                sendData(data, player.ipAddress, player.port);
                //System.out.println("Sending to: "+player.ipAddress.getHostAddress()+" port: "+player.port);
            }
        }
    }

    private void updatePlayer(InetAddress address, int port, int x, int y, String direction) {
        for (NPC_Player player : game.players) {
            if (player != null && player.ipAddress.equals(address) && player.port == port) {
                player.worldX = x;
                player.worldY = y;
                player.direction = direction;
                //System.out.println("Player updated: " + player.x + "," + player.y);
                return;
            }
        }
        //System.out.println("Player not found for update.");
    }
}
