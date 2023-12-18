package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import main.GamePanel;

public class GameClient extends Thread { // extends Thread so we can run it in the background
    private InetAddress ipAddress;// IP address of the server
    private DatagramSocket socket;// socket to send and receive packets
    private GamePanel game; // reference to the game panel

    public GameClient(GamePanel game, String ipAddress) {
        this.game = game; // set the reference to the game panel
        try {
            this.socket = new DatagramSocket(); // create a new socket
            this.ipAddress = InetAddress.getByName(ipAddress); // get the IP address of the server
        } catch (SocketException e) {
            e.printStackTrace();// print the error
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void run() {// this method is called when we start the thread
        while (true) {
            byte[] data = new byte[1024];// create a byte array to store the data we receive
            DatagramPacket packet = new DatagramPacket(data, data.length);//putting data into the packets
            try {
                socket.receive(packet); // receive the data
                this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort()); // parse the packet
            } catch (IOException e) {
                e.printStackTrace(); // print the error
            }

        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data, 0, data.length).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login();
                System.out.println("you have joined the game...");
                game.gameState = game.waitingState;
                game.new_gameState = game.waitingState;
                break;
            case LOGOUT:
                game.gameState = game.titleState;
                game.new_gameState = game.titleState;
                break;

            case MOVE:
                //System.out.println("Move packet received");
                packet = new Packet02Move(data);
                handleMove((Packet02Move) packet);
                break;
            case ATTACK:
                packet = new Packet03Attack(data);
                handleAttack((Packet03Attack) packet);
                //System.out.println("Attack packet received");
                break;
            case OBJECT:
                System.out.println("Object packet received");
                packet = new Packet04Object(data);
                handleObject((Packet04Object) packet);
                break;
            case HEALTH:
                packet = new Packet05Health(data);
                System.out.println("Health packet received");
                handleHealth((Packet05Health) packet);
                break;
            case MAPCHANGE:
                packet = new Packet06MapChange(data);
                System.out.println("Map change packet received");
                handleMapChange((Packet06MapChange) packet);
                break;
            case READY:
                packet = new Packet07Ready(data);
                System.out.println("Ready packet received");
                handleReady((Packet07Ready) packet);
                break;
            case LEAVE:
                System.out.println("Leave packet received");
                handleLeave();
                break;

        }

    }

    private void handleLeave() {
        game.gameState = game.titleState;
        game.new_gameState = game.titleState;
    }

    private void handleReady(Packet07Ready packet) {
        if (this.game != null) {
            if (packet.getStart() == 1) {
                //reset ready
                game.playerIsReady = false;
                game.player2IsReady = false;
                game.gameState = game.playState;
                game.new_gameState = game.playState;
            } else {
                game.player2Skin = packet.getCharacter();
                game.player2IsReady = packet.getReady() == 1 ? true : false;
            }
        }
    }

    private void handleHealth(Packet05Health packet) {
        //if id is -1 it is me
        //if it is -2 it is the other player
        //0 to 20 is npc index on that map
        System.out.println("ID = " + packet.getEntityID() + " Health = " + packet.getHealth() + " Map = " + packet.getMap());
        if (this.game != null) {
            if (packet.getEntityID() == -1) {
                System.out.println("This player was attacked");
                game.player.currentHealth = packet.getHealth();
            } else if (packet.getEntityID() == -2) {
                game.player2.currentHealth = packet.getHealth();
                System.out.println("Other player was attacked");
            } else {
                int i = packet.getEntityID();
                this.game.npc[packet.getMap()][i].currentHealth = packet.getHealth(); //DONE: Handle mapIndex
                this.game.npc[packet.getMap()][i].damageAnimationCounter = this.game.npc[packet.getMap()][i].defDamageAnimationCounter;
                if (this.game.npc[packet.getMap()][i].currentHealth <= 0) {
                    this.game.npc[packet.getMap()][i].alive = false;
                    this.game.npc[packet.getMap()][i] = null;
                    System.out.println("npc " + i + " died");
                }
            }
        }
    }

    private void handleAttack(Packet03Attack packet) {
        //System.out.println("Attack packet received");
        if (this.game != null) {
            if (packet.getEntityID() == 0) {
                //System.out.println("Player 1 attacked");
                game.player2.isAttacking = true;
            } else {
                this.game.npc[packet.getMap()][packet.getEntityID()].isAttacking = true; //DONE: Handle mapIndex
                System.out.println("NPC " + packet.getEntityID() + " attacked" + " on map " + packet.getMap());
                this.game.npc[packet.getMap()][packet.getEntityID()].damageAnimationCounter = this.game.npc[packet.getMap()][packet.getEntityID()].defDamageAnimationCounter;
            }
        }
    }


    private void handleMove(Packet02Move packet) {
        if (this.game != null) {
            //System.out.println("ID = " + packet.getEntityID() + " X = " + packet.getX() + " Y = " + packet.getY() + " Direction = " + packet.getDirection());
            if (packet.getEntityID() == 0) {

                //this.game.player2.direction = packet.getDirection();
                //this.game.player2.worldX = packet.getX();
                //this.game.player2.worldY = packet.getY();
                game.updatePlayer2(packet.getDirection(), packet.getMap(), packet.getX(), packet.getY());
                //System.out.println("Player 2 moved to " + packet.getX() + "," + packet.getY() + " direction: " + packet.getDirection());
            } else {
                int i = packet.getEntityID() - 1;


                this.game.npc[packet.getMap()][i].direction = packet.getDirection(); //DONE: Handle mapIndex
                this.game.npc[packet.getMap()][i].worldX = packet.getX(); //DONE: Handle mapIndex
                this.game.npc[packet.getMap()][i].worldY = packet.getY(); //DONE: Handle mapIndex
            }
        }
    }

    private void handleObject(Packet04Object packet) {

        if (this.game != null) {
            if (this.game.obj[packet.getMap()][packet.getitemIndex()] != null) {

                //if give is true, give it to the player
                if (packet.getGive()) {
                    System.out.println("\n\nRECEIVED ITEM");
                    game.player.giveItem(game.obj[packet.getMap()][packet.getitemIndex()]);


                    this.game.obj[packet.getMap()][packet.getitemIndex()] = null;
                }
                //if give is false, give it to the other player
                else {
                    System.out.println("\n\nDID NOT RECEIVE ITEM");
                    game.player2.inventory.add(game.obj[packet.getMap()][packet.getitemIndex()]); // kinda done
                    this.game.obj[packet.getMap()][packet.getitemIndex()] = null;
                }
                //delete the object
                //
            }


        }
    }

    private void handleMapChange(Packet06MapChange packet) {
        List<TileChange> changes = packet.getChanges();

        for (TileChange change : changes) {
            this.game.tileM.updateMap(packet.getLevel(), change.getX(), change.getY(), change.getNewTile());
        }

    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);// create a packet to send to the server
        try {
            //System.out.println("Sending packet to " + ipAddress + " to port:" + packet.getPort());
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        /*
        // Close the socket if it's open
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }*/
    }
}
