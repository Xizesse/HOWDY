package main;

import object.OBJ_Heart;
import object.SuperObject;
import org.w3c.dom.Text;

import java.awt.*;
import java.awt.desktop.AppReopenedEvent;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gp;
    //Graphics2D g2d;
    Font TimesRoman;
    BufferedImage heartFull, heartHalf, heartEmpty;

    public int commandNum = 0;
    int subState = 0;
    public String currentText = "" ;

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
        if(gp.gameState == gp.titleState){
            drawTitleScreen(g2d);
        }

        if(gp.gameState == gp.playState){
            drawPlayerLife(g2d);
            drawInventory(g2d);
            drawInstructions(g2d);

        }

        if (gp.gameState == gp.pauseState){
            drawPauseScreen(g2d);
            drawPlayerLife(g2d);
            drawInventory(g2d);


        }
        //read state
        if (gp.gameState == gp.readState) {
            drawReadScreen(g2d);
            drawPlayerLife(g2d);
            drawInventory(g2d);
            drawInstructions(g2d);
        }
        //options state
        if(gp.gameState == gp.optionsState){
            if(gp.prev_gameState == gp.playState){
                drawPlayerLife(g2d);
                drawInventory(g2d);
                drawInstructions(g2d);
            }
            else if(gp.prev_gameState == gp.pauseState){
                drawPauseScreen(g2d);
                drawPlayerLife(g2d);
                drawInventory(g2d);
            }
            drawOptionsScreen(g2d);
        }
    }

    private void drawPlayerLife(Graphics2D g2d) {
        //gp.player.currentHealth = 3;

        int x =  gp.tileSize/2;
        //int y = gp.screenWidth - gp.tileSize - gp.tileSize/2 ;
        int y = 0;
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

    private void drawInventory(Graphics2D g2d) {



        int x = gp.screenWidth2 - 3* gp.tileSize - gp.tileSize/2;
        int y =  gp.screenWidth2 - gp.tileSize*4 - gp.tileSize ;

        //black square with transparency and a white border
        Color b = new Color(0,0,0, 100);
        g2d.setColor(b);
        g2d.fillRect(x, y, gp.tileSize*3, gp.tileSize*3);
        Color w = new Color(200,200,200, 100);
        g2d.setColor(w);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, gp.tileSize*3, gp.tileSize*3);
        g2d.drawRect(x+ gp.tileSize, y, gp.tileSize, gp.tileSize*3);
        g2d.drawRect( x, y+gp.tileSize, gp.tileSize*3, gp.tileSize);


        x += gp.tileSize;

        if (gp.player.helmet != null) g2d.drawImage(gp.player.helmet.image, x, y, null);
        y += gp.tileSize;

        if (gp.player.armour != null) g2d.drawImage(gp.player.armour.image, x, y, null);
        y += gp.tileSize;

        if (gp.player.boots != null) g2d.drawImage(gp.player.boots.image, x, y, null);
        y -= gp.tileSize;
        x -= gp.tileSize;

        if(gp.player.shield != null) g2d.drawImage(gp.player.shield.image, x, y, null);
        x+= gp.tileSize*2;

        if(gp.player.weapon != null) g2d.drawImage(gp.player.weapon.image, x, y, null);


        x =  gp.screenWidth - gp.tileSize - gp.tileSize/2;
        y = gp.screenWidth - gp.tileSize - gp.tileSize/2 ;


        for (int i = 0; i < gp.player.inventory.size(); i++) {

            //draw a small black square
            g2d.setColor(b);
            g2d.fillRect(x, y, gp.tileSize, gp.tileSize);
            g2d.setColor(w);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(x, y, gp.tileSize, gp.tileSize);


            g2d.drawImage(gp.player.inventory.get(i).image, x, y, null);
            x -= gp.tileSize;
            }

    }

    private void drawInstructions(Graphics2D g2d) {

        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 20f));
        g2d.setColor(Color.WHITE);
        String text = "P - Pause";
        int x = getXforCenteredText(text, g2d);
        int y = gp.tileSize;
        g2d.drawString(text, x, y);
        text = "L - Light Effects";
        x = getXforCenteredText(text, g2d);
        y = gp.tileSize*2;
        g2d.drawString(text, x, y);
        if(gp.GOD) g2d.setColor(Color.red);
        text = "G - GOD MODE";
        x = getXforCenteredText(text, g2d);
        y = gp.tileSize*3;
        g2d.drawString(text, x, y);

    }

    public void drawPauseScreen(Graphics2D g2d){
        String text = "PAUSED";
        int x = getXforCenteredText(text, g2d);
        int y = gp.screenHeight/2;

        g2d.drawString(text, x, y);
    }

    private void drawTitleScreen(Graphics2D g2d) {

        //TITLE NAME

        g2d.setColor(Color.black);
        g2d.drawRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2d.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
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

    public void drawOptionsScreen(Graphics2D g2d){
        int x = gp.tileSize*4;
        int y = gp.tileSize*2;
        int width = gp.tileSize*8;
        int height = gp.tileSize*10;
        Color c = new Color(0, 0, 0, 210);
        g2d.setColor(c);
        g2d.fillRoundRect(x, y, width, height, 35, 35);
        c = new Color(255, 255, 255);
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);

        switch(subState){
            case 0: options_top(x, y, g2d); break;
            case 1: options_fullScreenNotification(x,y, g2d); break;
            case 2: options_control(x, y, g2d); break;
            case 3: options_endGameConfirmation(x, y, g2d); break;
        }

        gp.keyH.spacePressed = false;
    }

    public void options_top(int frameX, int frameY, Graphics2D g2d){
        int textX;
        int textY;
        String text = "Options";
        textX = getXforCenteredText(text, g2d);
        textY = frameY + gp.tileSize;
        g2d.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize*2;
        g2d.drawString("Full Screen", textX, textY);
        if(commandNum == 0){
            g2d.drawString(">", textX-25, textY);
            if(gp.keyH.spacePressed){
                if(!gp.fullScreenOn){
                    gp.fullScreenOn = true;
                }
                else if(gp.fullScreenOn){
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }

        textY += gp.tileSize;
        g2d.drawString("Music", textX, textY);
        if(commandNum == 1){
            g2d.drawString(">", textX-25, textY);
        }

        textY += gp.tileSize;
        g2d.drawString("SE", textX, textY);
        if(commandNum == 2){
            g2d.drawString(">", textX-25, textY);
        }

        textY += gp.tileSize;
        g2d.drawString("Control", textX, textY);
        if(commandNum == 3){
            g2d.drawString(">", textX-25, textY);
            if(gp.keyH.spacePressed){
                subState = 2;
                commandNum = 0;
            }
        }

        textY += gp.tileSize;
        g2d.drawString("End Game", textX, textY);
        if(commandNum == 4){
            g2d.drawString(">", textX-25, textY);
            if(gp.keyH.spacePressed){
                subState = 3;
                commandNum = 0;
            }
        }


        textY += gp.tileSize*2;
        g2d.drawString("Back", textX, textY);
        if(commandNum == 5){
            g2d.drawString(">", textX-25, textY);
            if(gp.keyH.spacePressed){
                gp.optionsBack = 1;
                commandNum = 0;
            }
        }

        textX = frameX + (int)(gp.tileSize*4.5);
        textY = frameY + gp.tileSize*2 + 24;
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(textX, textY, 24, 24);
        if(gp.fullScreenOn){
            g2d.fillRect(textX, textY, 24, 24);
        }

        textY += gp.tileSize;
        g2d.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24 * gp.music.volumeScale;
        g2d.fillRect(textX, textY, volumeWidth, 24);

        textY += gp.tileSize;
        g2d.drawRect(textX, textY, 120, 24);
        //volumeWidth = 24 * gp.se.volumeScale;
        g2d.fillRect(textX, textY, volumeWidth, 24);

        gp.config.saveConfig();
    }

    public void options_fullScreenNotification(int frameX, int frameY, Graphics2D g2d){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;

        currentText = "The change will take \neffect after restarting \nthe game.";

        for(String line: currentText.split("\n")){
            g2d.drawString(line, textX, textY);
            textY += 40;
        }

        textY = frameY + gp.tileSize*9;
        g2d.drawString("Back", textX, textY);
        if(commandNum == 0){
            g2d.drawString(">", textX-25, textY);
            if(gp.keyH.spacePressed){
                subState = 0;
            }
        }
    }
    public void options_control(int frameX, int frameY, Graphics2D g2d){
        int textX;
        int textY;

        String text = "Control";
        textX = getXforCenteredText(text, g2d);
        textY = frameY + gp.tileSize;
        g2d.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2d.drawString("Move", textX, textY); textY += gp.tileSize;
        g2d.drawString("Confirm/Attack", textX, textY); textY += gp.tileSize;
        g2d.drawString("Shoot/Cast", textX, textY); textY += gp.tileSize;
        g2d.drawString("Character Screen", textX, textY); textY += gp.tileSize;
        g2d.drawString("Pause", textX, textY); textY += gp.tileSize;
        g2d.drawString("Options", textX, textY); textY += gp.tileSize;

        textX = frameX + gp.tileSize*6;
        textY = frameY + gp.tileSize*2;
        g2d.drawString("ARROWS", textX, textY); textY += gp.tileSize;
        g2d.drawString("SPACE", textX, textY); textY += gp.tileSize;
        g2d.drawString("F", textX, textY); textY += gp.tileSize;
        g2d.drawString("C", textX, textY); textY += gp.tileSize;
        g2d.drawString("P", textX, textY); textY += gp.tileSize;
        g2d.drawString("ESC", textX, textY); textY += gp.tileSize;

        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize*9;
        g2d.drawString("Back", textX, textY);
        if(commandNum == 0){
            g2d.drawString(">", textX-25, textY);
            if(gp.keyH.spacePressed){
                subState = 0;
                commandNum = 3;
            }
        }
    }
    public void options_endGameConfirmation(int x, int y, Graphics2D g2d){
        int textX = x + gp.tileSize;
        int textY = y + gp.tileSize*3;

        currentText = "Quit the game and \nreturn to the title screen?";
        for(String line: currentText.split("\n")){
            g2d.drawString(line, textX, textY);
            textY += 40;
        }

        String text = "Yes";
        textX = getXforCenteredText(text, g2d);
        textY += gp.tileSize*3;
        g2d.drawString(text, textX, textY);
        if(commandNum == 0){
            g2d.drawString(">", textX-25, textY);
            if(gp.keyH.spacePressed){
               subState = 0;
               gp.gameState = gp.titleState;
               gp.new_gameState = gp.titleState;
            }
        }

        text = "No";
        textX = getXforCenteredText(text, g2d);
        textY += gp.tileSize;
        g2d.drawString(text, textX, textY);
        if(commandNum == 1){
            g2d.drawString(">", textX-25, textY);
            if(gp.keyH.spacePressed){
                subState = 0;
                commandNum = 4;
            }
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
