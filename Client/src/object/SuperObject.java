package object;

import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image, image2, image3;
    public BufferedImage up,down,left,right;
    public boolean equippable = false;
    public String name;
    public byte id;
    public String type;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solidAreaDefaultX = 0;
    public int SolidAreaDefaultY = 0;
    UtilityTool uT = new UtilityTool();

    public void draw(Graphics2D g2d, GamePanel gp){
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        //Stop camera movement at the edge of the map
        //left
        if(gp.player.screenX > gp.player.worldX){
            screenX = worldX;
        }
        //top
        if(gp.player.screenY > gp.player.worldY){
            screenY = worldY;
        }
        //right
        if(gp.player.screenX < gp.player.worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth){
            screenX = worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth;
        }
        //bottom
        if(gp.player.screenY < gp.player.worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight){
            screenY = worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight;
        }

        // only draw tiles that are on the screen
        if(worldX > gp.player.worldX - gp.screenWidth &&
                worldX < gp.player.worldX + gp.screenWidth &&
                worldY > gp.player.worldY - gp.screenHeight &&
                worldY < gp.player.worldY + gp.screenHeight){

            g2d.drawImage(image, screenX, screenY, null);
        }
    }
    public void interact(){

    }
    public void readChapter(GamePanel gp) {
    }
}
