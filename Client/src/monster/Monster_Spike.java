package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class Monster_Spike extends Entity {

    public Monster_Spike(GamePanel gp) {
        super(gp);
        speed = 1;
        maxHealth = 1;
        currentHealth = maxHealth;
        solidArea.x = 3;
        solidArea.y = 10;
        solidArea.width = 42;
        solidArea.height = 38;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }
    public void getImage(){

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