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
        speed = 1;
        getImage();
    }
    public void getImage()
 {
         bodyUp1 = setup("monster_spike/spike_right1");
         bodyUp2 = setup("monster_spike/spike_right2");
         bodyDown1 = setup("monster_spike/spike_left1");
         bodyDown2 = setup("monster_spike/spike_left2");
         BodyLeft1 = setup("monster_spike/spike_left1");
         BodyLeft2 = setup("monster_spike/spike_left2");
         BodyRight1 = setup("monster_spike/spike_right1");
         BodyRight2 = setup("monster_spike/spike_right2");
    }
    @Override
    public void setAction() {
        //System.out.println("npc action");
        actionCounter ++;
        if (actionCounter > 120){
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
