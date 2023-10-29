package main;

import entity.Player;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    // Screen settings

    final int originalTileSize = 16; // 16x16 pixels
    final int scale = 3; // 3x scale
    public final int tileSize = originalTileSize * scale; // 48x48 pixels
    final int maxScreenCol = 16; // 16 tiles wide
    final int maxScreenRow = 12; // 12 tiles tall
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels tall

    public int gameTime = 10;

    //FPS
    final int FPS = 60;
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);

    // Set players default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double deltaInterval = (double)1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / deltaInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {

        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        player.draw(g2d);
        g2d.dispose();
    }
}
