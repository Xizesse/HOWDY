package entity;

import main.GamePanel;
import object.OBJ_Heart;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.util.ArrayList;

public class NPC_Player extends Entity
{
    public InetAddress ipAddress;
    //public NPC_Player player;

    public ArrayList<SuperObject> inventory = new ArrayList<>(10);
    BufferedImage heartFull, heartHalf, heartEmpty;
    public boolean helmetOn = false;
    public int port;
    public NPC_Player(InetAddress ipAddress, int port, int x, int y, String direction, GamePanel gp) {
        super(gp);
        this.ipAddress = ipAddress;
        this.port = port;
        this.worldX = x;
        this.worldY = y;
        this.direction = direction;
        currentHealth = 3;
        maxHealth = 6;
        //solidAreaDefaultX = solidArea.x;
        //solidAreaDefaultY = solidArea.y;
    }
    public NPC_Player(GamePanel gp) {
        super(gp);
        currentHealth = 3;
        maxHealth = 6;
        direction = "down";
        speed = 4;


        getImage();
    }


    public void getImage() {

        bodyUp1 = setup("girl/girl_up_1");
        bodyUp2 = setup("girl/girl_up_2");
        bodyDown1 = setup("girl/girl_down_1");
        bodyDown2 = setup("girl/girl_down_2");
        BodyLeft1 = setup("girl/girl_left_1");
        BodyLeft2 = setup("girl/girl_left_2");
        BodyRight1 = setup("girl/girl_right_1");
        BodyRight2 = setup("girl/girl_right_2");
        titleArt = setup("girl/girl_title_art");


        attackUp = setup("attack/attack_up");
        attackDown = setup("attack/attack_down");
        attackLeft = setup("attack/attack_left");
        attackRight = setup("attack/attack_right");

        /*heartEmpty = setupScaled("heart/heart_empty", 3, 3);
        heartHalf = setupScaled("heart/heart_half",3, 3);
        heartFull = setupScaled("heart/heart_full",3, 3);
        */

        try {
            heartEmpty = ImageIO.read(ClassLoader.getSystemResourceAsStream("hearts/heart_empty.png"));
            heartHalf = ImageIO.read(ClassLoader.getSystemResourceAsStream("hearts/heart_half.png"));
            heartFull = ImageIO.read(ClassLoader.getSystemResourceAsStream("hearts/heart_full.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


@Override
    public void setAction() {

        this.direction = direction;
        this.worldX = worldX;
        this.worldY = worldY;


        if((previousWorldX != worldX) || (previousWorldY) != worldY){   //if player moved update the sprite
            spriteCounter++;

            if (spriteCounter > 10) {
                if (spriteNum == 1 ) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

        }

        previousWorldX = worldX;
        previousWorldY = worldY;

    }


    public void attack() {
        attackCounter++;
        if (attackCounter > 5){
            isAttacking = false;
            attackCounter = 0;
        }
}

    @Override
    public void draw(Graphics2D g2d) {

        BufferedImage body = null;
        BufferedImage attack = null;
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

            switch (direction) {
                case "up":
                    if (isAttacking) {
                        attack = attackUp;
                    }
                    if (spriteNum == 1) {
                        body = bodyUp1;
                    } else if (spriteNum == 2) {
                        body = bodyUp2;
                    }
                    break;
                case "down":
                    if (isAttacking) {
                        attack = attackDown;
                    }
                    if (spriteNum == 1) {
                        body = bodyDown1;
                    } else if (spriteNum == 2) {
                        body = bodyDown2;
                    }
                    break;
                case "left":
                    if (isAttacking) {
                        attack = attackLeft;
                    }
                    if (spriteNum == 1) {
                        body = BodyLeft1;
                    } else if (spriteNum == 2) {
                        body = BodyLeft2;
                    }
                    break;
                case "right":

                    if (isAttacking) {
                        attack = attackRight;
                    }
                    if (spriteNum == 1) {
                        body = BodyRight1;
                    } else if (spriteNum == 2)
                        body = BodyRight2;
                    break;
            }

            g2d.drawImage(body, screenX, screenY, null);
            drawHealthBar(g2d, screenX, screenY);

            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.get(i).equippable) {
                    if(inventory.get(i).name == "firefly"){
                        g2d.drawImage(inventory.get(i).image, screenX, screenY-gp.tileSize, null);
                        break;
                    }
                    switch (direction) {
                        case "up":
                            g2d.drawImage(inventory.get(i).up, screenX, screenY, null);
                            break;
                        case "down":
                            g2d.drawImage(inventory.get(i).down, screenX, screenY, null);
                            break;
                        case "left":
                            g2d.drawImage(inventory.get(i).left, screenX, screenY, null);
                            break;
                        case "right":
                            g2d.drawImage(inventory.get(i).right, screenX, screenY, null);
                            break;
                    }
                }

            }
            if (isAttacking)
            {

                switch (direction) {
                    case "up":
                        g2d.drawImage(attack, screenX, screenY - gp.tileSize/2 - gp.tileSize*attackCounter/5, null);
                        break;
                    case "down":
                        g2d.drawImage(attack, screenX,  screenY + gp.tileSize/2 + gp.tileSize*attackCounter/5, null);
                        break;
                    case "left":
                        g2d.drawImage(attack, screenX - gp.tileSize/2 - gp.tileSize*attackCounter/5 , screenY, null);
                        break;
                    case "right":
                        g2d.drawImage(attack, screenX + + gp.tileSize/2 + gp.tileSize*attackCounter/5, screenY, null);
                        break;
                }
                attackCounter++;
                if (attackCounter > 5){
                    isAttacking = false;
                    attackCounter = 0;
                }
            }
        }

    }

    private void drawHealthBar(Graphics2D g2d, int screenX, int screenY) {
        int x =  screenX;
        int y = screenY-gp.tileSize/3;
        int i=0;

        while (i < (this.maxHealth / 2)) {
            if (this.currentHealth >= (i + 1) * 2) {

                g2d.drawImage(heartFull, x, y, null);
            } else if (this.currentHealth >= (i * 2) + 1) {

                g2d.drawImage(heartHalf, x, y, null);
            } else {

                g2d.drawImage(heartEmpty, x, y, null);
            }

            i++;
            x += gp.tileSize/3; // Move to the next position to draw the next heart
        }
    }

}
