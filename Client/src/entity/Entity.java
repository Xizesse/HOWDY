package entity;

import java.awt.image.BufferedImage;

public class Entity {

    public int x, y;
    public int speed;

    public BufferedImage titleArt, bodyUp1, bodyUp2, bodyDown1, bodyDown2, BodyLeft1, BodyLeft2, BodyRight1, BodyRight2;


    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
}
