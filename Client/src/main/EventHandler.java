package main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    EventRect[][][] eventRect;
    int previousEventX, previousEventY;
    boolean canTouch = true;
    public EventHandler(GamePanel gp){
        this.gp = gp;
        eventRect = new EventRect[gp.maxMaps][gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;
        int map = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow && map < gp.maxMaps) {

            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventDefaultY = eventRect[map][col][row].y;
            row++;
            if (row >= gp.maxWorldRow) {
                col++;
                row = 0;
                if (col >= gp.maxWorldCol) {
                    map++;
                    col = 0;
                }
            }

        }

    }

    public void checkEvent(){
        int xDistance = Math.abs( gp.player.worldX - previousEventX);
        int yDistance = Math.abs( gp.player.worldY - previousEventY);
        int dist = Math.max(xDistance, yDistance);

        if (dist > gp.tileSize){
            canTouch = true;
        }
        if (canTouch) {
            if (hit(0, 30, 9, "any")) {
                dmgPit(gp.readState);
            }
            if (hit(0, 30, 21, "any")) {
                teleport( gp.readState, 0, 30, 7);
            }
            if (hit(0, 30, 15, "any")) {
                teleport( gp.readState, 1,  30, 15);
            }
        }
    }
    public boolean hit(int map, int col, int row, String reqDirection){
        boolean hit = false;

        if (map != gp.currentMap){
            return false;
        }
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
        eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

        if ( !eventRect[map][col][row].eventDone && gp.player.solidArea.intersects(eventRect[map][col][row]) && (gp.player.direction.equals(reqDirection) || reqDirection.equals("any"))){
            hit = true;
            previousEventX = gp.player.worldX;
            previousEventY = gp.player.worldY;
        }
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[map][col][row].x = eventRect[map][col][row].eventDefaultX;
        eventRect[map][col][row].y = eventRect[map][col][row].eventDefaultY;

        return hit;
    }
    public void dmgPit( int gameState) {
        gp.gameState = gameState;
        gp.ui.currentText = "You fell into a pit!";
        gp.player.currentHealth -= 1;
        canTouch = false;
        //eventRect[col][row].eventDone = true;
    }
    public void teleport(int gameState, int newMap,  int newX, int newY) {
        gp.gameState = gameState;
        gp.ui.currentText = "Get teleported, idiot";
        gp.currentMap = newMap;
        gp.player.worldX = newX * gp.tileSize;
        gp.player.worldY = newY * gp.tileSize;
    }
}
