package entity;

import main.GamePanel;
import main.UtilityTool;
import object.SuperObject;
import org.w3c.dom.css.Rect;
import server.ServerPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Objects;

public class Entity {
    public GamePanel gp;
    public int worldX, worldY;
    public int map;
    public int previousWorldX, previousWorldY;
    public int speed;
    public BufferedImage titleArt, bodyUp1, bodyUp2, bodyDown1, bodyDown2, BodyLeft1, BodyLeft2, BodyRight1, BodyRight2;
    public BufferedImage attackUp, attackLeft, attackDown, attackRight;

    public String direction;
    public int spriteCounter = 0;
    public int actionCounter = 0;
    public int attackCounter = 0;

    public int demageAnimationCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean isAttacking = false;

    public int attackCoolDown = 5;
    //Character stats

    public int maxHealth;
    public int currentHealth;
    int dyingCounter = 0;

    public boolean alive = true;
    boolean dying = false;


    public Entity(GamePanel gp, int map) {
        this.gp = gp;
        this.map = map;
    }

    public void setAction() {
    }

    public void setDefaultValue() {
    }

    public void draw(Graphics2D g2d) {

        if (!alive) return;


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
                    if (demageAnimationCounter > 0 && attackUp != null) {
                        demageAnimationCounter--;
                        body = attackUp;
                    }
                    break;
                case "down":                   //need to change to the correct sprites later on
                    if (spriteNum == 1) {
                        body = bodyDown1;
                    } else if (spriteNum == 2) {
                        body = bodyDown2;
                    }
                    if (demageAnimationCounter > 0 && attackDown != null) {
                        demageAnimationCounter--;
                        body = attackDown;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        body = BodyLeft1;
                    } else if (spriteNum == 2) {
                        body = BodyLeft2;
                    }
                    if (demageAnimationCounter > 0 && attackLeft != null) {
                        demageAnimationCounter--;
                        body = attackLeft;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        body = BodyRight1;
                    } else if (spriteNum == 2) {
                        body = BodyRight2;
                    }
                    if (demageAnimationCounter > 0 && attackRight != null) {
                        demageAnimationCounter--;
                        body = attackRight;
                    }
                    break;
            }

            g2d.drawImage(body, screenX, screenY, null);
        }

    }

    public BufferedImage setup(String imagePath) {
        UtilityTool uT = new UtilityTool();
        BufferedImage scaledImage = null;
        try {
            scaledImage = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(imagePath + ".png")));
            scaledImage = uT.scaleImage(scaledImage, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledImage;
    }

    public BufferedImage setupScaled(String imagePath, int width, int height) {
        UtilityTool uT = new UtilityTool();
        BufferedImage scaledImage = null;
        try {
            scaledImage = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(imagePath + ".png")));
            scaledImage = uT.scaleImage(scaledImage, gp.originalTileSize * width, gp.originalTileSize * height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledImage;
    }

    public void update() {


        setAction();
        collisionOn = false;
        gp.cCheck.checkTile(this);

        gp.cCheck.checkObject(this, false);      //check collision with objects


        if (!collisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
}
