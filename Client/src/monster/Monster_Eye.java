package monster;

import entity.Entity;
import entity.NPC_Player;
import main.GamePanel;

import java.awt.*;

public class Monster_Eye extends Entity {

    public Monster_Eye(GamePanel gp, int map) {
        super(gp, map);
        direction = "down";
        speed = 10;
        maxHealth = 15;
        damage = 2;
        defAttackCoolDown = 60;
        attackCoolDown = 0;
        attackRange = 70;
        currentHealth = maxHealth;
        solidArea = new Rectangle(0, 0, 30, 30);
        getImage();

    }

    public void getImage() {
        bodyUp1 = setup("monster_eye/eye_right1");
        bodyUp2 = setup("monster_eye/eye_right2");
        bodyDown1 = setup("monster_eye/eye_left1");
        bodyDown2 = setup("monster_eye/eye_left2");
        BodyLeft1 = setup("monster_eye/eye_left1");
        BodyLeft2 = setup("monster_eye/eye_left2");
        BodyRight1 = setup("monster_eye/eye_right1");
        BodyRight2 = setup("monster_eye/eye_right2");
    }

    @Override
    public void setAction() {
        if (gp.players.isEmpty()) {
            return; // No players to follow
        }

        NPC_Player closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;

        // Find the closest player
        for (NPC_Player player : gp.players) {
            if (player == null) {
                continue;
            }
            if (player.map != map) {
                continue;
            }
            double distance = Math.sqrt(Math.pow(worldX - player.worldX, 2) + Math.pow(worldY - player.worldY, 2));
            if (distance < closestDistance) {
                closestDistance = distance;
                closestPlayer = player;
            }
        }

        if (closestPlayer != null) {
            // Determine direction to move to get closer to the closest player
            int xDistance = closestPlayer.worldX - worldX;
            int yDistance = closestPlayer.worldY - worldY;

            if (Math.abs(xDistance) > Math.abs(yDistance)) {
                // Move horizontally
                direction = xDistance > 0 ? "right" : "left";
            } else {
                // Move vertically
                direction = yDistance > 0 ? "down" : "up";
            }
        }
    }
}
