package effects;

import main.GamePanel;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Light {
    GamePanel gp;
    int x,y;
    BufferedImage torchEffect;
    int size;

    public Light(GamePanel gp, int size) {
        this.gp = gp;
        this.size = size;
        updateTorchEffect();
    }

    public void updateTorchEffect() {
        size = gp.lightsize;
        torchEffect = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) torchEffect.getGraphics();
        Area screen = new Area(new Rectangle(0, 0, gp.screenWidth, gp.screenHeight));

        x = gp.player.screenX - size/2 + gp.tileSize/2;
        y = gp.player.screenY - size/2 + gp.tileSize/2;
        // Calculate position based on map position
        if(gp.player.screenX > gp.player.worldX){
            x = gp.player.worldX - size/2 + gp.tileSize/2;
        }
        //top
        if(gp.player.screenY > gp.player.worldY){
            y = gp.player.worldY - size/2 + gp.tileSize/2;
        }
        //right
        if(gp.player.screenX < gp.player.worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth){
            x = gp.player.worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth - size/2 + gp.tileSize/2;
        }
        //bottom
        if(gp.player.screenY < gp.player.worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight){
            y = gp.player.worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight - size/2 + gp.tileSize/2;
        }
        Shape circleShape = new Ellipse2D.Double(x, y, size, size);
        Area circle = new Area(circleShape);
        screen.subtract(circle);

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fill(screen);
        g2.dispose();
    }

    public void draw(Graphics2D g2d) {
        updateTorchEffect(); // Update torch effect based on player's current position
        g2d.drawImage(torchEffect, 0, 0, null);
    }
}