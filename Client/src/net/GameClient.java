package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

import main.GamePanel;

public class GameClient extends Thread{ // extends Thread so we can run it in the background
    private InetAddress ipAddress;// IP address of the server
    private DatagramSocket socket;// socket to send and receive packets
    private GamePanel game; // reference to the game panel

    public GameClient(GamePanel game, String ipAddress){
        this.game = game; // set the reference to the game panel
        try {
            this.socket = new DatagramSocket(); // create a new socket
            this.ipAddress = InetAddress.getByName(ipAddress); // get the IP address of the server
        } catch(SocketException e){
            e.printStackTrace();// print the error
        } catch(UnknownHostException e){
            e.printStackTrace();
        }

    }

    public void run(){// this method is called when we start the thread
        while(true){
            byte[] data = new byte[1024];// create a byte array to store the data we receive
            DatagramPacket packet = new DatagramPacket(data, data.length);//putting data into the packets
            try {
                socket.receive(packet); // receive the data
                this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort()); // parse the packet
            } catch(IOException e){
                e.printStackTrace(); // print the error
            }

        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port)
    {
        String message = new String(data,0,data.length).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0,2));
        Packet packet = null;
        switch (type)
        {
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login();
                System.out.println("["+address.getHostAddress()+"][port: "+port+"] has joined the game...");
                break;
            case LOGOUT:
                break;

            case MOVE:
                //System.out.println("Move packet received");
                packet = new Packet02Move(data);
                handleMove((Packet02Move)packet);
                break;
            case ATTACK:
                packet = new Packet03Attack(data);
                handleAttack((Packet03Attack)packet);
                System.out.println("Attack packet received");
                break;
            case OBJECT:
                System.out.println("Object packet received");
                packet = new Packet04Object(data);
                handleObject((Packet04Object)packet);
                break;

            case MAPCHANGE:
                packet = new Packet06MapChange(data);
                System.out.println("Map change packet received");
                handleMapChange((Packet06MapChange)packet);
                break;

        }

    }

    private void handleAttack(Packet03Attack packet) {
        System.out.println("Attack packet received");
        if (this.game != null) {
            if (packet.getEntityID() == 0) {
                //System.out.println("Player 1 attacked");
                game.player2.isAttacking = true;
            } else {


            }
        }
    }


    private void handleMove(Packet02Move packet) {
                if(this.game != null){
                    //System.out.println("ID = " + packet.getEntityID() + " X = " + packet.getX() + " Y = " + packet.getY() + " Direction = " + packet.getDirection());
                    if(packet.getEntityID() == 0){

                        //this.game.player2.direction = packet.getDirection();
                        //this.game.player2.worldX = packet.getX();
                        //this.game.player2.worldY = packet.getY();
                        game.updatePlayer2(packet.getDirection(),packet.getMap(), packet.getX(), packet.getY());
                        //System.out.println("Player 2 moved to " + packet.getX() + "," + packet.getY() + " direction: " + packet.getDirection());
                    }
                    else{
                        int i = packet.getEntityID() - 1;


                        this.game.npc[packet.getMap()][i].direction = packet.getDirection(); //DONE: Handle mapIndex
                        this.game.npc[packet.getMap()][i].worldX = packet.getX(); //DONE: Handle mapIndex
                        this.game.npc[packet.getMap()][i].worldY = packet.getY(); //DONE: Handle mapIndex
                        }
                    }
            }

    private void handleObject(Packet04Object packet)
    {

        if(this.game != null) {
            if (this.game.obj[packet.getMap()][packet.getitemIndex()] != null) {

                //if give is true, give it to the player
                if (packet.getGive()) {
                    System.out.println("\n\nRECEIVED ITEM");

                    game.player.inventory.add(game.obj[packet.getMap()][packet.getitemIndex()]);                      //kinda done
                    if (Objects.equals(game.obj[packet.getMap()][packet.getitemIndex()].name, "firefly")) {       //kinda done
                        game.lightSize += 100;
                        System.out.println("Light size increased to " + game.lightSize);
                    }

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
    private void handleMapChange(Packet06MapChange packet)
    {
        List<TileChange> changes = packet.getChanges();

        for (TileChange change : changes) {
            this.game.tileM.updateMap(0, change.getX(), change.getY(), change.getNewTile());
            //TODO: mapIndex is always 0 for now; NEED to fix packet for mapIndex
        }

    }

     public void sendData(byte[] data)
     {
         DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);// create a packet to send to the server
         try{
             //System.out.println("Sending packet to " + ipAddress + " to port:" + packet.getPort());
             socket.send(packet);
         } catch(IOException e){
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
