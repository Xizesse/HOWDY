package test;

import entity.Player;
import main.CollisionChecker;
import main.GamePanel;
import main.KeyHandler;
import monster.Monster_Spike;
import object.OBJ_Katana;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CollisionCheckerTest {
    private GamePanel gp;
    private CollisionChecker colCheck;

    @BeforeEach
    void setUp() throws IOException {
        gp = new GamePanel();
        colCheck = new CollisionChecker(gp);
    }

    @Test
    void checkTileCollisionOff() {
        Player player = new Player(gp, new KeyHandler(gp), 10, 10, 0);
        player.direction = "up";
        colCheck.checkTile(player);
        assertFalse(player.collisionOn);
        player.direction = "down";
        colCheck.checkTile(player);
        assertFalse(player.collisionOn);
        player.direction = "left";
        colCheck.checkTile(player);
        assertFalse(player.collisionOn);
        player.direction = "right";
        colCheck.checkTile(player);
        assertFalse(player.collisionOn);
    }

    @Test
    void checkTileCollisionOn() {
        Player player = new Player(gp, new KeyHandler(gp), 0, 0, 0 );
        player.direction = "up";
        colCheck.checkTile(player);
        assertTrue(player.collisionOn);
        player.direction = "down";
        colCheck.checkTile(player);
        assertTrue(player.collisionOn);
        player.direction = "left";
        colCheck.checkTile(player);
        assertTrue(player.collisionOn);
        player.direction = "right";
        colCheck.checkTile(player);
        assertTrue(player.collisionOn);
    }

    @Test
    void checkObjectTest() {
        gp.player = new Player(gp, new KeyHandler(gp), 25, 29, 1 );
        gp.currentMap = 1;
        gp.player.direction = "up";
        assertEquals(999, colCheck.checkObject(gp.player, true));
        //set up obj
        gp.obj[1][0] = new OBJ_Katana(gp);
        gp.obj[1][0].worldX = 25 * gp.tileSize;
        gp.obj[1][0].worldY = 29 * gp.tileSize;

        assertEquals(0, colCheck.checkObject(gp.player, true));

    }

    @Test
    void checkAttackTest() {
        gp.player = new Player(gp, new KeyHandler(gp), 25, 29, 1 );
        gp.currentMap = 1;


        //set up NPC
        gp.npc[1][0] = new Monster_Spike(gp, 1);
        gp.npc[1][0].worldX = 26 * gp.tileSize;
        gp.npc[1][0].worldY = 29 * gp.tileSize;

        gp.player.direction = "up";

        assertFalse( colCheck.checkAttack(gp.player, gp.npc[1][0]));
        gp.player.direction = "right";
        assertTrue( colCheck.checkAttack(gp.player, gp.npc[1][0]));

    }
}