package entity;

import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class NPC_Rat extends Entity {


    public NPC_Rat(GamePanel gp) {
        super(gp);


        direction = "down";
        speed = 1;
        getImage();

        solidArea = new Rectangle(0, 4 * 3, 14*3, 7*3);
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

        actionCounter ++;
        if (actionCounter > 40){
            Random random = new Random();
            int i = random.nextInt(100)+1;
            if (i <= 25) {
                direction = "left";
            }
            else if (i <= 50) {
                direction = "left";
            }
            else if (i <= 75) {
                direction = "right";
            }
            else if (i <= 100) {
                direction = "right";
            }
            actionCounter = 0;
        }
        actionCounter ++;
    }
}
