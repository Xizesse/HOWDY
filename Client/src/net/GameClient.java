package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

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
            case DISCONNECT:

                break;

            case MOVE:
                System.out.println("Move packet received");
                packet = new Packet02Move(data);
                handleMove((Packet02Move)packet);
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



    private void handleMove(Packet02Move packet) {
                if(this.game != null){
                    System.out.println("ID = " + packet.getEntityID() + " X = " + packet.getX() + " Y = " + packet.getY() + " Direction = " + packet.getDirection());
                    if(packet.getEntityID() == 0){
                        //this.game.player2.direction = packet.getDirection();
                        //this.game.player2.worldX = packet.getX();
                        //this.game.player2.worldY = packet.getY();
                        game.updatePlayer2(packet.getDirection(), packet.getX(), packet.getY());
                        System.out.println("Player 2 moved to " + packet.getX() + "," + packet.getY() + " direction: " + packet.getDirection());
                    }
                    else if(packet.getEntityID() == 1) {
                        this.game.npc[0].direction = packet.getDirection();
                        this.game.npc[0].worldX = packet.getX();
                        this.game.npc[0].worldY = packet.getY();
                    }

                }
            }

    private void handleObject(Packet04Object packet)
    {

        if(this.game != null) {
            if (this.game.obj[packet.getitemIndex()] != null) {

                //if give is true, give it to the player
                if (packet.getGive()) {

                    if (game.obj[packet.getitemIndex()].id == 1) {
                        System.out.println("Helmet PickUp");
                        game.player.helmetOn = true;


                    }
                }
                //if give is false, give it to the other player
                else {
                }
                //delete the object
                System.out.println("Helmet deleted");

                this.game.obj[packet.getitemIndex()] = null;
            }

        }
    }
    private void handleMapChange(Packet06MapChange packet)
    {
        List<TileChange> changes = packet.getChanges();

        for (TileChange change : changes) {
            this.game.tileM.updateMap(change.getX(), change.getY(), change.getNewTile());
        }

    }

     public void sendData(byte[] data)
     {
         DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);// create a packet to send to the server
         try{

             socket.send(packet);
         } catch(IOException e){
             e.printStackTrace();
         }
     }

}
