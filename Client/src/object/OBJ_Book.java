package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Book extends SuperObject{


    String chapters[] = new String[20];
    int chapterIndex = 0;
    public OBJ_Book(GamePanel gp){
        name = "Book";
        id = 3;
        setChapters();

        try{
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/book.png"));
            image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setChapters() {
    	chapters[0] = "Howdy ? \nYou fell into a dungeon. \nYou are now trapped. \nYou must find a way out.";
        chapters[1] = "YOU WILL DIE \nIF YOU DONT \nWORK TOGETHER";

    }

    @Override
    public void readChapter(GamePanel gp) {
        if (chapters[chapterIndex] == null) {
            chapterIndex = 0;
        }
        gp.ui.currentText = chapters[chapterIndex];
        chapterIndex++;
    }
}
