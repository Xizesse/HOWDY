package test;

import main.GamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class GamePanelTest {

    private GamePanel gp;

    @BeforeEach
    public void setUp() {
        gp = new GamePanel();
    }

    @Test
    void startGameThread() {

        // Call the method you want to test
        gp.startGameThread();

        // Verify that the expected methods were called
        assertNotNull(gp, "gameThread not started");
    }


    @Test
    void addRemotePlayer() {
        //TODO: addRemotePlayer Test
    }

    @Test
    void windowTest(){
        assertEquals(768, gp.getPreferredSize().width);
        assertEquals(768, gp.getPreferredSize().height);
    }
    @Test
    void paintComponent() {
        // TODO: paintComponent Test
    }
}