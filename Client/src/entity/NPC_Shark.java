package entity;

import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class NPC_Shark extends Entity {


    public NPC_Shark(GamePanel gp) {
        super(gp);


        direction = "down";
        speed = 4;
        getImage();

        solidArea = new Rectangle(0, 4 * 3, 14*3, 7*3);
    }
    public void getImage()
    {
        bodyUp1 = setup("npc/shark0");
        bodyUp2 = bodyUp1;
        bodyDown1 = setup("npc/shark1");
        bodyDown2 = bodyDown1;
        BodyLeft1 = bodyUp1;
        BodyLeft2 = bodyUp1;
        BodyRight1 = bodyDown1;
        BodyRight2 = bodyDown1;
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
                direction = "right";
            }
            else if (i <= 75) {
                direction = "down" +
                        "" +
                        "";
            }
            else if (i <= 100) {
                direction = "up";
            }
            actionCounter = 0;
        }
        actionCounter ++;
    }
}
