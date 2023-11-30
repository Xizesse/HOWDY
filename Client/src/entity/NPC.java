package entity;

import jdk.jshell.execution.Util;
import main.GamePanel;
import main.UtilityTool;

import java.awt.image.BufferedImage;
import java.util.Random;

public class NPC extends Entity {


    public NPC(GamePanel gp) {
        super(gp);
        //System.out.println("npc constructor created");

        direction = "down";
        speed = 6;
        getImage();
    }
    public void getImage()
 {
         bodyUp1 = setup("npc/mouse0");
         bodyUp2 = setup("npc/mouse1");
         bodyDown1 = setup("npc/mouse2");
         bodyDown2 = setup("npc/mouse3");
         BodyLeft1 = setup("npc/mouse0");
         BodyLeft2 = setup("npc/mouse1");
         BodyRight1 = setup("npc/mouse2");
         BodyRight2 = setup("npc/mouse3");
    }
    @Override
    public void setAction() {
        //System.out.println("npc action");
        actionCounter ++;
        if (actionCounter > 40){
            Random random = new Random();
            int i = random.nextInt(100)+1;
            if (i <= 25) {
                direction = "up";
            }
            else if (i <= 50) {
                direction = "down";
            }
            else if (i <= 75) {
                direction = "left";
            }
            else if (i <= 100) {
                direction = "right";
            }
            actionCounter = 0;

        }
        actionCounter ++;



    }


}
