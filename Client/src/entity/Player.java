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

    private int attackingX = 0;
    private int attackingY = 0;

    public BufferedImage HelmetUp, HelmetDown, HelmetLeft, HelmetRight;

    //Inventory and slots
    public SuperObject helmet;
    public SuperObject armour;
    public SuperObject weapon;
    public SuperObject shield;
    public SuperObject boots;
    //and then standard inventory
    public ArrayList<SuperObject> inventory = new ArrayList<>(10);

    public Player(GamePanel gp, KeyHandler keyH, int x, int y, int map) {
        super(gp, map);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        solidArea = new Rectangle(16, 16, 16, 24);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.height = 36;
        attackArea.width = 36;

        speed = 8;
        direction = "down";
        this.worldX = gp.tileSize * x;
        this.worldY = gp.tileSize * y;
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
        if (gp.GOD) {
            this.speed = 20;
        } else {
            this.speed = 8;
        }
        if (isAttacking) {
            attack();
        }
        //MOVEMENT AND COLLISION CHECKING
        else if (keyH.keysPressed[keyH.downKey] || keyH.keysPressed[keyH.upKey] || keyH.keysPressed[keyH.leftKey] || keyH.keysPressed[keyH.rightKey] || keyH.keysPressed[keyH.attackKey]) {
            if (keyH.keysPressed[keyH.attackKey]) {
                isAttacking = true;
                Packet03Attack packet = new Packet03Attack(0, 1, map);
                packet.writeData(gp.socketClient);
                attackCounter = 0;

            }
            if (keyH.keysPressed[keyH.upKey]) {
                collisionOn = false;                                               //reset collision
                direction = "up";                                                  //set direction
                if (!gp.GOD) gp.cCheck.checkTile(this);                                   //check collision with tile
                int objIndex = gp.cCheck.checkObject(this, true);     //check collision with object
                pickUpObject(objIndex);                                            //pick up object
                //int npcIndex = gp.cCheck.checkEntity(this, gp.npc);          //check collision with npc
                //interactNPC(npcIndex);                                             //interact with npc

                if (!collisionOn) {                                                //if no collision
                    worldY -= speed;                                               //move
                }
            }   //same for all directions
            if (keyH.keysPressed[keyH.downKey]) {
                collisionOn = false;
                direction = "down";
                if (!gp.GOD) gp.cCheck.checkTile(this);
                int objIndex = gp.cCheck.checkObject(this, true);
                pickUpObject(objIndex);
                //int npcIndex = gp.cCheck.checkEntity(this, gp.npc);
                //interactNPC(npcIndex);

                if (!collisionOn) {
                    worldY += speed;
                }
            }
            if (keyH.keysPressed[keyH.leftKey]) {
                collisionOn = false;
                direction = "left";
                if (!gp.GOD) gp.cCheck.checkTile(this);
                int objIndex = gp.cCheck.checkObject(this, true);
                pickUpObject(objIndex);
                //int npcIndex = gp.cCheck.checkEntity(this, gp.npc);
                //interactNPC(npcIndex);
                //int monsterIndex = gp.cCheck.checkEntity(this, gp.monster);  //check collision with monster
                if (!collisionOn) {
                    worldX -= speed;
                }
            }
            if (keyH.keysPressed[keyH.rightKey]) {
                collisionOn = false;
                direction = "right";
                if (!gp.GOD) gp.cCheck.checkTile(this);
                int objIndex = gp.cCheck.checkObject(this, true);
                pickUpObject(objIndex);

                if (!collisionOn) {
                    worldX += speed;
                }
            }

            //CHECK EVENT
            gp.eH.checkEvent();

            spriteCounter++;
            //System.out.println("Moving to " + worldX + ", " + worldY + " map: " + gp.currentMap);
            Packet02Move packet = new Packet02Move(0, gp.currentMap, this.worldX, this.worldY, this.direction);
            packet.writeData(gp.socketClient);


            if (spriteCounter > 10) {
                if (spriteNum == 1 && (keyH.keysPressed[keyH.downKey] || keyH.keysPressed[keyH.upKey] || keyH.keysPressed[keyH.leftKey] || keyH.keysPressed[keyH.rightKey])) {
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
        if (attackCounter > 5) {
            isAttacking = false;
            attackCounter = 0;
        }

        switch (direction) {
            case "up":
                attackArea.x = worldX + gp.tileSize / 2 - attackArea.width / 2;
                attackArea.y = worldY - gp.tileSize / 2 - attackArea.height / 2 - gp.tileSize * attackCounter / 5;
                break;
        }
    }


    public void pickUpObject(int i) {
        if (i != 999) {
            if (i < 0) {
                i = -i;
                gp.obj[gp.currentMap][i].turnOff();
            }
            else if (Objects.equals(gp.obj[gp.currentMap][i].name, "PP"))
            { //pp
                gp.obj[gp.currentMap][i].interact();
                return;
            }
            else {
                String type = gp.obj[gp.currentMap][i].type;
                switch (type)
                {
                    case "helmet":
                        if ( helmet != null)
                        {
                            if (helmet.tier >= gp.obj[gp.currentMap][i].tier)
                            {
                                return;
                            }
                        }
                        Packet04Object p4 = new Packet04Object(gp.currentMap, (char) i, true);
                        p4.writeData(gp.socketClient);
                        System.out.println("Requesting item: " + p4.getitemIndex());
                        break;
                    case "armour":
                        if ( armour != null)
                        {
                            if (armour.tier >= gp.obj[gp.currentMap][i].tier)
                            {
                                return;
                            }
                        }
                        p4 = new Packet04Object(gp.currentMap, (char) i, true);
                        p4.writeData(gp.socketClient);
                        System.out.println("Requesting item: " + p4.getitemIndex());
                        break;
                    case "weapon":
                        if ( weapon != null)
                        {
                            if (weapon.tier >= gp.obj[gp.currentMap][i].tier)
                            {
                                return;
                            }
                        }
                        p4 = new Packet04Object(gp.currentMap, (char) i, true);
                        p4.writeData(gp.socketClient);
                        System.out.println("Requesting item: " + p4.getitemIndex());
                        break;
                    case "shield":
                        if ( shield != null)
                        {
                            if (shield.tier >= gp.obj[gp.currentMap][i].tier)
                            {
                                return;
                            }
                        }
                        p4 = new Packet04Object(gp.currentMap, (char) i, true);
                        p4.writeData(gp.socketClient);
                        System.out.println("Requesting item: " + p4.getitemIndex());
                        break;
                    case "boots":
                        if ( boots != null)
                        {
                            if (boots.tier >= gp.obj[gp.currentMap][i].tier)
                            {
                                return;
                            }
                        }
                        p4 = new Packet04Object(gp.currentMap, (char) i, true);
                        p4.writeData(gp.socketClient);
                        System.out.println("Requesting item: " + p4.getitemIndex());
                        break;
                    case "book":
                        //giveItem(gp.obj[gp.currentMap][i]);
                        p4 = new Packet04Object(gp.currentMap, (char) i, true);
                        p4.writeData(gp.socketClient);
                        System.out.println("Requesting item: " + p4.getitemIndex());
                        gp.obj[gp.currentMap][i].readChapter(gp);
                        gp.gameState = gp.readState;
                        break;



                    default:
                        p4 = new Packet04Object(gp.currentMap, (char) i, true);
                        p4.writeData(gp.socketClient);
                        System.out.println("Requesting item: " + p4.getitemIndex());
                        break;
                }
            }



                /*
                } else if (gp.obj[gp.currentMap][i].id == 2) {//axe
                    Packet04Object p4 = new Packet04Object(gp.currentMap, (char) i, true);
                    p4.writeData(gp.socketClient);
                    System.out.println("Requesting item: " + p4.getitemIndex());
                } else if (gp.obj[gp.currentMap][i].id == 3) { //book
                    Packet04Object p4 = new Packet04Object(gp.currentMap, (char) i, true);
                    p4.writeData(gp.socketClient);
                    System.out.println("Requesting item: " + p4.getitemIndex()); //Sends item INDEX
                    if (gp.obj[gp.currentMap][i] != null) {
                        gp.obj[gp.currentMap][i].readChapter(gp);
                        gp.gameState = gp.readState;
                    }
                } else if (Objects.equals(gp.obj[gp.currentMap][i].name, "PP")) { //pp
                    gp.obj[gp.currentMap][i].interact();
                } else if (Objects.equals(gp.obj[gp.currentMap][i].name, "RuneDoor")) { //rune door
                    gp.obj[gp.currentMap][i].interact();
                    gp.obj[gp.currentMap][i] = null;
                    }*/
                }
            }



    private void interactNPC(int npcIndex) {
        if (npcIndex != 999) {
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
        if (screenX > worldX) {
            x = worldX;
        }
        //top
        if (screenY > worldY) {
            y = worldY;
        }
        //right
        if (screenX < worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth) {
            x = worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth;
        }
        //bottom
        if (screenY < worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight) {
            y = worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight;
        }

        //g2d.drawImage(body, x, y, null);

        //draw armour, then helmet, then boots, then weapon, then shield
        drawitems(g2d, x, y, body);

        //if (helmetOn) {g2d.drawImage(helmet, x, y, null);}
        if (isAttacking) {
            switch (direction) {
                case "up":
                    g2d.drawImage(attack, x, y - gp.tileSize / 2 - gp.tileSize * attackCounter / 5, null);
                    break;
                case "down":
                    g2d.drawImage(attack, x, y + gp.tileSize / 2 + gp.tileSize * attackCounter / 5, null);
                    break;
                case "left":
                    g2d.drawImage(attack, x - gp.tileSize / 2 - gp.tileSize * attackCounter / 5, y, null);
                    break;
                case "right":
                    g2d.drawImage(attack, x + +gp.tileSize / 2 + gp.tileSize * attackCounter / 5, y, null);
                    break;
            }
        }
    }

    private void drawitems(Graphics2D g2d, int x, int y, BufferedImage body) {
        //this now considers the order of the drawing of the items
        switch (direction) {
            case "down":
                g2d.drawImage(body, x, y, null);
                if (boots != null) {
                    g2d.drawImage(boots.down, x, y, null);
                }
                if (armour != null) {
                    g2d.drawImage(armour.down, x, y, null);
                }
                if (helmet != null) {
                    g2d.drawImage(helmet.down, x, y, null);
                }
                if (weapon != null) {
                    g2d.drawImage(weapon.down, x, y, null);
                }
                if (shield != null) {
                    g2d.drawImage(shield.down, x, y, null);
                }
                break;
            case "up":
                if (shield != null) {
                    g2d.drawImage(shield.up, x, y, null);
                }
                g2d.drawImage(body, x, y, null);
                if (weapon != null) {
                    g2d.drawImage(weapon.up, x, y, null);
                }
                if (boots != null) {
                    g2d.drawImage(boots.up, x, y, null);
                }
                if (armour != null) {
                    g2d.drawImage(armour.up, x, y, null);
                }
                if (helmet != null) {
                    g2d.drawImage(helmet.up, x, y, null);
                }
                break;
            case "left":
                g2d.drawImage(body, x, y, null);
                if (helmet != null) {
                    g2d.drawImage(helmet.left, x, y, null);
                }
                if (boots != null) {
                    g2d.drawImage(boots.left, x, y, null);
                }
                if (armour != null) {
                    g2d.drawImage(armour.left, x, y, null);
                }
                if (weapon != null) {
                    g2d.drawImage(weapon.left, x, y, null);
                }
                if (shield != null) {
                    g2d.drawImage(shield.left, x, y, null);
                }
                break;
            case "right":
                g2d.drawImage(body, x, y, null);
                if (helmet != null) {
                    g2d.drawImage(helmet.right, x, y, null);
                }
                if (boots != null) {
                    g2d.drawImage(boots.right, x, y, null);
                }
                if (armour != null) {
                    g2d.drawImage(armour.right, x, y, null);
                }
                if (weapon != null) {
                    g2d.drawImage(weapon.right, x, y, null);
                }
                if (shield != null) {
                    g2d.drawImage(shield.right, x, y, null);
                }
                break;


        }


        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).equippable) {
                if (inventory.get(i).name == "firefly") {
                    g2d.drawImage(inventory.get(i).image, x, y - gp.tileSize, null);
                } else {
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
    }

    public void giveItem(SuperObject item) {
        if (Objects.equals(item.type, "helmet")) {
            helmet = item;
            this.maxHealth += 1;
            this.currentHealth += 1;
        } else if (Objects.equals(item.type, "armour")) {
            armour = item;
            this.maxHealth += 2;
            this.currentHealth += 2;
        } else if (Objects.equals(item.type, "weapon")) {
            weapon = item;

        } else if (Objects.equals(item.type, "shield")) {
            shield = item;
        } else if (Objects.equals(item.type, "boots")) {
            boots = item;
            this.speed += 2;
        } else {
            inventory.add(item);
        }
        if (Objects.equals(item.name, "firefly")) {       //kinda done
            gp.lightSize += 100;

        }
    }
}