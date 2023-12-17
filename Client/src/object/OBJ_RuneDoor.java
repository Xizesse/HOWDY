package object;

import main.GamePanel;
import net.Packet06MapChange;
import net.TileChange;

import java.awt.*;
import java.util.ArrayList;

public class OBJ_RuneDoor extends SuperObject{
    char shape = 'a';
    public GamePanel gp;
public OBJ_RuneDoor(GamePanel gp, char shape) {
        name = "RuneDoor";
        type = "door";
        this.shape = shape;
        equippable = false;
        this.gp = gp;
        this.solidArea = new Rectangle(-1,-1,50,50);

    }


    @Override
    public void interact() {
        for (SuperObject obj : gp.player.inventory) {
            if (obj instanceof OBJ_Rune) {
                if (((OBJ_Rune) obj).shape == this.shape) {
                    ArrayList<TileChange> changes = new ArrayList<>();
                    TileChange change;
                    change = new TileChange(gp.currentMap, this.worldX /gp.tileSize, this.worldY/gp.tileSize, 50);
                    changes.add(change);
                    Packet06MapChange p6 = new Packet06MapChange(gp.currentMap, changes);
                    System.out.println("Sending map change packet");
                    String[] dataArray = p6.readData(p6.getData()).split(",");
                    p6.writeData(this.gp.socketClient);
                    break;
                }
            }
        }


    }
}
