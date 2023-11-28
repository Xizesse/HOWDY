package object;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class OBJ_Heart extends SuperObject{
    GamePanel gp;
    public OBJ_Heart(GamePanel gp){
        this.gp = gp;
        name = "Heart";
        try{
            image3 = ImageIO.read(ClassLoader.getSystemResourceAsStream("hearts/heart_empty.png"));
            image3 = uT.scaleImage(image3, gp.tileSize, gp.tileSize);
            image2 = ImageIO.read(ClassLoader.getSystemResourceAsStream("hearts/heart_half.png"));
            image2 = uT.scaleImage(image2, gp.tileSize, gp.tileSize);
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("hearts/heart_full.png"));
            image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
            //image = ImageIO.read(ClassLoader.getSystemResourceAsStream("/items/helmet.png"));
            //image2 = ImageIO.read(ClassLoader.getSystemResourceAsStream("/hearts/heart_half.png"));
            //image3 = ImageIO.read(ClassLoader.getSystemResourceAsStream("/hearts/heart_empty.png"));
            //image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
            //image2 = uT.scaleImage(image2, gp.tileSize, gp.tileSize);
            //image3 = uT.scaleImage(image3, gp.tileSize, gp.tileSize);

    }catch (Exception e){
            e.printStackTrace();
        }
    }


}
