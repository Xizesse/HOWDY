package server;

import main.GamePanel;
import net.GameServer;

import javax.swing.*;
import java.io.IOException;

public class MainServer {

    public static void main(String[] args) throws IOException {
        ServerPanel sp = new ServerPanel();

        //System.out.println("ServerPanel instance: " + sp);
        sp.setupGame();


        sp.startGameThread();

    }
}