package test;

import entity.Player;
import main.CollisionChecker;
import main.GamePanel;
import main.KeyHandler;
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
    void checkTileCollisionOOB() {
        //TODO: CheckTileCollision Out of Bounds Test
        

    }
}