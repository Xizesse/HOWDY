package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NPC_Player2 extends Entity
{

    public NPC_Player2(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 4;

        getImage();
    }


    public void getImage() {

        bodyUp1 = setup("girl/girl_up_1");
        bodyUp2 = setup("girl/girl_up_2");
        bodyDown1 = setup("girl/girl_down_1");
        bodyDown2 = setup("girl/girl_down_2");
        BodyLeft1 = setup("girl/girl_left_1");
        BodyLeft2 = setup("girl/girl_left_2");
        BodyRight1 = setup("girl/girl_right_1");
        BodyRight2 = setup("girl/girl_right_2");
        titleArt = setup("girl/girl_title_art");
        System.out.println("NPC_Player2 getImage()");

    }


@Override
    public void setAction() {

        this.direction = direction;
        this.worldX = worldX;
        this.worldY = worldY;


        if((previousWorldX != worldX) || (previousWorldY) != worldY){   //if player moved update the sprite
            spriteCounter++;

            if (spriteCounter > 10) {
                if (spriteNum == 1 ) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

        }

        previousWorldX = worldX;
        previousWorldY = worldY;

    }



}
