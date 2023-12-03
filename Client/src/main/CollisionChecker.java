package main;

import entity.Entity;
import entity.NPC_Player2;

import java.lang.reflect.Array;
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
            entity.solidArea.x = entity.worldX + entity.solidArea.x;
            entity.solidArea.y = entity.worldY + entity.solidArea.y;

            // Object's solid area position
            gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
            gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

            switch (entity.direction){

                case "up":
                    entity.solidArea.y -= entity.speed;
                    if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                        if (gp.obj[i].collision){
                            entity.collisionOn = true;
                        }
                        if(player) {
                            index = i;
                        }
                    }
                    break;
                case "down":
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                        if (gp.obj[i].collision){
                            entity.collisionOn = true;
                        }
                        if(player) {
                            index = i;
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
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
            gp.obj[i].solidArea.y = gp.obj[i].SolidAreaDefaultY;
        }
        return index;
    }
    public int checkEntity(Entity entity, Entity[] target){
        int index = 999;

        for (int i = 0; i < target.length; i++) {

            if (target[i] != null) {

                // Entity solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Object's solid area position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                switch (entity.direction){

                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(target[i].solidArea)){
                                entity.collisionOn = true;
                                System.out.println("Collision with entity: "+i);
                                index = i;
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(target[i].solidArea)){

                                entity.collisionOn = true;
                            System.out.println("Collision with entity: "+i);

                                index = i;
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(target[i].solidArea)){

                                entity.collisionOn = true;
                            System.out.println("Collision with entity: "+i);



                                index = i;

                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(target[i].solidArea)){
                                entity.collisionOn = true;
                            System.out.println("Collision with entity: "+i);

                                index = i;

                        }
                        break;

                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return index;
    }
    public void checkPlayer(Entity entity)
    {
            // Entity solid area position
            entity.solidArea.x = entity.worldX + entity.solidArea.x;
            entity.solidArea.y = entity.worldY + entity.solidArea.y;

            // Object's solid area position
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

            switch (entity.direction){

                case "up":
                    entity.solidArea.y -= entity.speed;
                    if (entity.solidArea.intersects(gp.player.solidArea)){
                        entity.collisionOn = true;

                    }
                    break;
                case "down":
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(gp.player.solidArea)){

                        entity.collisionOn = true;
                    }
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    if (entity.solidArea.intersects(gp.player.solidArea)){

                        entity.collisionOn = true;


                    }
                    break;
                case "right":
                    entity.solidArea.x += entity.speed;
                    if (entity.solidArea.intersects(gp.player.solidArea)){
                        entity.collisionOn = true;


                    }
                    break;

            }
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;

    }

    public int checkNPC_players(Entity entity, List<NPC_Player2> target) {
        int index = 999;
        ;
        for (int i = 0; i < target.size(); i++) {
            NPC_Player2 targetEntity = target.get(i);

            if (targetEntity != null) {
                //System.out.println("Checking collision with entity: " + i);
                // Entity solid area position
                int entitySolidAreaX = entity.worldX + entity.solidArea.x;
                int entitySolidAreaY = entity.worldY + entity.solidArea.y;

                // Target's solid area position
                int targetSolidAreaX = targetEntity.worldX + targetEntity.solidArea.x;
                int targetSolidAreaY = targetEntity.worldY + targetEntity.solidArea.y;
                //System.out.println("entitySolidAreaX: " + entitySolidAreaX);
                //System.out.println("entitySolidAreaY: " + entitySolidAreaY);
                //System.out.println("targetSolidAreaX: " + targetSolidAreaX);
                //System.out.println("targetSolidAreaY: " + targetSolidAreaY);

                // Temporarily move the entity's solid area for collision checking
                switch (entity.direction) {

                    case "up":
                        if (entity.solidArea.intersects(targetEntity.worldX, targetEntity.worldY - entity.speed, targetEntity.solidArea.width, targetEntity.solidArea.height)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "down":
                        if (entity.solidArea.intersects(targetEntity.worldX, targetEntity.worldY + entity.speed, targetEntity.solidArea.width, targetEntity.solidArea.height)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "left":
                        if (entity.solidArea.intersects(targetEntity.worldX - entity.speed, targetEntity.worldY, targetEntity.solidArea.width, targetEntity.solidArea.height)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "right":
                        if (entity.solidArea.intersects(targetEntity.worldX + entity.speed, targetEntity.worldY, targetEntity.solidArea.width, targetEntity.solidArea.height)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                }
            }
        }
        return index;
    }

}
