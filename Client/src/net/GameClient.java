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
        }

    }

            private void handleMove(Packet02Move packet) {
                if(this.game != null){
                    this.game.player2.setAction(packet.getDirection(), packet.getX(), packet.getY());
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
