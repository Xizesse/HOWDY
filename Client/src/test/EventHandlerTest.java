package test;

import entity.Player;
import main.GamePanel;
import main.KeyHandler;
import net.GameClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.EventHandler;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EventHandlerTest {
    GamePanel gp;
    EventHandler eH;
    @BeforeEach
    void setUp() throws IOException {
        gp = new GamePanel();
        eH = new EventHandler(gp);
    }
    @Test
    void hitTest() {
        gp.player = new Player(gp, new KeyHandler(gp),10, 3, 0 );
        assertTrue(eH.hit(0, 10, 3, "any"));
        assertFalse(eH.hit(0, 30, 15, "any"));
    }

    @Test
    void dmgPitTest() {
        this.gp.socketClient = new GameClient(gp, "localhost");
        gp.player = new Player(gp, new KeyHandler(gp),0, 5, 5 );
        int health = gp.player.currentHealth;
        eH.dmgPit(gp.playState);
        assertEquals(health-1, gp.player.currentHealth );
    }
    @Test
    void deathTest() {
        this.gp.socketClient = new GameClient(gp, "localhost");
        gp.player = new Player(gp, new KeyHandler(gp),1, 5, 5 );
        eH.death(gp.playState, 0, 15, 15);
        assertEquals(6, gp.player.currentHealth );
        assertEquals(0, gp.currentMap);
        assertEquals(15, gp.player.worldX/gp.tileSize);
        assertEquals(15, gp.player.worldY/gp.tileSize);
    }

    @Test
    void teleportTest() {
        Player player = new Player(gp, new KeyHandler(gp),0, 0, 0 );
        player.worldX = 10;
        player.worldY = 30;
        eH.teleport( gp.playState, 1, 1, 15);
        assertEquals(1, gp.currentMap);
        assertEquals(10, player.worldX);
        assertEquals(30, player.worldY);
    }


}