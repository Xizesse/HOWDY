package main;

import net.GameServer;

public class MainServer {
    public static void main(String[] args) {
        GamePanel gamePanel = new GamePanel();

        GameServer socketServer = new GameServer(gamePanel);
        socketServer.start();
    }
}