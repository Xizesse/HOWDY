package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public BufferedImage HelmetUp, HelmetDown, HelmetLeft, HelmetRight;


    public Player(GamePanel gp, KeyHandler keyH, int x, int y) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        solidArea = new Rectangle(8,16, 32, 32);

        speed = 4;
        direction = "down";
        this.worldX = gp.tileSize* x;
        this.worldY = gp.tileSize* y;
        getPlayerImage();
        System.out.println("Player created on (" + x + "," + y + ")");
    }

    public void setDefaultValue() {
        worldX = gp.tileSize * 8; //starting position = (8,8)
        worldY = gp.tileSize * 8;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {

        bodyUp1 = setup("boy/boy_up_1");
        bodyUp2 = setup("boy/boy_up_2");
        bodyDown1 = setup("boy/boy_down_1");
        bodyDown2 = setup("boy/boy_down_2");
        BodyLeft1 = setup("boy/boy_left_1");
        BodyLeft2 = setup("boy/boy_left_2");
        BodyRight1 = setup("boy/boy_right_1");
        BodyRight2 = setup("boy/boy_right_2");
        titleArt = setup("boy/boy_title_art");

        HelmetUp = setup("boy/ironHelmet_up");
        HelmetDown = setup("boy/ironHelmet_down");
        HelmetLeft = setup("boy/ironHelmet_left");
        HelmetRight = setup("boy/ironHelmet_right");

    }

    public BufferedImage setup(String imagePath) {
        UtilityTool uT = new UtilityTool();
        BufferedImage scaledImage = null;
        try {
            scaledImage = ImageIO.read(ClassLoader.getSystemResourceAsStream(imagePath + ".png"));
            scaledImage = uT.scaleImage(scaledImage, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledImage;
    }

    public void update() {

        //MOVEMENT AND COLLISION CHECKING
        if (keyH.downPressed|| keyH.upPressed|| keyH.leftPressed|| keyH.rightPressed) {
            if (keyH.upPressed) {
                colisionOn = false;
                direction = "up";
                gp.cCheck.checkTile(this);
                if (!colisionOn) {
                    worldY -= speed;
                }
            }
            if (keyH.downPressed) {
                colisionOn = false;
                direction = "down";
                gp.cCheck.checkTile(this);
                if (!colisionOn) {
                    worldY += speed;
                }
            }
            if (keyH.leftPressed) {
                colisionOn = false;
                direction = "left";
                gp.cCheck.checkTile(this);
                if (!colisionOn) {
                    worldX -= speed;
                }
            }
            if (keyH.rightPressed) {
                colisionOn = false;
                direction = "right";
                gp.cCheck.checkTile(this);
                if (!colisionOn) {
                    worldX += speed;
                }

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


    }

    public void draw(Graphics2D g2d) {


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



        int x = screenX;
        int y = screenY;

        //Stop camera movement at the edge of the map
        //left
        if(screenX > worldX){
            x = worldX;
        }
        //top
        if(screenY > worldY){
            y = worldY;
        }
        //right
        if(screenX < worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth){
            x = worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth;
        }
        //bottom
        if(screenY < worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight){
            y = worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight;
        }

        g2d.drawImage(body, x, y, null);
        g2d.drawImage(helmet, x, y, null);

    }
}