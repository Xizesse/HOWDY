package main;

import object.OBJ_Heart;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gp;
    //Graphics2D g2d;
    Font TimesRoman;
    BufferedImage heartFull, heartHalf, heartEmpty;

    public int commandNum = 0;
    public String currentText = "";

    public UI(GamePanel gp) {
        this.gp = gp;
        TimesRoman = new Font("TimesRoman", Font.PLAIN, 40);

        SuperObject heart = new OBJ_Heart(gp);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartEmpty = heart.image3;
    }

    public void draw(Graphics2D g2d) {
        g2d.setFont(TimesRoman);
        g2d.setColor(Color.WHITE);


        //title state
        if(gp.gameState == gp.titleState) {
            drawTitleScreen(g2d);


        }
        if(gp.gameState == gp.playState){

            drawPlayerLife(g2d);
        }

        else if (gp.gameState == gp.pauseState) {
            drawPauseScreen(g2d);
            drawPlayerLife(g2d);

        }
        //read state
        else if (gp.gameState == gp.readState) {
            drawReadScreen(g2d);
            drawPlayerLife(g2d);
        }


    }

    private void drawPlayerLife(Graphics2D g2d) {
        //gp.player.currentHealth = 3;

        int x =  gp.tileSize/2;
        int y = gp.screenWidth - gp.tileSize - gp.tileSize/2 ;
        int i=0;

        while (i < (gp.player.maxHealth / 2)) {
            if (gp.player.currentHealth >= (i + 1) * 2) {

                g2d.drawImage(heartFull, x, y, null);
            } else if (gp.player.currentHealth >= (i * 2) + 1) {

                g2d.drawImage(heartHalf, x, y, null);
            } else {

                g2d.drawImage(heartEmpty, x, y, null);
            }

            i++;
            x += gp.tileSize; // Move to the next position to draw the next heart
        }
    }


    public void drawPauseScreen(Graphics2D g2d){
        String text = "PAUSED";
        int x = getXforCenteredText(text, g2d);
        int y = gp.screenHeight/2;

        g2d.drawString(text, x, y);
    }

    private void drawTitleScreen(Graphics2D g2d) {

        //TITLE NAME
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 100));
        String text = "HOWDY";
        int x = getXforCenteredText(text, g2d);
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

        text = "HOST GAME";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize * 5;
        g2d.drawString(text, x, y);
        if(commandNum == 0){
            g2d.drawString(">", x-gp.tileSize, y);
        }

        text = "JOIN GAME";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize;
        g2d.drawString(text, x, y);
        if(commandNum == 1){
            g2d.drawString(">", x-gp.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize;
        g2d.drawString(text, x, y);
        if(commandNum == 2){
            g2d.drawString(">", x-gp.tileSize, y);
        }

    }

    public int getXforCenteredText(String text, Graphics2D g2d){

        int length = (int)g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return (gp.screenWidth - length) / 2;

    }



    private void drawReadScreen(Graphics2D g2d) {
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - gp.tileSize*4;
        int height = gp.screenHeight -2*gp.tileSize;

        drawSubWindow(x, y, width, height, g2d);
        x += gp.tileSize;
        y += gp.tileSize;
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 30f));
        g2d.setColor(Color.black);
        for(String line: currentText.split("\n")){
            g2d.drawString(line, x, y);
            y += gp.tileSize/1.5;
        }

    }

    public void drawSubWindow(int x, int y , int width, int height, Graphics2D g2d)
    {
        Color c = new Color(200,200,200, 175);
        g2d.setColor(c);
        g2d.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(120,50,19);
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(10));
        g2d.drawRoundRect(x+5, y+5, width-10, height-10, 35, 35);

    }
}
