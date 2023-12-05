package effects;

import main.GamePanel;

import java.awt.*;

public class EffectManager {
    GamePanel gp;
    Light light;;
    public EffectManager(GamePanel gp) {
        this.gp = gp;
    }

    public void setup() {
        light = new Light(gp, 200);
    }

    public void draw(Graphics2D g2d) {
        light.draw(g2d);
    }
}
