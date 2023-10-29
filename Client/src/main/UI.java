package main;

import java.awt.*;

public class UI {
    GamePanel gp;
    Graphics2D g2d;
    Font TimesRoman;

    public int commandNum = 0;

    public UI(GamePanel gp) {
        this.gp = gp;

        TimesRoman = new Font("TimesRoman", Font.PLAIN, 40);
    }

    public void draw(Graphics2D g2d) {

        this.g2d = g2d;

        g2d.setFont(TimesRoman);
        g2d.setColor(Color.WHITE);
        
        if(gp.gameState == gp.titleState) {
            drawTitleScreen(g2d);
        }
        
        


    }

    private void drawTitleScreen(Graphics2D g2d) {

        //TITLE NAME
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 100));
        String text = "HOWDY";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        //SHADOW
        g2d.setColor(Color.gray);
        g2d.drawString(text, x + 3, y + 3);

        //MAIN TEXT COLOR
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);

        //HERO IMAGE
        int scale = 3;
        x = gp.screenWidth/2 - (gp.tileSize * scale)/2;
        y += gp.tileSize;
        g2d.drawImage(gp.player.titleArt, x, y, gp.tileSize*scale, gp.tileSize*scale, null);

        //MENU
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 48f));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 5;
        g2d.drawString(text, x, y);
        if(commandNum == 0){
            g2d.drawString(">", x-gp.tileSize, y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2d.drawString(text, x, y);
        if(commandNum == 1){
            g2d.drawString(">", x-gp.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2d.drawString(text, x, y);
        if(commandNum == 2){
            g2d.drawString(">", x-gp.tileSize, y);
        }

    }

    public int getXforCenteredText(String text){

        int length = (int)g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return (gp.screenWidth - length) / 2;

    }
}