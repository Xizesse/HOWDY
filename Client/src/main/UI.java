package main;

import net.Packet00Login;
import net.Packet10Leave;
import net.Packet01Logout;
import object.OBJ_Heart;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class UI {
    GamePanel gp;
    //Graphics2D g2d;
    Font PraxisFontis, OldEnglish, JimNightshade, IngridDarling;
    KeyHandler keyH;
    Font TimesRoman;
    BufferedImage heartFull, heartHalf, heartEmpty;

    public int commandNum = 0;
    int subState = 0;
    public String currentText = "";
    int i = 0;
    private BufferedImage background;

    public UI(GamePanel gp, KeyHandler keyH) {
        this.keyH = keyH;
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/PraxisFontis.ttf");
            PraxisFontis = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/fonts/OLDENGL.TTF");
            OldEnglish = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/fonts/JimNightshade-Regular.ttf");
            JimNightshade = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/fonts/IngridDarling-Regular.ttf");
            IngridDarling = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SuperObject heart = new OBJ_Heart(gp);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartEmpty = heart.image3;

        background = loadTitleScreen();
    }

    public void draw(Graphics2D g2d) {
//        g2d.setFont(OldEnglish.deriveFont(Font.BOLD, 20f));
        g2d.setColor(Color.WHITE);

        //title state
        if (gp.gameState == gp.titleState) {
            drawTitleScreen(g2d);
        }

        //join state
        if (gp.gameState == gp.joinState) {
            drawJoinScreen(g2d);
        }

        //waiting state
        if (gp.gameState == gp.waitingState) {
            drawWaitingScreen(g2d);
        }

        //play state
        if (gp.gameState == gp.playState) {
            drawPlayerLife(g2d);
            drawInventory(g2d);
            drawInstructions(g2d);

        }

        //pause state
        if (gp.gameState == gp.pauseState) {
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

        if (gp.gameState == gp.endState) {

            long delta = 0;
            long lastTime = System.currentTimeMillis();
            while (true){
                delta = System.currentTimeMillis() - lastTime;
                if (delta >= 10) {
                    if (i < 50) {
                        drawEndGameScreen(0, g2d);
                    } else { drawEndGameScreen (i-50, g2d);}
                    if (i<=1150){i++;}
                    break;
                }
            }
        }
        //options state
        if (gp.gameState == gp.optionsState) {
            if (gp.prev_gameState == gp.playState) {
                drawPlayerLife(g2d);
                drawInventory(g2d);
            } else if (gp.prev_gameState == gp.pauseState) {
                drawPauseScreen(g2d);
                drawPlayerLife(g2d);
                drawInventory(g2d);
            }
            drawOptionsScreen(g2d);
        }
    }

    private BufferedImage loadTitleScreen(){
        background = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_RGB);
        try {
            background = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("title_screen/titleScreen.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return background;
    }

    private void drawJoinScreen(Graphics2D g2d) {
        //TITLE NAME
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 18f));
        String text = "Insert server ip address format (xxx.xxx.xxx.xxx) non zero";
        int x = getXforCenteredText(text, g2d);
        int y = gp.screenHeight / 4;
        g2d.drawString(text, x, y);

        text = "IP: " + gp.userInputedServerIP;
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize * 2;
        g2d.drawString(text, x, y);

        text = "Press Enter to confirm";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize * 2;
        g2d.drawString(text, x, y);

        if (keyH.invalidIPinserted) {
            text = "Pede para a sua mae te ensinar a escrever um IP";
            x = getXforCenteredText(text, g2d);
            y = gp.screenHeight - gp.tileSize * 2;

            Color prevColor = g2d.getColor();
            g2d.setColor(Color.red);
            g2d.drawString(text, x, y);
            g2d.setColor(prevColor);
        }
    }

    private void drawWaitingScreen(Graphics2D g2d) {
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 30f));
        String text = "Select your character bitch Using the fucking arrows you little boomer";
        int x = getXforCenteredText(text, g2d);
        int y = gp.screenHeight / 9;
        g2d.drawString(text, x, y);

        BufferedImage[] skin = new BufferedImage[2];

        int scale = 3;

        skin[0] = gp.player.setup("player1/boy_title_art");

        skin[1] = gp.player.setup("girl/girl_title_art");

        //draw first player
        x = gp.screenWidth / 6;
        y = gp.screenHeight / 3;
        g2d.drawImage(skin[gp.player1Skin], x, y, gp.tileSize * scale, gp.tileSize * scale, null);

        //draw character selecting arrows
        text = "<";
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 60f));
        int textHeight = (int) g2d.getFont().getLineMetrics(text, g2d.getFontRenderContext()).getHeight();
        int textWidth = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        y = (gp.screenHeight / 3) + gp.tileSize * scale + textHeight * 2 / 3;
        x -= textWidth / 2;
        g2d.drawString(text, x, y);
        text = ">";
        x += gp.tileSize * scale;
        g2d.drawString(text, x, y);


        //Draw Second player
        y = gp.screenHeight / 3;
        x = gp.screenWidth * 4 / 6;
        g2d.drawImage(skin[gp.player2Skin], x, y, gp.tileSize * scale, gp.tileSize * scale, null);

        //set font to color to gray and draw arrows
        text = "<";
        g2d.setColor(Color.gray);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 60f));
        textHeight = (int) g2d.getFont().getLineMetrics(text, g2d.getFontRenderContext()).getHeight();
        textWidth = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        y = (gp.screenHeight / 3) + gp.tileSize * scale + textHeight * 2 / 3;
        x -= textWidth / 2;
        g2d.drawString(text, x, y);
        text = ">";
        x += gp.tileSize * scale;
        g2d.drawString(text, x, y);


        //draw player 1 state
        if (gp.playerIsReady) {
            g2d.setColor(Color.green);
            text = "Ready";
        } else {
            g2d.setColor(Color.red);
            text = "Not Ready";
        }
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 30f));
        textWidth = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        x = (gp.screenWidth / 6) + ((gp.tileSize * scale) - textWidth) / 2;
        y += (int) g2d.getFont().getLineMetrics(text, g2d.getFontRenderContext()).getHeight();
        g2d.drawString(text, x, y);

        //draw player 2 state
        if (gp.player2IsReady) {
            g2d.setColor(Color.green);
            text = "a putinha esta pronta";
        } else {
            g2d.setColor(Color.red);
            text = "a putinha nao esta pronta";
        }
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 30f));
        textWidth = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        x = (gp.screenWidth * 4 / 6) + ((gp.tileSize * scale) - textWidth) / 2;
        g2d.drawString(text, x, y);


    }

    private void drawPlayerLife(Graphics2D g2d) {
        int x = gp.tileSize / 2;
        int y;

        if(gp.fullScreenOn){
            y = gp.screenHeight - gp.tileSize * 2;
        } else{
            y = gp.screenHeight - gp.tileSize * 3;
        }
        int i = 0;

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
        int x = gp.screenWidth - gp.tileSize * 4;
        int y;
        if(gp.fullScreenOn){
            y = gp.screenHeight - gp.tileSize * 6;
        } else{
            y = gp.screenHeight - gp.tileSize * 7;
        }

        //black square with transparency and a white border
        Color b = new Color(0, 0, 0, 100);
        g2d.setColor(b);
        g2d.fillRect(x, y, gp.tileSize * 3, gp.tileSize * 3);
        Color w = new Color(200, 200, 200, 100);
        g2d.setColor(w);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, gp.tileSize * 3, gp.tileSize * 3);
        g2d.drawRect(x + gp.tileSize, y, gp.tileSize, gp.tileSize * 3);
        g2d.drawRect(x, y + gp.tileSize, gp.tileSize * 3, gp.tileSize);


        x += gp.tileSize;

        if (gp.player.helmet != null) g2d.drawImage(gp.player.helmet.image, x, y, null);
        y += gp.tileSize;

        if (gp.player.armour != null) g2d.drawImage(gp.player.armour.image, x, y, null);
        y += gp.tileSize;

        if (gp.player.boots != null) g2d.drawImage(gp.player.boots.image, x, y, null);
        y -= gp.tileSize;
        x -= gp.tileSize;

        if (gp.player.shield != null) g2d.drawImage(gp.player.shield.image, x, y, null);
        x += gp.tileSize * 2;

        if (gp.player.weapon != null) g2d.drawImage(gp.player.weapon.image, x, y, null);


        x = gp.screenWidth - gp.tileSize * 2;
        if(gp.fullScreenOn){
            y = gp.screenHeight - gp.tileSize * 2;
        } else{
            y = gp.screenHeight - gp.tileSize * 3;
        }


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

        g2d.setFont(JimNightshade.deriveFont(Font.PLAIN, 35f));
        g2d.setColor(Color.WHITE);
        g2d.drawString("< ESC", gp.tileSize/2, gp.tileSize);
    }

    public void drawPauseScreen(Graphics2D g2d) {
        String text = "PAUSED";
        int x = getXforCenteredText(text, g2d);
        int y = gp.screenHeight / 2;

        g2d.drawString(text, x, y);
    }

    private void drawTitleScreen(Graphics2D g2d) {
        g2d.drawImage(background, 0,0, gp.screenWidth, gp.screenHeight
                , null);
        Color c = new Color(0, 0, 0, 0.35f);
        g2d.setColor(c);
        g2d.drawRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2d.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        //TITLE NAME
        g2d.setFont(JimNightshade.deriveFont(Font.BOLD, 80f));
        String text = "Heroes Of War:";
        int x = getXforCenteredText(text, g2d);
        int y = 170;
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);

        /*
        //HERO IMAGE
        int scale = 3;
        x = gp.screenWidth / 2 - (gp.tileSize * scale) / 2;
        y += gp.tileSize;
        g2d.drawImage(gp.player.titleArt, x, y, gp.tileSize * scale, gp.tileSize * scale, null);*/

        text = "Die Young";
        x = 688;
        y = 246;
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(-20.31), 0, 0);
        g2d.setFont(IngridDarling.deriveFont(affineTransform));
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 65f));
        c = new Color(214, 61, 61);
        g2d.setColor(c);
        g2d.drawString(text, x, y);

        //MENU
        affineTransform.rotate(Math.toRadians(20.31), 0, 0);
        g2d.setFont(IngridDarling.deriveFont(affineTransform));
        g2d.setColor(Color.WHITE);
        g2d.setFont(JimNightshade.deriveFont(Font.PLAIN, 48f));

        text = "Host Game";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize + 30;
        g2d.drawString(text, x, y);
        if (commandNum == 0) {
            g2d.drawString(">", x - gp.tileSize, y);
        }

        text = "Join Game";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize;
        g2d.drawString(text, x, y);
        if (commandNum == 1) {
            g2d.drawString(">", x - gp.tileSize, y);
        }

        text = "Instructions";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize;
        g2d.drawString(text, x, y);
        if (commandNum == 2) {
            g2d.drawString(">", x - gp.tileSize, y);
        }

        text = "About";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize;
        g2d.drawString(text, x, y);
        if (commandNum == 3) {
            g2d.drawString(">", x - gp.tileSize, y);
        }

        text = "Quit";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize;
        g2d.drawString(text, x, y);
        if (commandNum == 4) {
            g2d.drawString(">", x - gp.tileSize, y);
        }

    }

    public int getXforCenteredText(String text, Graphics2D g2d) {

        int length = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return (gp.screenWidth - length) / 2;

    }

    private void drawReadScreen(Graphics2D g2d) {
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - gp.tileSize * 4;
        int height = gp.screenHeight - 2 * gp.tileSize;

        drawSubWindow(x, y, width, height, g2d);
        x += gp.tileSize;
        y += gp.tileSize;
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 30f));
        g2d.setColor(Color.black);
        for (String line : currentText.split("\n")) {
            g2d.drawString(line, x, y);
            y += gp.tileSize / 1.5;
        }

    }

    private void drawOptionsScreen(Graphics2D g2d) {
        int x = gp.tileSize * 4;
        int y = gp.tileSize * 2;
        int width = gp.tileSize * 8;
        int height = gp.tileSize * 10;
        Color c = new Color(0, 0, 0, 210);
        g2d.setColor(c);
        g2d.fillRoundRect(x, y, width, height, 35, 35);
        c = new Color(255, 255, 255);
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

        switch (subState) {
            case 0:
                options_top(x, y, g2d);
                break;
            case 1:
                options_fullScreenNotification(x, y, g2d);
                break;
            case 2:
                options_control(x, y, g2d);
                break;
            case 3:
                options_endGameConfirmation(x, y, g2d);
                break;
        }

        gp.keyH.keysPressed[keyH.confirmKey] = false;
    }

    private void options_top(int frameX, int frameY, Graphics2D g2d) {
        int textX;
        int textY;
        String text = "Options";
        textX = getXforCenteredText(text, g2d);
        textY = frameY + gp.tileSize;
        g2d.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        g2d.drawString("Full Screen", textX, textY);
        if (commandNum == 0) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                if (!gp.fullScreenOn) {
                    gp.fullScreenOn = true;
                } else if (gp.fullScreenOn) {
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }

        textY += gp.tileSize;
        g2d.drawString("Music", textX, textY);
        if (commandNum == 1) {
            g2d.drawString(">", textX - 25, textY);
        }

        textY += gp.tileSize;
        g2d.drawString("SE", textX, textY);
        if (commandNum == 2) {
            g2d.drawString(">", textX - 25, textY);
        }

        textY += gp.tileSize;
        g2d.drawString("Control", textX, textY);
        if (commandNum == 3) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 2;
                commandNum = 0;
            }
        }

        textY += gp.tileSize;
        g2d.drawString("Exit to main menu", textX, textY);
        if (commandNum == 4) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 3;
                commandNum = 0;
            }
        }


        textY += gp.tileSize * 2;
        g2d.drawString("Back", textX, textY);
        if (commandNum == 5) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                gp.gameState = gp.prev_gameState;
                gp.new_gameState = gp.prev_gameState;
                commandNum = 0;
            }
        }

        textX = frameX + (int) (gp.tileSize * 4.5);
        textY = frameY + gp.tileSize * 2 + 24;
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(textX, textY, 24, 24);
        if (gp.fullScreenOn) {
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

        gp.config.
                saveConfig();
    }

    private void options_fullScreenNotification(int frameX, int frameY, Graphics2D g2d) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentText = "The change will take \neffect after restarting \nthe game.";

        for (String line : currentText.split("\n")) {
            g2d.drawString(line, textX, textY);
            textY += 40;
        }

        textY = frameY + gp.tileSize * 9;
        g2d.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 0;
            }
        }
    }

    private void options_control(int frameX, int frameY, Graphics2D g2d) {
        int textX;
        int textY;

        String text = "Control";
        textX = getXforCenteredText(text, g2d);
        textY = frameY + gp.tileSize;
        g2d.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2d.drawString("Move", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("Confirm/Attack", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("Shoot/Cast", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("Character Screen", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("Pause", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("Options", textX, textY);
        textY += gp.tileSize;

        textX = frameX + gp.tileSize * 6;
        textY = frameY + gp.tileSize * 2;
        g2d.drawString("ARROWS", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("SPACE", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("F", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("C", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("P", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("ESC", textX, textY);
        textY += gp.tileSize;

        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2d.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 0;
                commandNum = 3;
            }
        }
    }

    private void options_endGameConfirmation(int x, int y, Graphics2D g2d) {
        int textX = x + gp.tileSize;
        int textY = y + gp.tileSize * 3;

        currentText = "Quit the game and \nreturn to the title screen?";
        for (String line : currentText.split("\n")) {
            g2d.drawString(line, textX, textY);
            textY += 40;
        }

        String text = "Yes";
        textX = getXforCenteredText(text, g2d);
        textY += gp.tileSize * 3;
        g2d.drawString(text, textX, textY);
        if (commandNum == 0) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 0;
                //Send Logout packet
                Packet01Logout logoutPacket = new Packet01Logout();
                logoutPacket.writeData(gp.socketClient);
                System.out.println("DISCONNECTING");
                gp.socketClient.close();

            }
        }

        text = "No";
        textX = getXforCenteredText(text, g2d);
        textY += gp.tileSize;
        g2d.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 0;
                commandNum = 4;
            }
        }
    }

    private void drawSubWindow(int x, int y, int width, int height, Graphics2D g2d) {
        Color c = new Color(200, 200, 200, 175);
        g2d.setColor(c);
        g2d.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(120, 50, 19);
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(10));
        g2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 35, 35);

    }
    private void drawEndGameScreen(int i, Graphics2D g2d) {
        String text = "Heroes Of War\n"
                    + "Die Young\n"
                    + "\n\n\n\n\n"
                    + "by: Kiko\n"
                    + "Lucca\n"
                    + "Pedro\n"
                    + "Érico\n"
                    + "\n\nThe End\n\n"
                    + "Press Enter to return\nto the title screen" ;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 60f));
        int x ;
        int y = gp.screenHeight / 3;
        for (String line : text.split("\n")) {
            x = getXforCenteredText(line, g2d);
            g2d.drawString(line, x, y - i);
            y += 80;
        }
    }
}
