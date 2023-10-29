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
    Thread gameThread;
    // ENTITY AND OBJECTS

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;

    public UI ui = new UI(this);
    KeyHandler keyH = new KeyHandler(ui.gp);
    Player player = new Player(this, keyH);

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


        if (gameState == titleState) {
            ui.draw(g2d);
        } else if (gameState == playState) {
            player.draw(g2d);
        } else if (gameState == pauseState) {

        }

        g2d.dispose();
    }
}
