package entity;

import main.GamePanel;

import java.net.InetAddress;


public class PlayerMP  {
    public InetAddress ipAddress;
    public int port;
    public char entityID;
    public int x;
    public int y;
    public String direction;


    public PlayerMP(InetAddress ipAddress, int port, char entityID, int x, int y, String direction) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }


}
