package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    GamePanel gp;
    public int worldX, worldY;

    public int previousWorldX, previousWorldY;
    public int speed;

    public BufferedImage titleArt, bodyUp1, bodyUp2, bodyDown1, bodyDown2, BodyLeft1, BodyLeft2, BodyRight1, BodyRight2;

    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public boolean collisionOn = false;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction(){}

    public void update(){
        setAction();

    }
    public void setDefaultValue(){}

    public void draw(Graphics2D g2d) {

        BufferedImage body = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

//        //Stop camera movement at the edge of the map
        //left
        if (gp.player.screenX > gp.player.worldX) {
            screenX = worldX;
        }
        //top
        if (gp.player.screenY > gp.player.worldY) {
            screenY = worldY;
        }
        //right
        if (gp.player.screenX < gp.player.worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth) {
            screenX = worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth;
        }
        //bottom
        if (gp.player.screenY < gp.player.worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight) {
            screenY = worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight;
        }

        // only draw tiles that are on the screen
        if (worldX > gp.player.worldX - gp.screenWidth &&
                worldX < gp.player.worldX + gp.screenWidth &&
                worldY > gp.player.worldY - gp.screenHeight &&
                worldY < gp.player.worldY + gp.screenHeight) {

            switch (direction) { //get the correct sprite for the direction the entity is facing
                case "up":
                    if (spriteNum == 1) {
                        body = bodyUp1;
                    } else if (spriteNum == 2) {
                        body = bodyUp2;
                    }
                    break;
                case "down":                   //need to change to the correct sprites later on
                    if (spriteNum == 1) {
                        body = bodyDown1;
                    } else if (spriteNum == 2) {
                        body = bodyDown2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        body = BodyLeft1;
                    } else if (spriteNum == 2) {
                        body = BodyLeft2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        body = BodyRight1;
                    } else if (spriteNum == 2)
                        body = BodyRight2;
                    break;
            }

            g2d.drawImage(body, screenX, screenY, null);
        }

    }

    public BufferedImage setup (String imagePath){
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

}