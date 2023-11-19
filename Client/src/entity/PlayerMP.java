package entity;

import main.GamePanel;
import main.KeyHandler;
import net.GameClient;
import net.GameServer;
import entity.Player;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class PlayerMP extends NPC {
    public InetAddress ipAddress;
    public int port;


    public PlayerMP(GamePanel gp, InetAddress ipAddress, int port, int x, int y) {
        super(gp, x, y);
        direction = "down";
        this.ipAddress = ipAddress;
        this.port = port;

    }
}
