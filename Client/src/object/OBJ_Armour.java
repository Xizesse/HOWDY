package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Armour extends SuperObject{
    String rank = "";
    public OBJ_Armour(GamePanel gp, String rank) {
        name = "Armour";
        type = "armour";
        this.rank = rank;
        if (rank.equals("gold")) {
            this.tier = 2;
        } else if (rank.equals("iron")) {
            this.tier = 1;
        }
        id = 1;
        equippable = true;

        if (rank.equals("gold")) {
            try {
                image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/goldArmour/goldArmour.png"));
                up = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/goldArmour/goldArmour_up.png"));
                down = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/goldArmour/goldArmour_down.png"));
                left = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/goldArmour/goldArmour_left.png"));
                image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
                up = uT.scaleImage(up, gp.tileSize, gp.tileSize);
                down = uT.scaleImage(down, gp.tileSize, gp.tileSize);
                left = uT.scaleImage(left, gp.tileSize, gp.tileSize);
                right = down;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (rank.equals("iron")) {
            try {
                image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/armour/armour.png"));
                up = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/armour/armour_up.png"));
                down = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/armour/armour_down.png"));
                right = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/armour/armour_right.png"));

                image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
                up = uT.scaleImage(up, gp.tileSize, gp.tileSize);
                down = uT.scaleImage(down, gp.tileSize, gp.tileSize);
                right = uT.scaleImage(right, gp.tileSize, gp.tileSize);
                left = down;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}