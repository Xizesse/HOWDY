package monster;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class Monster_Stalker extends Entity {

    public Monster_Stalker(GamePanel gp, int map) {
        super(gp, map);
        direction = "down";
        speed = 1;
        maxHealth = 1;
        currentHealth = maxHealth;
        solidArea = new Rectangle(0, 0, 48, 48);
        getImage();

    }
    public void getImage(){
        bodyUp1 = setup("monster_stalker/stalker_right");
        bodyUp2 = bodyUp1;
        bodyDown1 = setup("monster_stalker/stalker_left");
        bodyDown2 = bodyDown1;
        BodyLeft1 = bodyDown1;
        BodyLeft2 = bodyDown1;
        BodyRight1 = bodyUp1;
        BodyRight2 = bodyUp1;
    }
    @Override
    public void setAction() {
        actionCounter ++;
        if (actionCounter == 120){
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

    }
}
