package main;

import net.Packet05Health;

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
        if (gp.player.currentHealth <= 0){
            death(gp.readState, 0, 3, 15);
        }
        if (canTouch) {
            if (hit(0, 30, 9, "any")) {
                dmgPit(gp.readState);
            }

            //map 1 to 2
            if (hit(0, 30, 15, "right")) {
                teleport( gp.playState, 1, 1, 15);
            }
            //map 2 to 1
            if (hit(1, 1, 15, "left")) {
                teleport( gp.playState, 0,  30, 15);
            }
            //map 2 to 3
            if (hit(1, 30, 15, "right")) {
                gp.LIGHT = !gp.LIGHT;
                teleport( gp.playState, 2,  1, 15);
            }
            //map 3 to 2
            if (hit(2, 1, 15, "left")) {
                gp.LIGHT = !gp.LIGHT;
                teleport( gp.playState, 1,  30, 15);
            }

            //map 2 to 4
            if (hit(1, 15, 1, "up")) {
                teleport( gp.playState, 3,  15, 30);
            }
            //map 4 to 2
            if (hit(3, 15, 30, "down")) {
                teleport( gp.playState, 1,  15, 1);
            }

            //map 2 to 5
            if (hit(1, 15, 30, "down")) {
                teleport( gp.playState, 4,  15, 1);
            }
            //map 5 to 2
            if (hit(4, 15, 1, "up")) {
                teleport( gp.playState, 1,  15, 30);
            }

            //infinite loop on bridge
            if (hit(2, 19, 10, "right")) {
                teleport( gp.playState, 2,  18, gp.player.worldY/ gp.tileSize);
                System.out.println("Teleorting from "+ gp.player.worldX + ", " + gp.player.worldY + " to " + gp.player.worldX + ", " + gp.player.worldY/ gp.tileSize);
            }
            if (hit(2, 19, 11, "right")) {
                teleport( gp.playState, 2,  18, gp.player.worldY/ gp.tileSize);
                System.out.println("Teleorting from "+ gp.player.worldX + ", " + gp.player.worldY + " to " + gp.player.worldX + ", " + gp.player.worldY/ gp.tileSize);

            }
            if (hit(2, 19, 12, "right")) {
                System.out.println("Teleporting from "+ gp.player.worldX + ", " + gp.player.worldY + " to " + gp.player.worldX + ", " + gp.player.worldY/ gp.tileSize);

                teleport( gp.playState, 2,  18, gp.player.worldY/ gp.tileSize);
            }
            if (hit(2, 19, 13, "right")) {

                System.out.println("Teleporting from "+ gp.player.worldX + ", " + gp.player.worldY + " to " + gp.player.worldX + ", " + gp.player.worldY/ gp.tileSize);
                teleport( gp.playState, 2,  18, gp.player.worldY/ gp.tileSize);
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
        Packet05Health packet = new Packet05Health(-2, gp.player.currentHealth, gp.currentMap);
        gp.socketClient.sendData(packet.getData());

        canTouch = false;
        //eventRect[col][row].eventDone = true;
    }
    public void teleport(int gameState, int newMap,  int newX, int newY) {
        gp.gameState = gameState;
        gp.ui.currentText = "Get teleported, idiot";
        gp.currentMap = newMap;
        gp.player.map = newMap;
        gp.player.worldX = newX * gp.tileSize;
        gp.player.worldY = newY * gp.tileSize;
    }
    public void death(int gameState, int newMap,  int newX, int newY) {
        gp.gameState = gameState;
        gp.ui.currentText = "YOU DIED!";
        gp.currentMap = newMap;
        gp.player.map = newMap;
        gp.player.currentHealth = gp.player.maxHealth;
        Packet05Health packet = new Packet05Health(-2, gp.player.currentHealth, gp.currentMap);
        gp.socketClient.sendData(packet.getData());
        gp.player.worldX = newX * gp.tileSize;
        gp.player.worldY = newY * gp.tileSize;
    }
}
