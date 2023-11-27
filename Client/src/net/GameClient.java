package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import entity.Player;
import entity.PlayerMP;
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
                    this.game.player2.setAction(packet.getDirection(), packet.getX(), packet.getY());
                    //System.out.println("[ X, Y, dir]: [ "+packet.getX() + " , " + packet.getY() + " , " + packet.getDirection() + " ]");
                }
            }

    private void handleObject(Packet04Object packet)
    {
        //System.out.println("Handling object");
        //System.out.println("Item ID: "+packet.getItemID()+" Give: "+packet.getGive());
        if(this.game != null) {
            if (this.game.obj[packet.getItemID()] != null) {

                //if give is true, give it to the player
                if (packet.getGive()) {
                    //System.out.println("Giving item");
                    if (game.obj[packet.getItemID()].id == 1) {
                        //System.out.println("Helmet");
                        game.player.helmetOn = true;
                        //System.out.println("Helmet on");

                    }
                }
                //if give is false, give it to the other player
                else {
                }
                //delete the object
                this.game.obj[packet.getItemID()] = null;
            }

        }
    }
    private void handleMapChange(Packet06MapChange packet)
    {

    }

     public void sendData(byte[] data)
     {
         DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);// create a packet to send to the server
         try{
             //System.out.println("Sending data to server: "+data);
             socket.send(packet);
         } catch(IOException e){
             e.printStackTrace();
         }
     }

}
