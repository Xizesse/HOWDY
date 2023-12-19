package monster;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class Monster_Spike extends Entity {

    public Monster_Spike(GamePanel gp, int map) {
        super(gp, map);
        direction = "down";
        speed = 1;
        maxHealth = 20;
        damage = 1;
        defAttackCoolDown = 60;
        attackCoolDown = 0;
        attackRange = 50;
        currentHealth = maxHealth;
        solidArea = new Rectangle(0, 0, 48, 48);
        getImage();

    }

    public void getImage() {
        bodyUp1 = setup("monster_spike/spike_right1");
        bodyUp2 = setup("monster_spike/spike_right2");
        bodyDown1 = setup("monster_spike/spike_left1");
        bodyDown2 = setup("monster_spike/spike_left2");
        BodyLeft1 = setup("monster_spike/spike_left1");
        BodyLeft2 = setup("monster_spike/spike_left2");
        BodyRight1 = setup("monster_spike/spike_right1");
        BodyRight2 = setup("monster_spike/spike_right2");

        attackUp = setup("monster_spike/spike_attack_left");
        attackDown = setup("monster_spike/spike_attack_right");
        attackLeft = setup("monster_spike/spike_attack_left");
        attackRight = setup("monster_spike/spike_attack_right");
    }

    @Override
    public void setAction() {
        actionCounter++;
        if (actionCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            if (i <= 25) {
                direction = "up";
            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else if (i <= 100) {
                direction = "right";
            }
            actionCounter = 0;
        }


    }
}
