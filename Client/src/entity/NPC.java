package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NPC extends Entity
{

        GamePanel gp;

        public final int screenX;
        public final int screenY;

        public BufferedImage HelmetUp, HelmetDown, HelmetLeft, HelmetRight;


        public NPC(GamePanel gp, int x, int y) {
            this.gp = gp;
            screenX = gp.screenWidth / 2 - gp.tileSize / 2;
            screenY = gp.screenHeight / 2 - gp.tileSize / 2;
            getPlayerImage();

        }

        public void setDefaultValue() {
            worldX = gp.tileSize * 8; //starting position = (8,8)
            worldY = gp.tileSize * 8;
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

        public void update(int worldX, int worldY)
        {


            this.worldX = worldX;
            this.worldY = worldY;
            spriteCounter++;

                if (spriteCounter > 10)
                {
                    if (spriteNum == 1 &&( direction.equals("up") || direction.equals("down") || direction.equals("left") || direction.equals("right"))) {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;
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

            g2d.drawImage(body, x, y, gp.tileSize, gp.tileSize, null);
            g2d.drawImage(helmet, x, y, gp.tileSize, gp.tileSize, null);

        }

}
