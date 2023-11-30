package server;

import main.GamePanel;
import net.GameServer;

import javax.swing.*;

public class MainServer {

    public static void main(String[] args)
    {
        ServerPanel sp = new ServerPanel();

        //System.out.println("ServerPanel instance: " + sp);
        sp.setupGame();
        System.out.println("objects set up");

        for (int i = 0; i < sp.obj.length; i++) {
            if (sp.obj[i] != null) {
                System.out.println("Object index: " + i + " Object ID: " + sp.obj[i].id);
            }
        }
        sp.startGameThread();

    }
}