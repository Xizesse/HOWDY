package main;

import entity.Entity;
import entity.NPC_Player;
import monster.Monster_Spike;
import object.OBJ_PP;
import object.OBJ_RuneDoor;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        //check if the entity is colliding with a solid tile

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;                                  // entity's worldX coordinate of left most edge of the solid area
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;        // entity's worldX coordinate of right most edge of the solid area
        int entityTopWorldY = entity.worldY + entity.solidArea.y;                                   // entity's worldY coordinate of top most edge of the solid area
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;      // entity's worldY coordinate of bottom most edge of the solid area

        int entityLeftCol = entityLeftWorldX / gp.tileSize;       // tile column of left most edge of the solid area
        int entityRightCol = entityRightWorldX / gp.tileSize;     // tile column of right most edge of the solid area
        int entityTopRow = entityTopWorldY / gp.tileSize;         // tile row of top most edge of the solid area
        int entityBottomRow = entityBottomWorldY / gp.tileSize;   // tile row of bottom most edge of the solid area

        int tileNum1, tileNum2; // numerical id of the tile that the entity is currently on top of

        //based on the direction and speed of the entity, choose the correct vertices to check for collision
        //check if the tile that the entity is moving into is solid
        //if it is, set collisionOn to true
        switch (entity.direction) {
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;                 // add speed to the left most edge of the solid area to get the new left most edge of the solid area
                tileNum1 = gp.tileM.mapTileNum[entity.map][entityLeftCol][entityTopRow];                    // get the tile index beneath the top vertex of the left most edge of the solid area
                tileNum2 = gp.tileM.mapTileNum[entity.map][entityLeftCol][entityBottomRow];                 // get the tile index beneath the bottom vertex of the left most edge of the solid area
                if (gp.tileM.tile[tileNum1].Collision || gp.tileM.tile[tileNum2].Collision) {    // check if either of the tiles are solid
                    entity.collisionOn = true;                                                  // if they are, set collisionOn to true
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;               // add speed to the right most edge of the solid area to get the new right most edge of the solid area
                tileNum1 = gp.tileM.mapTileNum[entity.map][entityRightCol][entityTopRow];                   // get the tile index beneath the top vertex of the right most edge of the solid area
                tileNum2 = gp.tileM.mapTileNum[entity.map][entityRightCol][entityBottomRow];                // get the tile index beneath the bottom vertex of the right most edge of the solid area
                if (gp.tileM.tile[tileNum1].Collision || gp.tileM.tile[tileNum2].Collision) {    // check if either of the tiles are solid
                    entity.collisionOn = true;                                                  // if they are, set collisionOn to true
                }

                break;
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;                    // add speed to the top most edge of the solid area to get the new top most edge of the solid area
                tileNum1 = gp.tileM.mapTileNum[entity.map][entityLeftCol][entityTopRow];                    // get the tile index beneath the left vertex of the top most edge of the solid area
                tileNum2 = gp.tileM.mapTileNum[entity.map][entityRightCol][entityTopRow];                   // get the tile index beneath the right vertex of the top most edge of the solid area
                if (gp.tileM.tile[tileNum1].Collision || gp.tileM.tile[tileNum2].Collision) {    // check if either of the tiles are solid
                    entity.collisionOn = true;                                                  // if they are, set collisionOn to true
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;              // add speed to the bottom most edge of the solid area to get the new bottom most edge of the solid area
                tileNum1 = gp.tileM.mapTileNum[entity.map][entityLeftCol][entityBottomRow];                 // get the tile index beneath the left vertex of the bottom most edge of the solid area
                tileNum2 = gp.tileM.mapTileNum[entity.map][entityRightCol][entityBottomRow];                // get the tile index beneath the right vertex of the bottom most edge of the solid area
                if (gp.tileM.tile[tileNum1].Collision || gp.tileM.tile[tileNum2].Collision) {    // check if either of the tiles are solid
                    entity.collisionOn = true;                                                  // if they are, set collisionOn to true
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {
        //check if the entity is colliding with an object
        int index = 999;

        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap] == null) {
                continue;
            }
            if (gp.obj[gp.currentMap][i] == null) {
                continue;
            }

            // Entity solid area position
            entity.solidArea.x = entity.worldX + entity.solidArea.x; //get the entity worldX coordinate of the left most edge of the solid area
            entity.solidArea.y = entity.worldY + entity.solidArea.y; //get the entity worldY coordinate of the top most edge of the solid area



            // Object's solid area position
            gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x; //get the object worldX coordinate of the left most edge of the solid area
            gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y; //get the object worldY coordinate of the top most edge of the solid area

            if (gp.obj[gp.currentMap][i] instanceof OBJ_RuneDoor) {
                gp.obj[gp.currentMap][i].solidArea.x -= 1;
                gp.obj[gp.currentMap][i].solidArea.y -= 1;
            }

            switch (entity.direction) {

                case "up":
                    entity.solidArea.y -= entity.speed;                         //get next position of the top most edge of the solid area
                    if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {      //check if the entity's solid area intersects with the object's solid area
                        if (gp.obj[gp.currentMap][i].collision) {                               //check if the object is solid
                            entity.collisionOn = true;                          //if it is, set collisionOn to true
                        }
                        if (player) {
                            index = i;
                        }
                    }
                    break;
                //repeat for the other directions
                case "down":
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                        if (gp.obj[gp.currentMap][i].collision) {
                            entity.collisionOn = true;
                        }
                        if (player) {                //if the entity is the player, set index to the object's index
                            index = i;              //this is used to pick up the object
                        }
                    }
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                        if (gp.obj[gp.currentMap][i].collision) {
                            entity.collisionOn = true;
                        }
                        if (player) {
                            index = i;
                        }
                    }
                    break;
                case "right":
                    entity.solidArea.x += entity.speed;
                    if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                        if (gp.obj[gp.currentMap][i].collision) {
                            entity.collisionOn = true;
                        }
                        if (player) {
                            index = i;
                        }
                    }
                    break;

            }
            if (gp.obj[gp.currentMap][i] instanceof OBJ_PP && index != i) {
                if (gp.obj[gp.currentMap][i].isItOn) {
                    index = -i;
                }
            }
            entity.solidArea.x = entity.solidAreaDefaultX;          //reset the entity's solid area position
            entity.solidArea.y = entity.solidAreaDefaultY;          //reset the entity's solid area position
            gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;    //reset the object's solid area position
            gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].SolidAreaDefaultY;    //reset the object's solid area position
        }
        return index;   //return the index of the object that the entity is colliding with
    }

    public int checkEntity(Entity entity, Entity[][] target) {
        //check if the entity is colliding with another entity

        int index = 999;
        // Entity solid area position

        for (int i = 0; i < target[1].length; i++) {
            if (target[i] == null) {
                continue;
            }
            entity.solidArea.x = entity.worldX + entity.solidArea.x; //get the entity worldX coordinate of the left most edge of the solid area
            entity.solidArea.y = entity.worldY + entity.solidArea.y; //get the entity worldY coordinate of the top most edge of the solid area


            // targetEntity solid area position
            target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x; //get the object worldX coordinate of the left most edge of the solid area
            target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y; //get the object worldY coordinate of the top most edge of the solid area

            switch (entity.direction) {
                case "up":
                    entity.solidArea.y -= entity.speed;
                    break;
                case "down":
                    entity.solidArea.y += entity.speed;
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    break;
                case "right":
                    entity.solidArea.x += entity.speed;
                    break;
            }


            if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                //check if the entity's solid area intersects with the object's solid area
                if (target[gp.currentMap][i] != entity) {                               //prevents entity from colliding with itself
                    entity.collisionOn = true;
                    //if it does, set collisionOn to true
                    index = i;
                }
            }


            entity.solidArea.x = entity.solidAreaDefaultX;          //reset the entity's solid area position
            entity.solidArea.y = entity.solidAreaDefaultY;          //reset the entity's solid area position
            target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;    //reset the object's solid area position
            target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;    //reset the object's solid area position
        }
        return index;
    }

    public void checkNPC_players(Entity entity, List<NPC_Player> target) {

        // Entity solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x; //get the entity worldX coordinate of the left most edge of the solid area
        entity.solidArea.y = entity.worldY + entity.solidArea.y; //get the entity worldY coordinate of the top most edge of the solid area

        for (NPC_Player npc_player : target) {
            if (npc_player == null) {
                continue;
            }
            // Player's Solid Area Position
            npc_player.solidArea.x = npc_player.worldX + npc_player.solidArea.x; //get the player worldX coordinate of the left most edge of the solid area
            npc_player.solidArea.y = npc_player.worldY + npc_player.solidArea.y; //get the player worldY coordinate of the top most edge of the solid area

            switch (entity.direction) {

                case "up":
                    entity.solidArea.y -= entity.speed;                         //get next position of the top most edge of the solid area
                    if (entity.solidArea.intersects(npc_player.solidArea)) {      //check if the entity's solid area intersects with the object's solid area
                        entity.collisionOn = true;                          //if it does, set collisionOn to true
                    }
                    break;
                //repeat for the other directions
                case "down":
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(npc_player.solidArea)) {
                        entity.collisionOn = true;
                    }
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    if (entity.solidArea.intersects(npc_player.solidArea)) {
                        entity.collisionOn = true;
                    }
                    break;
                case "right":
                    entity.solidArea.x += entity.speed;
                    if (entity.solidArea.intersects(npc_player.solidArea)) {
                        entity.collisionOn = true;
                    }
                    break;

            }
            entity.solidArea.x = entity.solidAreaDefaultX;          //reset the entity's solid area position
            entity.solidArea.y = entity.solidAreaDefaultY;          //reset the entity's solid area position
            npc_player.solidArea.x = npc_player.solidAreaDefaultX;    //reset the object's solid area position
            npc_player.solidArea.y = npc_player.solidAreaDefaultY;    //reset the object's solid area position
        }
    }

    public boolean checkAttack(Entity player, Entity target) {

        target.solidAreaDefaultX = target.solidArea.x; //save the default solid area position
        target.solidAreaDefaultY = target.solidArea.y; //save the default solid area position

        target.solidArea.x = target.worldX + target.solidArea.x; //get the entity worldX coordinate of the left most edge of the solid area
        target.solidArea.y = target.worldY + target.solidArea.y; //get the entity worldY coordinate of the top most edge of the solid area

        Rectangle attackArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);

        //get attack area
        switch (player.direction) {
            case "up":
                attackArea.x = player.worldX;
                attackArea.y = player.worldY - gp.tileSize;
                break;
            //repeat for the other directions
            case "down":
                attackArea.x = player.worldX;
                attackArea.y = player.worldY + gp.tileSize;
                break;
            case "left":
                attackArea.x = player.worldX - gp.tileSize;
                attackArea.y = player.worldY;
                break;
            case "right":
                attackArea.x = player.worldX + gp.tileSize;
                attackArea.y = player.worldY;
                break;

        }

        boolean result = attackArea.intersects(target.solidArea);

        target.solidArea.x = target.solidAreaDefaultX;          //reset the entity's solid area position
        target.solidArea.y = target.solidAreaDefaultY;          //reset the entity's solid area position

        return result;
    }
}
