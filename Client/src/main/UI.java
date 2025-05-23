package main;

import net.Packet01Logout;
import object.OBJ_Heart;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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

    public int commandNum = 0, commandNum1 = 0;
    int subState = 0;
    public String currentText = "";
    int i = 0;
    private BufferedImage background, instructionimage;

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
            drawInstructions(g2d);
        }

        //join state
        if (gp.gameState == gp.joinState) {
            drawJoinScreen(g2d);
            drawInstructions(g2d);
        }

        //waiting state
        if (gp.gameState == gp.waitingState) {
            drawWaitingScreen(g2d);
            drawInstructions(g2d);
        }

        //play state
        if (gp.gameState == gp.playState) {
            drawPlayerLife(g2d);
            drawInventory(g2d);
            drawInstructions(g2d);
//            drawWorldCoordinates(g2d);

            if (gp.player.map == 2) {
                Color c = g2d.getColor();
                g2d.setColor(Color.gray);
                g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 35f));
                String text = "Cross the bridge together to escape the dungeon";
                int x = getXforCenteredText(text, g2d);
                int y = gp.screenHeight - (int) g2d.getFont().getLineMetrics(text, g2d.getFontRenderContext()).getHeight();
                g2d.drawString(text, x, y);
                g2d.setColor(c);
            }

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
            while (true) {
                delta = System.currentTimeMillis() - lastTime;
                if (delta >= 10) {
                    if (i < 50) {
                        drawEndGameScreen(0, g2d);
                    } else {
                        drawEndGameScreen(i - 50, g2d);
                    }
                    if (i <= 1150) {
                        i++;
                    }
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
            } else if (gp.prev_gameState == gp.titleState){
                drawTitleScreen(g2d);
            } else if (gp.prev_gameState == gp.joinState){
                drawJoinScreen(g2d);
            } else if (gp.prev_gameState == gp.waitingState){
                drawWaitingScreen(g2d);
            } else if (gp.prev_gameState == gp.instructionsState){
                drawInstructionsScreen(g2d);
            } else if (gp.prev_gameState == gp.aboutState){
                drawAboutScreen(g2d);
            }
            drawOptionsScreen(g2d);
        }
        if (gp.gameState == gp.instructionsState){
            drawInstructions(g2d);
            drawInstructionsScreen(g2d);
        }
        if(gp.gameState == gp.aboutState){
            drawInstructions(g2d);
            drawAboutScreen(g2d);
        }
    }

    private void drawWorldCoordinates(Graphics2D g2d) {
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 20f));
        String text = "X: " + gp.player.worldX / gp.tileSize + " Y: " + gp.player.worldY / gp.tileSize;
        int x = getXforCenteredText(text, g2d);
        int y = gp.screenHeight - gp.tileSize * 2;
        g2d.drawString(text, x, y);
    }

    private BufferedImage loadTitleScreen() {
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
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 35f));
        String text = "Insert Game Code";
        int x = getXforCenteredText(text, g2d);
        int y = gp.screenHeight / 4;
        g2d.drawString(text, x, y);

        text = "Code: " + gp.userInputedServerIP.toUpperCase();
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize * 2;
        g2d.drawString(text, x, y);

        text = "Press Enter to confirm";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize * 2;
        g2d.drawString(text, x, y);

        if (keyH.invalidIPinserted) {
            text = "Server not found";
            x = getXforCenteredText(text, g2d);
            y = gp.screenHeight - gp.tileSize * 2;

            Color prevColor = g2d.getColor();
            g2d.setColor(Color.red);
            g2d.drawString(text, x, y);
            g2d.setColor(prevColor);
        }

        drawInstructions(g2d);
    }

    private void drawWaitingScreen(Graphics2D g2d) {
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 30f));
        String text = "Select your character using the arrow keys ";
        int x = getXforCenteredText(text, g2d);
        int y = gp.screenHeight / 9;
        g2d.drawString(text, x, y);
        text = "Press Enter to confirm";
        x = getXforCenteredText(text, g2d);
        y += (int) g2d.getFont().getLineMetrics(text, g2d.getFontRenderContext()).getHeight();
        g2d.drawString(text, x, y);
        y -= (int) g2d.getFont().getLineMetrics(text, g2d.getFontRenderContext()).getHeight();

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
            text = "Player 2 is ready";
        } else {
            g2d.setColor(Color.red);
            text = "Player 2 is not ready";
        }
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 30f));
        textWidth = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        x = (gp.screenWidth * 4 / 6) + ((gp.tileSize * scale) - textWidth) / 2;
        g2d.drawString(text, x, y);

        if (gp.showGameCode) {
            String base36 = keyH.getIPcode();

            g2d.setColor(Color.white);
            text = "Game code: " + base36.toUpperCase();
            textHeight = (int) g2d.getFont().getLineMetrics(text, g2d.getFontRenderContext()).getHeight();
            x = getXforCenteredText(text, g2d);
            y = gp.screenHeight - textHeight / 2;
            g2d.drawString(text, x, y);
        }

    }

    private void drawPlayerLife(Graphics2D g2d) {
        int x = gp.tileSize / 2;
        int y;

        if (gp.fullScreenOn) {
            y = gp.screenHeight - gp.tileSize * 2;
        } else {
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
        if (gp.fullScreenOn) {
            y = gp.screenHeight - gp.tileSize * 6;
        } else {
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
        if (gp.fullScreenOn) {
            y = gp.screenHeight - gp.tileSize * 2;
        } else {
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
        if (gp.gameState == gp.titleState) g2d.drawString("< ESC for Options", gp.tileSize / 2, gp.tileSize);
        else g2d.drawString("< ESC", gp.tileSize / 2, gp.tileSize);
    }

    public void drawPauseScreen(Graphics2D g2d) {
        String text = "PAUSED";
        int x = getXforCenteredText(text, g2d);
        int y = gp.screenHeight / 2;

        g2d.drawString(text, x, y);
    }

    private void drawTitleScreen(Graphics2D g2d) {
        g2d.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);
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

        y += gp.tileSize;
        int rectheight = gp.tileSize - 12;
        c = new Color(0, 0, 0, 0.6f);
        g2d.setColor(c);
        g2d.drawRect(gp.tileSize * 9, y, gp.tileSize * 6, rectheight);
        g2d.fillRect(gp.tileSize * 9, y, gp.tileSize * 6, rectheight);
        g2d.setColor(Color.WHITE);
        text = "Host Game";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize - 15;
        g2d.drawString(text, x, y);
        if (commandNum == 0) {
            g2d.drawString(">", x - gp.tileSize, y);
        }

        y += 15;
        g2d.setColor(c);
        g2d.drawRect(gp.tileSize * 9, y, gp.tileSize * 6, rectheight);
        g2d.fillRect(gp.tileSize * 9, y, gp.tileSize * 6, rectheight);
        g2d.setColor(Color.WHITE);
        text = "Join Game";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize - 15;
        g2d.drawString(text, x, y);
        if (commandNum == 1) {
            g2d.drawString(">", x - gp.tileSize, y);
        }

        y += 15;
        g2d.setColor(c);
        g2d.drawRect(gp.tileSize * 9, y, gp.tileSize * 6, rectheight);
        g2d.fillRect(gp.tileSize * 9, y, gp.tileSize * 6, rectheight);
        g2d.setColor(Color.WHITE);
        text = "Instructions";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize - 15;
        g2d.drawString(text, x, y);
        if (commandNum == 2) {
            g2d.drawString(">", x - gp.tileSize, y);
        }

        y += 15;
        g2d.setColor(c);
        g2d.drawRect(gp.tileSize * 9, y, gp.tileSize * 6, rectheight);
        g2d.fillRect(gp.tileSize * 9, y, gp.tileSize * 6, rectheight);
        g2d.setColor(Color.WHITE);
        text = "About";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize - 15;
        g2d.drawString(text, x, y);
        if (commandNum == 3){
            g2d.drawString(">", x - gp.tileSize, y);
        }

        y += 15;
        g2d.setColor(c);
        g2d.drawRect(gp.tileSize * 9, y, gp.tileSize * 6, rectheight);
        g2d.fillRect(gp.tileSize * 9, y, gp.tileSize * 6, rectheight);
        g2d.setColor(Color.WHITE);
        text = "Quit";
        x = getXforCenteredText(text, g2d);
        y += gp.tileSize - 15;
        g2d.drawString(text, x, y);
        if (commandNum == 4) {
            g2d.drawString(">", x - gp.tileSize, y);
        }
        drawInstructions(g2d);
    }

    private void drawInstructionsScreen(Graphics2D g2d){
        g2d.setFont(JimNightshade.deriveFont(Font.BOLD, 80f));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Instructions", getXforCenteredText("Instructions", g2d), 156);
        int textX = gp.tileSize * 5, textY = gp.tileSize * 6;
        g2d.setFont(JimNightshade.deriveFont(Font.BOLD, 200f));

        Color c = new Color(185, 169, 169);
        g2d.setColor(c);
        g2d.drawRect(gp.tileSize * 4, 248, gp.tileSize * 16, gp.tileSize * 9);
        g2d.fillRect(gp.tileSize * 4, 248, gp.tileSize * 16, gp.tileSize * 9);

        g2d.setColor(Color.BLACK);
        g2d.setFont(JimNightshade.deriveFont(Font.PLAIN, 30f));

        currentText = "This is a 2 player game. One of the players should choose the option Host,\nand the other one the option Join, entering the Hosts IP address.";
        for (String line : currentText.split("\n")) {
            g2d.drawString(line, textX, textY);
            textY += 40;
        }
        textY += 15;
        currentText = "For movement, use the arrow keys to go up, down, left or right, or a\ncombination of two to go diagonally.";
        for (String line : currentText.split("\n")) {
            g2d.drawString(line, textX, textY);
            textY += 40;
        }
        textY += 15;
        currentText = "Use the space bar to attack and simply walk over the items to pick them up.";
        for (String line : currentText.split("\n")) {
            g2d.drawString(line, textX, textY);
            textY += 40;
        }
        textY += 15;
        currentText = "If you find a pressure plate, your teammate should find an open door\nto go through.";
        for (String line : currentText.split("\n")) {
            g2d.drawString(line, textX, textY);
            textY += 40;
        }
        textY += 15;
        currentText = "At any time, press the ESC key for the options menu.";
        for (String line : currentText.split("\n")) {
            g2d.drawString(line, textX, textY);
            textY += 40;
        }
    }

    private void drawAboutScreen(Graphics2D g2d){
        g2d.setFont(JimNightshade.deriveFont(Font.BOLD, 80f));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Instructions", getXforCenteredText("Instructions", g2d), 156);
        int textX = gp.tileSize * 5, textY = gp.tileSize * 6;
        g2d.setFont(JimNightshade.deriveFont(Font.BOLD, 200f));

        Color c = new Color(185, 169, 169);
        g2d.setColor(c);
        g2d.drawRect(gp.tileSize * 4, 248, gp.tileSize * 16, gp.tileSize * 9);
        g2d.fillRect(gp.tileSize * 4, 248, gp.tileSize * 16, gp.tileSize * 9);

        g2d.setColor(Color.BLACK);
        g2d.setFont(JimNightshade.deriveFont(Font.PLAIN, 30f));
        currentText = "We are a group of Master's students in Electrical and Computer Engineering\nat the Faculty of Engineering of the University of Porto. We developed this\ngame as a part of our Software Design course. Thanks for playing our game!";
        for (String line : currentText.split("\n")) {
            g2d.drawString(line, textX, textY);
            textY += 40;
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
        int x = gp.tileSize * 7;
        int y = gp.tileSize * 2;
        int width = gp.tileSize * 10;
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
        g2d.setColor(Color.WHITE);
        g2d.setFont(JimNightshade.deriveFont(Font.PLAIN, 35f));
        g2d.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        g2d.drawString("Full Screen", textX, textY);
        if (commandNum1 == 0) {
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
        if (commandNum1 == 1) {
            g2d.drawString(">", textX - 25, textY);
        }

        /*textY += gp.tileSize;
        g2d.drawString("SE", textX, textY);
        if (commandNum1 == 2) {
            g2d.drawString(">", textX - 25, textY);
        }*/

        textY += gp.tileSize;
        g2d.drawString("Control", textX, textY);
        if (commandNum1 == 2) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 2;
                commandNum1 = 0;
            }
        }

        textY += gp.tileSize;
        g2d.drawString("Exit to main menu", textX, textY);
        if (commandNum1 == 3) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 3;
                commandNum1 = 0;
            }
        }


        textY += gp.tileSize * 3;
        g2d.drawString("Back", textX, textY);
        if (commandNum1 == 4) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                gp.gameState = gp.prev_gameState;
                gp.new_gameState = gp.prev_gameState;
                commandNum1 = 0;
            }
        }

        textX = frameX + gp.tileSize * 2 + (int) (gp.tileSize * 4.5);
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

        /*textY += gp.tileSize;
        g2d.drawRect(textX, textY, 120, 24);
        //volumeWidth = 24 * gp.se.volumeScale;
        g2d.fillRect(textX, textY, volumeWidth, 24);*/

        gp.config.saveConfig();
    }

    private void options_fullScreenNotification(int frameX, int frameY, Graphics2D g2d) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        g2d.setColor(Color.WHITE);
        g2d.setFont(JimNightshade.deriveFont(Font.PLAIN, 35f));
        currentText = "The change will take effect after\nrestarting the game.";

        for (String line : currentText.split("\n")) {
            g2d.drawString(line, textX, textY);
            textY += 40;
        }

        textY = frameY + gp.tileSize * 9;
        g2d.drawString("Back", textX, textY);
        if (commandNum1 == 0) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 0;
            }
        }
    }

    private void options_control(int frameX, int frameY, Graphics2D g2d) {
        int textX;
        int textY;

        g2d.setColor(Color.WHITE);
        g2d.setFont(JimNightshade.deriveFont(Font.PLAIN, 35f));

        String text = "Control";
        textX = getXforCenteredText(text, g2d);
        textY = frameY + gp.tileSize;
        g2d.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2d.drawString("Move", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("Attack", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("Confirm", textX, textY);
        textY += gp.tileSize;

        g2d.drawString("Options", textX, textY);
        textY += gp.tileSize;

        textX = frameX + gp.tileSize * 6;
        textY = frameY + gp.tileSize * 2;
        g2d.drawString("ARROWS", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("SPACE", textX, textY);
        textY += gp.tileSize;
        g2d.drawString("ENTER", textX, textY);
        textY += gp.tileSize;

        g2d.drawString("ESC", textX, textY);
        textY += gp.tileSize;

        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2d.drawString("Back", textX, textY);
        if (commandNum1 == 0) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 0;
                commandNum1 = 3;
            }
        }
    }

    private void options_endGameConfirmation(int x, int y, Graphics2D g2d) {
        int textX = x + gp.tileSize;
        int textY = y + gp.tileSize * 3;
        g2d.setColor(Color.WHITE);
        g2d.setFont(JimNightshade.deriveFont(Font.PLAIN, 35f));

        currentText = "Quit the game and return to the\ntitle screen?";
        for (String line : currentText.split("\n")) {
            g2d.drawString(line, textX, textY);
            textY += 40;
        }

        String text = "Yes";
        textX = getXforCenteredText(text, g2d);
        textY += gp.tileSize * 3;
        g2d.drawString(text, textX, textY);
        if (commandNum1 == 0) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 0;
                //Send Logout packet
                if (gp.prev_gameState == gp.waitingState) {
                    Packet01Logout logoutPacket = new Packet01Logout(0);
                    logoutPacket.writeData(gp.socketClient);
                    System.out.println("DISCONNECTING");
                    gp.socketClient.close();
                    return;
                } else if (gp.prev_gameState == gp.joinState) {
                    gp.gameState = gp.titleState;
                    gp.new_gameState = gp.titleState;
                    return;
                } else if (gp.prev_gameState == gp.titleState) {
                    gp.gameState = gp.titleState;
                    gp.new_gameState = gp.titleState;

                } else if (gp.prev_gameState == gp.instructionsState) {
                    gp.gameState = gp.titleState;
                    gp.new_gameState = gp.titleState;

                } else if (gp.prev_gameState == gp.aboutState) {
                    gp.gameState = gp.titleState;
                    gp.new_gameState = gp.titleState;

                }else {
                    Packet01Logout logoutPacket = new Packet01Logout(0);
                    logoutPacket.writeData(gp.socketClient);
                    System.out.println("DISCONNECTING");
                    gp.socketClient.close();
                }

            }
        }

        text = "No";
        textX = getXforCenteredText(text, g2d);
        textY += gp.tileSize;
        g2d.drawString(text, textX, textY);
        if (commandNum1 == 1) {
            g2d.drawString(">", textX - 25, textY);
            if (gp.keyH.keysPressed[keyH.confirmKey]) {
                subState = 0;
                commandNum1 = 4;
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
                + "by: \n"
                + " \n"
                + "Xizesse\n"
                + "Lucca\n"
                + "Xanax\n"
                + "Érico\n"
                + " \n"
                + "Huge thanks to RyiSnow \n"
                + " \n"
                + "\n\nThe End\n\n"
                + "Press Enter to return\nto the title screen";
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 60f));
        int x;
        int y = gp.screenHeight / 3;
        for (String line : text.split("\n")) {
            x = getXforCenteredText(line, g2d);
            g2d.drawString(line, x, y - i);
            y += 80;
        }
    }
}
