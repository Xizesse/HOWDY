package entity;

import main.GamePanel;
import main.KeyHandler;
import net.*;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;


public class Player extends Entity {



    KeyHandler keyH;
    public final int screenX;
    public final int screenY;

    public BufferedImage HelmetUp, HelmetDown, HelmetLeft, HelmetRight;

    //Inventory and slots
    public SuperObject helmet;
    public SuperObject armour;
    public SuperObject weapon;
    public SuperObject shield;
    //and then standart inventory
    public ArrayList <SuperObject> inventory = new ArrayList<>(10);

    public Player(GamePanel gp, KeyHandler keyH, int x, int y) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        solidArea = new Rectangle(16,16, 16, 24);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.height = 36;
        attackArea.width = 36;

        speed = 8;
        direction = "down";
        this.worldX = gp.tileSize* x;
        this.worldY = gp.tileSize* y;
        getPlayerImage();
        //PLayer stats
        maxHealth = 6;
        currentHealth = maxHealth;

    }

    public void setDefaultValue() {
        worldX = gp.tileSize * 3;
        worldY = gp.tileSize * 15;
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



        attackUp = setup("attack/attack_up");
        attackDown = setup("attack/attack_down");
        attackLeft = setup("attack/attack_left");
        attackRight = setup("attack/attack_right");


    }


@Override
    public void update() {
        if (isAttacking){
            attack();
        }
        //MOVEMENT AND COLLISION CHECKING
        else if (keyH.downPressed|| keyH.upPressed|| keyH.leftPressed|| keyH.rightPressed || keyH.spacePressed) {
            if (keyH.spacePressed) {
                isAttacking = true;
                Packet03Attack packet = new Packet03Attack(0, 1);
                packet.writeData(gp.socketClient);
                attackCounter = 0;

            }
            if (keyH.upPressed) {
                collisionOn = false;                                               //reset collision
                direction = "up";                                                  //set direction
                gp.cCheck.checkTile(this);                                   //check collision with tile
                int objIndex = gp.cCheck.checkObject(this, true);     //check collision with object
                pickUpObject(objIndex);                                            //pick up object
                //int npcIndex = gp.cCheck.checkEntity(this, gp.npc);          //check collision with npc
                //interactNPC(npcIndex);                                             //interact with npc

                if (!collisionOn) {                                                //if no collision
                    worldY -= speed;                                               //move
                }
            }   //same for all directions
            if (keyH.downPressed) {
                collisionOn = false;
                direction = "down";
                gp.cCheck.checkTile(this);
                int objIndex = gp.cCheck.checkObject(this, true);
                pickUpObject(objIndex);
                //int npcIndex = gp.cCheck.checkEntity(this, gp.npc);
                //interactNPC(npcIndex);

                if (!collisionOn) {
                    worldY += speed;
                }
            }
            if (keyH.leftPressed)
            {
                collisionOn = false;
                direction = "left";
                gp.cCheck.checkTile(this);
                int objIndex = gp.cCheck.checkObject(this, true);
                pickUpObject(objIndex);
                //int npcIndex = gp.cCheck.checkEntity(this, gp.npc);
                //interactNPC(npcIndex);
                //int monsterIndex = gp.cCheck.checkEntity(this, gp.monster);  //check collision with monster
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
                //int npcIndex = gp.cCheck.checkEntity(this, gp.npc);
                //interactNPC(npcIndex);


                //int monsterIndex = gp.cCheck.checkEntity(this, gp.monster);  //check collision with monster
                if (!collisionOn) {
                    worldX += speed;
                }

            }

            //CHECK EVENT
            gp.eH.checkEvent();

            spriteCounter++;

            Packet02Move packet = new Packet02Move( 0, this.worldX, this.worldY, this.direction);
            packet.writeData(gp.socketClient);


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

    private void attack() {
        attackCounter++;
        if (attackCounter > 5){
            isAttacking = false;
            attackCounter = 0;
        }

        switch(direction){
            case "up":
                attackArea.x = worldX + gp.tileSize/2 - attackArea.width/2;
                attackArea.y = worldY - gp.tileSize/2 - attackArea.height/2 - gp.tileSize*attackCounter/5;
                break;
        }
    }


    public void pickUpObject(int i){
        if(i!=999){
            //print all objects and their iD
//            for(int j = 0; j < gp.obj.length; j++){
//                if(gp.obj[j] != null){
//                        System.out.println("Object index: "+j+" Object NAME: " + gp.obj[j].name);
//                        if (Objects.equals(gp.obj[j].name, "PP")) {
//                            System.out.println(gp.obj[j].isItOn);
//                        }
//                }
//            }
            if (i < 0) {
                i = - i;
                gp.obj[gp.currentMap][i].turnOff();
            } else {
                if (gp.obj[gp.currentMap][i].id == 1) {//helmet
                    Packet04Object p4 = new Packet04Object((char) i, true);
                    p4.writeData(gp.socketClient);
                    //System.out.println("Helmet PickUp with id: 1");

                } else if (gp.obj[gp.currentMap][i].id == 2) {//axe
                    Packet04Object p4 = new Packet04Object((char) i, true);
                    p4.writeData(gp.socketClient);
                    System.out.println("Requesting item: " + p4.getitemIndex());
                } else if (gp.obj[gp.currentMap][i].id == 3) { //book
                    Packet04Object p4 = new Packet04Object((char) i, true);
                    p4.writeData(gp.socketClient);
                    System.out.println("Requesting item: " + p4.getitemIndex()); //Sends item INDEX
                    if (gp.obj[gp.currentMap][i] != null) {
                        gp.obj[gp.currentMap][i].readChapter(gp);
                        gp.gameState = gp.readState;
                    }
                } else if (Objects.equals(gp.obj[gp.currentMap][i].name, "PP")) { //pp
                    gp.obj[gp.currentMap][i].interact();
                }
            }

        }
    }

    private void interactNPC(int npcIndex) {
        if(npcIndex!=999){
            System.out.println("NPC colision");
        }
    }
    public void draw(Graphics2D g2d) {


        BufferedImage body = null;
        BufferedImage attack = null;


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
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).equippable) {
                if(inventory.get(i).name == "firefly"){
                    g2d.drawImage(inventory.get(i).image, x, y-gp.tileSize, null);
                }
                else {
                    switch (direction) {
                        case "up":
                            g2d.drawImage(inventory.get(i).up, x, y, null);
                            break;
                        case "down":
                            g2d.drawImage(inventory.get(i).down, x, y, null);
                            break;
                        case "left":
                            g2d.drawImage(inventory.get(i).left, x, y, null);
                            break;
                        case "right":
                            g2d.drawImage(inventory.get(i).right, x, y, null);
                            break;
                    }
                }
            }

        }
        //if (helmetOn) {g2d.drawImage(helmet, x, y, null);}
        if (isAttacking)
        {
            switch (direction) {
                case "up":
                    g2d.drawImage(attack, x, y - gp.tileSize/2 - gp.tileSize*attackCounter/5, null);
                    break;
                case "down":
                    g2d.drawImage(attack, x, y + gp.tileSize/2 + gp.tileSize*attackCounter/5, null);
                    break;
                case "left":
                    g2d.drawImage(attack, x - gp.tileSize/2 - gp.tileSize*attackCounter/5 , y, null);
                    break;
                case "right":
                    g2d.drawImage(attack, x + + gp.tileSize/2 + gp.tileSize*attackCounter/5, y, null);
                    break;
            }
        }
    }
}