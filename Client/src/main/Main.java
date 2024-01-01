package main;



import net.Packet00Login;
import server.MainServer;
import server.ServerPanel;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.PublicKey;


public class Main {

    public static boolean DEV_MODE = false;
    public static JFrame window;


    public static void main(String[] args) throws IOException {

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("HOWDY");
        new Main().setIcon();

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        gamePanel.config.loadConfig();
        if (gamePanel.fullScreenOn) {
            window.setUndecorated(true);
        }

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();

        gamePanel.startGameThread();

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Perform any actions you need before the window closes
                gamePanel.performClosingActions();
                //System.out.println("Closing window...");

                // Dispose the window and exit the application
                System.exit(0);
            }
        });
    }

    public static void launchServer() throws IOException {
        MainServer.main(null);
    }
    public void setIcon() {
        javax.swing.ImageIcon img = new javax.swing.ImageIcon("res/monster_eye/eye_left1.png");
        window.setIconImage(img.getImage());

    }

}