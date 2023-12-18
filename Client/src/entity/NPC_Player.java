package entity;

import main.GamePanel;
import object.OBJ_Heart;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.util.ArrayList;

public class NPC_Player extends Entity {
    public InetAddress ipAddress;
    //public NPC_Player player;

    public ArrayList<SuperObject> inventory = new ArrayList<>(10);
    public int ready = 0;
    BufferedImage heartFull, heartHalf, heartEmpty;
    BufferedImage[][] sprites = new BufferedImage[2][8];
    public boolean helmetOn = false;
    public int map;
    public int character = 1;
    public int screenX, screenY;
    public int currentHealth, maxHealth;
    public int port;

    public NPC_Player(InetAddress ipAddress, int port, int x, int y, String direction, GamePanel gp, int map) {
        super(gp, map);
        this.ipAddress = ipAddress;
        this.port = port;
        this.worldX = x;
        this.worldY = y;
        this.direction = direction;
        currentHealth = 6;
        maxHealth = 6;
        //solidAreaDefaultX = solidArea.x;
        //solidAreaDefaultY = solidArea.y;
    }

    public NPC_Player(GamePanel gp, int map) {
        super(gp, map);
        currentHealth = 6;
        maxHealth = 6;
        direction = "down";
        speed = 4;


        getImage();
    }


    public void getImage() {

        sprites[0][0] = setup("player1/boy_up_1");
        sprites[0][1] = setup("player1/boy_up_2");
        sprites[0][2] = setup("player1/boy_down_1");
        sprites[0][3] = setup("player1/boy_down_2");
        sprites[0][4] = setup("player1/boy_left_1");
        sprites[0][5] = setup("player1/boy_left_2");
        sprites[0][6] = setup("player1/boy_right_1");
        sprites[0][7] = setup("player1/boy_right_2");
        titleArt = setup("player1/boy_title_art");

        sprites[1][0] = setup("girl/girl_up_1");
        sprites[1][1] = setup("girl/girl_up_2");
        sprites[1][2] = setup("girl/girl_down_1");
        sprites[1][3] = setup("girl/girl_down_2");
        sprites[1][4] = setup("girl/girl_left_1");
        sprites[1][5] = setup("girl/girl_left_2");
        sprites[1][6] = setup("girl/girl_right_1");
        sprites[1][7] = setup("girl/girl_right_2");


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


        if ((previousWorldX != worldX) || (previousWorldY) != worldY) {   //if player moved update the sprite
            spriteCounter++;

            if (spriteCounter > 10) {
                if (spriteNum == 1) {
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
        if (attackCounter > 5) {
            isAttacking = false;
            attackCounter = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {

        BufferedImage body = null;
        BufferedImage attack = null;
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;

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
                        body = sprites[gp.player2Skin][0];
                    } else if (spriteNum == 2) {
                        body = sprites[gp.player2Skin][1];
                    }

                    break;
                case "down":
                    if (isAttacking) {
                        attack = attackDown;
                    }
                    if (spriteNum == 1) {
                        body = sprites[gp.player2Skin][2];
                    } else if (spriteNum == 2) {
                        body = sprites[gp.player2Skin][3];
                    }

                    break;
                case "left":
                    if (isAttacking) {
                        attack = attackLeft;
                    }
                    if (spriteNum == 1) {
                        body = sprites[gp.player2Skin][4];
                    } else if (spriteNum == 2) {
                        body = sprites[gp.player2Skin][5];
                    }

                    break;
                case "right":
                    if (isAttacking) {
                        attack = attackRight;
                    }
                    if (spriteNum == 1) {
                        body = sprites[gp.player2Skin][6];
                    } else if (spriteNum == 2)
                        body = sprites[gp.player2Skin][7];
                    break;
            }

            g2d.drawImage(body, screenX, screenY, null);
            drawHealthBar(g2d, screenX, screenY);

            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.get(i).equippable) {
                    if (inventory.get(i).name == "firefly") {
                        g2d.drawImage(inventory.get(i).image, screenX, screenY - gp.tileSize, null);
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
            if (isAttacking) {

                switch (direction) {
                    case "up":
                        g2d.drawImage(attack, screenX, screenY - gp.tileSize / 2 - gp.tileSize * attackCounter / 5, null);
                        break;
                    case "down":
                        g2d.drawImage(attack, screenX, screenY + gp.tileSize / 2 + gp.tileSize * attackCounter / 5, null);
                        break;
                    case "left":
                        g2d.drawImage(attack, screenX - gp.tileSize / 2 - gp.tileSize * attackCounter / 5, screenY, null);
                        break;
                    case "right":
                        g2d.drawImage(attack, screenX + +gp.tileSize / 2 + gp.tileSize * attackCounter / 5, screenY, null);
                        break;
                }
                attackCounter++;
                if (attackCounter > 5) {
                    isAttacking = false;
                    attackCounter = 0;
                }
            }
        }

    }

    private void drawHealthBar(Graphics2D g2d, int screenX, int screenY) {
        int x = screenX;
        int y = screenY - gp.tileSize / 3;
        int i = 0;

        while (i < (this.maxHealth / 2)) {
            if (this.currentHealth >= (i + 1) * 2) {

                g2d.drawImage(heartFull, x, y, null);
            } else if (this.currentHealth >= (i * 2) + 1) {

                g2d.drawImage(heartHalf, x, y, null);
            } else {

                g2d.drawImage(heartEmpty, x, y, null);
            }

            i++;
            x += gp.tileSize / 3; // Move to the next position to draw the next heart
        }
    }

}
