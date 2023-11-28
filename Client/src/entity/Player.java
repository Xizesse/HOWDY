package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import net.Packet02Move;
import net.Packet04Object;
import net.Packet06MapChange;
import net.TileChange;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public boolean helmetOn = false;
    public BufferedImage HelmetUp, HelmetDown, HelmetLeft, HelmetRight;


    public Player(GamePanel gp, KeyHandler keyH, int x, int y) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        solidArea = new Rectangle(16,16, 16, 24);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        speed = 4;
        direction = "down";
        this.worldX = gp.tileSize* x;
        this.worldY = gp.tileSize* y;
        getPlayerImage();
        //System.out.println("Player created on (" + x + "," + y + ")");
    }

    public void setDefaultValue() {
        worldX = gp.tileSize * 8; //starting position = (8,8)
        worldY = gp.tileSize * 8;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {

        bodyUp1 = setup("player1/boy_up_1");
        bodyUp2 = setup("player1/boy_up_2");
        bodyDown1 = setup("player1/boy_down_1");
        bodyDown2 = setup("player1/boy_down_2");
        BodyLeft1 = setup("player1/boy_left_1");
        BodyLeft2 = setup("player1/boy_left_2");
        BodyRight1 = setup("player1/boy_right_1");
        BodyRight2 = setup("player1/boy_right_2");
        titleArt = setup("player1/boy_title_art");

        HelmetUp = setup("player1/ironHelmet_up");
        HelmetDown = setup("player1/ironHelmet_down");
        HelmetLeft = setup("player1/ironHelmet_left");
        HelmetRight = setup("player1/ironHelmet_right");

    }



    public void update() {

        //MOVEMENT AND COLLISION CHECKING
        if (keyH.downPressed|| keyH.upPressed|| keyH.leftPressed|| keyH.rightPressed) {
            if (keyH.upPressed) {
                collisionOn = false;
                direction = "up";
                gp.cCheck.checkTile(this);
                int objIndex = gp.cCheck.checkObject(this, true);
                pickUpObject(objIndex);
                if (!collisionOn) {
                    worldY -= speed;
                }
            }
            if (keyH.downPressed) {
                collisionOn = false;
                direction = "down";
                gp.cCheck.checkTile(this);
                int objIndex = gp.cCheck.checkObject(this, true);
                pickUpObject(objIndex);
                if (!collisionOn) {
                    worldY += speed;
                }
            }
            if (keyH.leftPressed) {
                collisionOn = false;
                direction = "left";
                gp.cCheck.checkTile(this);
                int objIndex = gp.cCheck.checkObject(this, true);
                pickUpObject(objIndex);
                if (!collisionOn) {
                    worldX -= speed;
                }
            }
            if (keyH.rightPressed) {
                collisionOn = false;
                direction = "right";
                gp.cCheck.checkTile(this);
                int objIndex = gp.cCheck.checkObject(this, true);
                pickUpObject(objIndex);
                if (!collisionOn) {
                    worldX += speed;
                }

            }




            spriteCounter++;

            Packet02Move packet = new Packet02Move((char) 0, this.worldX, this.worldY, this.direction);
            packet.writeData(gp.socketClient);
            //System.out.println("Sending data to server: "+this.worldX+","+this.worldY);

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

    public void pickUpObject(int i){
        if(i!=999){
            System.out.println(i+" is the object index, " + gp.obj[i].id + " is the object id");
            if (gp.obj[i].id == 4) { //pp
                ArrayList<TileChange> changes = new ArrayList<>();
                //TODO isto está completamente hardcoded
                TileChange change;
                change = new TileChange(14, 17, 12);
                changes.add(change);
                change = new TileChange(14, 16, 8);
                changes.add(change);
                change = new TileChange(14, 15, 8);
                changes.add(change);
                change = new TileChange(14, 14, 13);
                changes.add(change);
                Packet06MapChange p6 = new Packet06MapChange(0, changes);
                System.out.println("Sending map change packet");
                String[] dataArray = p6.readData(p6.getData()).split(",");
                p6.writeData(gp.socketClient);
            } else if (gp.obj[i].id == 1) {//helmet
                Packet04Object p4 = new Packet04Object((char) i, true);
                p4.writeData(gp.socketClient);
                System.out.println("Requesting item: "+p4.getItemID());
            }else if (gp.obj[i].id == 3) {
                Packet04Object p4 = new Packet04Object((char) i, true);
                p4.writeData(gp.socketClient);
                System.out.println("Requesting item: "+p4.getItemID());
                gp.obj[i].readChapter(gp);
                gp.gameState = gp.readState;

            }


//            p6.writeData(gp.socketClient);



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
            case "down":
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
        if (helmetOn) {g2d.drawImage(helmet, x, y, null);}

    }
}