package object;

import main.GamePanel;
import net.Packet06MapChange;
import net.TileChange;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class OBJ_PP extends SuperObject{
    public int[][] changeArray;
    public GamePanel gp;
    public OBJ_PP(GamePanel gamePanel, int id, int[][] chArray ){
        name = "PP";
        //id = 4;
        this.changeArray = chArray;
        this.gp = gamePanel;


    }


    @Override
    public void interact() {
        ArrayList<TileChange> changes = new ArrayList<>();
        TileChange change;

        for (int i = 0; i < changeArray.length; ++i) {
            change = new TileChange(changeArray[i][0],changeArray[i][1], changeArray[i][2]);
            changes.add(change);
            System.out.println(changes + "\n");
        }

        Packet06MapChange p6 = new Packet06MapChange(0, changes);
        System.out.println("Sending map change packet");
        String[] dataArray = p6.readData(p6.getData()).split(",");
        p6.writeData(this.gp.socketClient);
    }
}

