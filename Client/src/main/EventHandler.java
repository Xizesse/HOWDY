package main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    Rectangle eventRect;
    int eventDefaultX, eventDefaultY;
    public EventHandler(GamePanel gp){
        this.gp = gp;
        eventRect = new Rectangle(23, 23,2,2);
        eventDefaultX = eventRect.x;
        eventDefaultY = eventRect.y;
    }

    public void checkEvent(){
        if(hit(30,9,"any")){
            dmgPit(gp.readState);
        }
        if(hit(30, 21, "any")){
            teleport(gp.readState, 30, 7);
        }
    }
    public boolean hit(int eventCol, int eventRow, String reqDirection){
        boolean hit = false;
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect.x = eventCol * gp.tileSize + eventRect.x;
        eventRect.y = eventRow * gp.tileSize + eventRect.y;

        if ( gp.player.solidArea.intersects(eventRect) && (gp.player.direction.equals(reqDirection) || reqDirection.equals("any"))){
            hit = true;
            System.out.println("hit");
        }
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect.x = eventDefaultX;
        eventRect.y = eventDefaultY;

        return hit;
    }
    public void dmgPit(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentText = "You fell into a pit!";
        gp.player.currentHealth -= 1;

    }
    public void teleport(int gameState, int newX, int newY) {
        gp.gameState = gameState;
        gp.ui.currentText = "Get teleported, idiot";
        gp.player.worldX = newX * gp.tileSize;
        gp.player.worldY = newY * gp.tileSize;
    }
}
