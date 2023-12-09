package object;

import main.GamePanel;
import net.Packet06MapChange;
import net.TileChange;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class OBJ_PP extends SuperObject{
    public int[][] defaultArray;

    public int[][] changeArray;
    public GamePanel gp;

    public OBJ_PP(GamePanel gamePanel, int id, int[][] chArray , int[][] dfArray){
        name = "PP";
        //id = 4;
        this.changeArray = chArray;
        this.gp = gamePanel;
        this.defaultArray = dfArray;

    }


    @Override
    public void interact() {
        if (!isItOn ) {
            this.isItOn = true;
            ArrayList<TileChange> changes = new ArrayList<>();
            TileChange change;
            for (int[] ints : changeArray) {
                change = new TileChange(gp.currentMap, ints[0], ints[1], ints[2]);
                changes.add(change);
                System.out.println(changes + "\n");
            }
            Packet06MapChange p6 = new Packet06MapChange(0, changes);
            System.out.println("Sending map change packet");
            String[] dataArray = p6.readData(p6.getData()).split(",");
            p6.writeData(this.gp.socketClient);
            System.out.println(this.isItOn);
        }

    }

    @Override
    public void turnOff() {
        if (isItOn) {
            ArrayList<TileChange> changes = new ArrayList<>();
            TileChange change;

            for (int[] ints : defaultArray) {
                change = new TileChange(gp.currentMap, ints[0], ints[1], ints[2]);
                changes.add(change);
                System.out.println(changes + "\n");
            }

            Packet06MapChange p6 = new Packet06MapChange(0, changes);
            System.out.println("Sending map change back");
            String[] dataArray = p6.readData(p6.getData()).split(",");
            p6.writeData(this.gp.socketClient);
            this.isItOn = false;
        }
    }
}

