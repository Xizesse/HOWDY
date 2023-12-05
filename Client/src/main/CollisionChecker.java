package main;

import entity.Entity;
import entity.NPC_Player;
import monster.Monster_Spike;

import java.util.List;

public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        //check if the entity is colliding with a solid tile

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;                                  // entity's worldX coordinate of left most edge of the solid area
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;        // entity's worldX coordinate of right most edge of the solid area
        int entityTopWorldY = entity.worldY + entity.solidArea.y;                                   // entity's worldY coordinate of top most edge of the solid area
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;      // entity's worldY coordinate of bottom most edge of the solid area

        int entityLeftCol = entityLeftWorldX/gp.tileSize;       // tile column of left most edge of the solid area
        int entityRightCol = entityRightWorldX/gp.tileSize;     // tile column of right most edge of the solid area
        int entityTopRow = entityTopWorldY/gp.tileSize;         // tile row of top most edge of the solid area
        int entityBottomRow = entityBottomWorldY/gp.tileSize;   // tile row of bottom most edge of the solid area

        int tileNum1, tileNum2; // numerical id of the tile that the entity is currently on top of

        //based on the direction and speed of the entity, choose the correct vertices to check for collision
        //check if the tile that the entity is moving into is solid
        //if it is, set collisionOn to true
        switch (entity.direction) {
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed)/ gp.tileSize;                 // add speed to the left most edge of the solid area to get the new left most edge of the solid area
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];                    // get the tile index beneath the top vertex of the left most edge of the solid area
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];                 // get the tile index beneath the bottom vertex of the left most edge of the solid area
                if (gp.tileM.tile[tileNum1].Collision || gp.tileM.tile[tileNum2].Collision){    // check if either of the tiles are solid
                    entity.collisionOn = true;                                                  // if they are, set collisionOn to true
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/ gp.tileSize;               // add speed to the right most edge of the solid area to get the new right most edge of the solid area
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];                   // get the tile index beneath the top vertex of the right most edge of the solid area
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];                // get the tile index beneath the bottom vertex of the right most edge of the solid area
                if (gp.tileM.tile[tileNum1].Collision || gp.tileM.tile[tileNum2].Collision){    // check if either of the tiles are solid
                    entity.collisionOn = true;                                                  // if they are, set collisionOn to true
                }

                break;
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;                    // add speed to the top most edge of the solid area to get the new top most edge of the solid area
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];                    // get the tile index beneath the left vertex of the top most edge of the solid area
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];                   // get the tile index beneath the right vertex of the top most edge of the solid area
                if (gp.tileM.tile[tileNum1].Collision || gp.tileM.tile[tileNum2].Collision){    // check if either of the tiles are solid
                    entity.collisionOn = true;                                                  // if they are, set collisionOn to true
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;              // add speed to the bottom most edge of the solid area to get the new bottom most edge of the solid area
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];                 // get the tile index beneath the left vertex of the bottom most edge of the solid area
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];                // get the tile index beneath the right vertex of the bottom most edge of the solid area
                if (gp.tileM.tile[tileNum1].Collision || gp.tileM.tile[tileNum2].Collision){    // check if either of the tiles are solid
                    entity.collisionOn = true;                                                  // if they are, set collisionOn to true
                }
                break;
        }

    }

    public int checkObject(Entity entity, boolean player) {
        //check if the entity is colliding with an object
        int index = 999;

        for (int i = 0; i < gp.obj.length; i++) {

            if (gp.obj[i] == null) {
                continue;
            }

            // Entity solid area position
            entity.solidArea.x = entity.worldX + entity.solidArea.x; //get the entity worldX coordinate of the left most edge of the solid area
            entity.solidArea.y = entity.worldY + entity.solidArea.y; //get the entity worldY coordinate of the top most edge of the solid area

            // Object's solid area position
            gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x; //get the object worldX coordinate of the left most edge of the solid area
            gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y; //get the object worldY coordinate of the top most edge of the solid area

            switch (entity.direction){

                case "up":
                    entity.solidArea.y -= entity.speed;                         //get next position of the top most edge of the solid area
                    if (entity.solidArea.intersects(gp.obj[i].solidArea)){      //check if the entity's solid area intersects with the object's solid area
                        if (gp.obj[i].collision){                               //check if the object is solid
                            entity.collisionOn = true;                          //if it is, set collisionOn to true
                        }
                        if(player) {
                            index = i;
                        }
                    }
                    break;
                    //repeat for the other directions
                case "down":
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                        if (gp.obj[i].collision){
                            entity.collisionOn = true;
                        }
                        if(player) {                //if the entity is the player, set index to the object's index
                            index = i;              //this is used to pick up the object
                        }
                    }
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                        if (gp.obj[i].collision){
                            entity.collisionOn = true;
                        }
                        if(player) {
                            index = i;
                        }
                    }
                    break;
                case "right":
                    entity.solidArea.x += entity.speed;
                    if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                        if (gp.obj[i].collision){
                            entity.collisionOn = true;
                        }
                        if(player) {
                            index = i;
                        }
                    }
                    break;

            }
            entity.solidArea.x = entity.solidAreaDefaultX;          //reset the entity's solid area position
            entity.solidArea.y = entity.solidAreaDefaultY;          //reset the entity's solid area position
            gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;    //reset the object's solid area position
            gp.obj[i].solidArea.y = gp.obj[i].SolidAreaDefaultY;    //reset the object's solid area position
        }
        return index;   //return the index of the object that the entity is colliding with
    }
    public int checkEntity(Entity entity, Entity[] target) {
        //check if the entity is colliding with another entity

        int index = 999;
        // Entity solid area position

        for (int i = 0; i < target.length; i++) {
            if (target[i] == null) {
                continue;
            }
            entity.solidArea.x = entity.worldX + entity.solidArea.x; //get the entity worldX coordinate of the left most edge of the solid area
            entity.solidArea.y = entity.worldY + entity.solidArea.y; //get the entity worldY coordinate of the top most edge of the solid area
            //System.out.println("Checking entity "+i);


            // targetEntity solid area position
            target[i].solidArea.x = target[i].worldX + target[i].solidArea.x; //get the object worldX coordinate of the left most edge of the solid area
            target[i].solidArea.y = target[i].worldY + target[i].solidArea.y; //get the object worldY coordinate of the top most edge of the solid area

            switch (entity.direction) {
                case "up": entity.solidArea.y -= entity.speed; break;
                case "down": entity.solidArea.y += entity.speed; break;
                case "left": entity.solidArea.x -= entity.speed; break;
                case "right": entity.solidArea.x += entity.speed; break;
            }

            //print solid areas
//            System.out.println("Entity solid area: "+(entity.solidArea.x/ gp.tileSize)+","+(entity.solidArea.y/ gp.tileSize));
//            System.out.println("Target solid area: "+target[i].solidArea.x/ gp.tileSize+","+target[i].solidArea.y/ gp.tileSize);

            if (entity.solidArea.intersects(target[i].solidArea)) {
            //check if the entity's solid area intersects with the object's solid area
                if(target[i] != entity) {                               //prevents entity from colliding with itself
                    entity.collisionOn = true;
                    //if it does, set collisionOn to true
                    index = i;
                }
            }


            entity.solidArea.x = entity.solidAreaDefaultX;          //reset the entity's solid area position
            entity.solidArea.y = entity.solidAreaDefaultY;          //reset the entity's solid area position
            target[i].solidArea.x = target[i].solidAreaDefaultX;    //reset the object's solid area position
            target[i].solidArea.y = target[i].solidAreaDefaultY;    //reset the object's solid area position
        }
        return index;
    }


    public void checkNPC_players(Entity entity, List<NPC_Player> target) {

        // Entity solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x; //get the entity worldX coordinate of the left most edge of the solid area
        entity.solidArea.y = entity.worldY + entity.solidArea.y; //get the entity worldY coordinate of the top most edge of the solid area

        for(NPC_Player npc_player : target){
            if(npc_player == null){
                continue;
            }
            // Player's Solid Area Position
            npc_player.solidArea.x = npc_player.worldX + npc_player.solidArea.x; //get the player worldX coordinate of the left most edge of the solid area
            npc_player.solidArea.y = npc_player.worldY + npc_player.solidArea.y; //get the player worldY coordinate of the top most edge of the solid area

            switch (entity.direction){

                case "up":
                    entity.solidArea.y -= entity.speed;                         //get next position of the top most edge of the solid area
                    if (entity.solidArea.intersects(npc_player.solidArea)){      //check if the entity's solid area intersects with the object's solid area
                            entity.collisionOn = true;                          //if it does, set collisionOn to true
                    }
                    break;
                    //repeat for the other directions
                case "down":
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(npc_player.solidArea)){
                        entity.collisionOn = true;
                    }
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    if (entity.solidArea.intersects(npc_player.solidArea)){
                        entity.collisionOn = true;
                    }
                    break;
                case "right":
                    entity.solidArea.x += entity.speed;
                    if (entity.solidArea.intersects(npc_player.solidArea)){
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
}
