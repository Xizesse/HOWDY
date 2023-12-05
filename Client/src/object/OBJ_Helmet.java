package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Helmet extends SuperObject{

    public OBJ_Helmet(GamePanel gp){
        name = "Helmet";
        id = 1;
        equippable = true;

        try{
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

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
