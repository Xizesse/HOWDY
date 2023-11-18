package entity;

import main.GamePanel;
import main.KeyHandler;
import net.GameClient;
import net.GameServer;
import entity.Player;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class PlayerMP extends Player {
    public InetAddress ipAddress;
    public int port;
    public PlayerMP (GamePanel gp, KeyHandler keyH, InetAddress ipAddress, int port,  int x, int y) {
        super(gp, keyH, x, y);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP (GamePanel gp, InetAddress ipAddress, int port, int x, int y) {
        super(gp, null, x, y);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void update(){
        super.update();
    }

}
