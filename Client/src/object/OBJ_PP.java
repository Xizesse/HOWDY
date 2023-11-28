package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_PP extends SuperObject{

    public OBJ_PP(GamePanel gp){
        name = "PP";
        id = 4;

//        try{
//            image = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("tiles/pp_clover4.png")));
//            image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
//        }catch (IOException e){
//            e.printStackTrace();
//        }

    }
}

