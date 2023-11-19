package entity;

import main.GamePanel;

import java.net.InetAddress;


public class PlayerMP extends NPC_Player2 {
    public InetAddress ipAddress;
    public int port;


    public PlayerMP(GamePanel gp, InetAddress ipAddress, int port, int x, int y) {
        super(gp);
        direction = "down";
        this.ipAddress = ipAddress;
        this.port = port;

    }
}
