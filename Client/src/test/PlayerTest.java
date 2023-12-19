package test;

import entity.Player;
import main.CollisionChecker;
import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Armour;
import object.OBJ_Helmet;
import object.OBJ_Katana;
import object.OBJ_Rune;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private GamePanel gp;
    private Player player;
    private KeyHandler kH;

    @BeforeEach
    void setUp() throws IOException {
        gp = new GamePanel();
        player = new Player(gp, kH, 15, 15, 0);
    }

    @Test
    void giveItemTest() {
        assertNull(player.weapon);
        assertNull(player.helmet);
        assertNull(player.armour);
        assertNull(player.boots);
        assertTrue(player.inventory.isEmpty());
        player.giveItem(new OBJ_Armour(gp, "gold"));
        assertNotNull(player.armour);
        player.giveItem(new OBJ_Helmet(gp, "gold"));
        assertNotNull(player.helmet);
        player.giveItem(new OBJ_Katana(gp));
        assertNotNull(player.armour);
        OBJ_Rune rune = new OBJ_Rune(gp, 'p');
        player.giveItem(rune);
        assertTrue(player.inventory.contains(rune));

    }

}
