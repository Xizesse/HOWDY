package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public BufferedImage HelmetUp, HelmetDown, HelmetLeft, HelmetRight;


    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValue();
        getPlayerImage();
    }

    public void setDefaultValue() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {

        try {
           bodyUp1 = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_up_1.png"));
            bodyUp2 = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_up_2.png"));
            bodyDown1 = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_down_1.png"));
            bodyDown2 = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_down_2.png"));
            BodyLeft1 = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_left_1.png"));
            BodyLeft2 = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_left_2.png"));
            BodyRight1 = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_right_1.png"));
            BodyRight2 = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_right_2.png"));
            titleArt = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/boy_title_art.png"));

            HelmetUp = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/ironHelmet_up.png"));
            HelmetDown = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/ironHelmet_down.png"));
            HelmetLeft = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/ironHelmet_left.png"));
            HelmetRight = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/ironHelmet_right.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update() {

        if (keyH.upPressed) {
            direction = "up";
            y -= speed;
        }
        if (keyH.downPressed) {
            direction = "down";
            y += speed;
        }
        if (keyH.leftPressed) {
            direction = "left";
            x -= speed;
        }
        if (keyH.rightPressed) {
            direction = "right";
            x += speed;
        }

        spriteCounter++;

        if (spriteCounter > 10) {
            if (spriteNum == 1 &&( keyH.downPressed || keyH.upPressed || keyH.leftPressed || keyH.rightPressed)) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

    }

    public void draw(Graphics2D g2d) {
//        g2d.setColor(Color.WHITE);
//        g2d.fillRect(x, y, gp.tileSize, gp.tileSize);


        BufferedImage body = null;
        BufferedImage helmet = null;


        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    body = bodyUp1;
                } else if (spriteNum == 2) {
                    body = bodyUp2;
                }
                helmet = HelmetUp;
                break;
            case "down":                   //need to change to the correct sprites later on
                if (spriteNum == 1) {
                    body = bodyDown1;
                } else if (spriteNum == 2) {
                    body = bodyDown2;
                }
                helmet = HelmetDown;
                break;
            case "left":
                if (spriteNum == 1) {
                    body = BodyLeft1;
                } else if (spriteNum == 2) {
                    body = BodyLeft2;
                }
                helmet = HelmetLeft;
                break;
            case "right":
                if (spriteNum == 1) {
                    body = BodyRight1;
                } else if (spriteNum == 2)
                    body = BodyRight2;
                helmet = HelmetRight;
                break;
        }

        g2d.drawImage(body, x, y, gp.tileSize, gp.tileSize, null);
        g2d.drawImage(helmet, x, y, gp.tileSize, gp.tileSize, null);

    }
}