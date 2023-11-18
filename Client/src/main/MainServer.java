package main;

import net.GameServer;

public class MainServer {
    public static void main(String[] args) {
        GameServer socketServer = new GameServer();
        socketServer.start();
    }
}