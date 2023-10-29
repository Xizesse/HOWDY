package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValue();
        getPlayerImage();
    }

    public void setDefaultValue(){
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {

        try{
            up1 = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_up_1.png"));
            up2 = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_up_2.png"));
            down1   = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_down_1.png"));
            down2   = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_down_2.png"));
            left1   = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_left_1.png"));
            left2   = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_left_2.png"));
            right1  = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_right_1.png"));
            right2  = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_right_2.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void update(){
        if(keyH.upPressed) {
            direction = "up";
            y -= speed;
        }
        if(keyH.downPressed) {
            direction = "down";
            y += speed;
        }
        if(keyH.leftPressed) {
            direction = "left";
            x -= speed;
        }
        if(keyH.rightPressed) {
            direction = "right";
            x += speed;
        }

    }
    public void draw(Graphics2D g2d){
//        g2d.setColor(Color.WHITE);
//        g2d.fillRect(x, y, gp.tileSize, gp.tileSize);


        BufferedImage image = null;

        switch (direction){
            case "up":
                if (gp.gameTime % 20 < 10) {
                    image = up1;
                } else {
                    image = up2;
                }
                break;
            case "down":
                if (gp.gameTime % 20 < 10) {
                    image = down1;
                } else {
                    image = down2;
                }
                break;
            case "left":
                if (gp.gameTime % 20 < 10) {
                    image = left1;
                } else {
                    image = left2;
                }
                break;
            case "right":
                if (gp.gameTime % 20 < 10) {
                    image = right1;
                } else {
                    image = right2;
                }
                break;
        }

        g2d.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}