package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Helmet extends SuperObject{
    String rank = "";
    public OBJ_Helmet(GamePanel gp, String rank) {
        name = "Helmet";
        type = "helmet";
        this.rank = rank;
        if (rank.equals("gold")) {
            this.tier = 2;
        } else if (rank.equals("iron")) {
            this.tier = 1;
        }
        id = 1;
        equippable = true;

        if (rank.equals("iron")) {
            try {
                image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/helmet/helmet.png"));
                up = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/helmet/ironHelmet_up.png"));
                down = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/helmet/ironHelmet_down.png"));
                left = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/helmet/ironHelmet_left.png"));
                right = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/helmet/ironHelmet_right.png"));

                image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
                up = uT.scaleImage(up, gp.tileSize, gp.tileSize);
                down = uT.scaleImage(down, gp.tileSize, gp.tileSize);
                left = uT.scaleImage(left, gp.tileSize, gp.tileSize);
                right = uT.scaleImage(right, gp.tileSize, gp.tileSize);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (rank.equals("gold")) {
            try {
                image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/goldhelmet/goldHelmet.png"));
                up = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/goldhelmet/goldHelmet_up.png"));
                down = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/goldhelmet/goldHelmet_down.png"));
                left = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/goldhelmet/goldHelmet_left.png"));
                right = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/goldhelmet/goldHelmet_right.png"));

                image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
                up = uT.scaleImage(up, gp.tileSize, gp.tileSize);
                down = uT.scaleImage(down, gp.tileSize, gp.tileSize);
                left = uT.scaleImage(left, gp.tileSize, gp.tileSize);
                right = uT.scaleImage(right, gp.tileSize, gp.tileSize);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
