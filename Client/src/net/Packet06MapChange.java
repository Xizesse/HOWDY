
package net;


import java.util.ArrayList;
import java.util.List;

public class Packet06MapChange extends Packet{
    private int level;
    private int nChanges;
    private List<TileChange> changes;
    public Packet06MapChange(int level, List<TileChange> changes) {
        super(06);
        System.out.println("Packet06MapChange Created");
        this.level = level;
        this.nChanges = changes.size();
        this.changes = changes;
    }

    public Packet06MapChange(byte[] data) {
        super(06);
        System.out.println("Packet06MapChange Created");
        String[] dataArray = new String(data).trim().split(",");
        try {
            this.level = Integer.parseInt(dataArray[1]); // level is after packet ID
            this.nChanges = Integer.parseInt(dataArray[2]); // number of changes is after level


            this.changes = new ArrayList<>();
            for (int i = 0; i < this.nChanges; i++) {
                // Each change consists of 3 parts: x, y, and newTile, hence the index calculation
                int index = 3 + i * 3;
                if (index + 2 >= dataArray.length) {
                    break;
                }
                int x = Integer.parseInt(dataArray[index]);
                int y = Integer.parseInt(dataArray[index + 1]);
                int newTile = Integer.parseInt(dataArray[index + 2]);

                this.changes.add(new TileChange(x, y, newTile));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Handle the case where parsing fails
        }
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {

    }

    @Override
    public byte[] getData() {
        StringBuilder builder = new StringBuilder();
        builder.append("06,");
        builder.append(this.level);
        builder.append(",");
        builder.append(this.nChanges);
        for (TileChange change : this.changes) {
            builder.append(",");
            builder.append(change.getX());
            builder.append(",");
            builder.append(change.getY());
            builder.append(",");
            builder.append(change.getNewTile());
        }
        return builder.toString().getBytes();
    }

    public int getLevel() {
        return level;
    }
    public int getnChanges() {
        return nChanges;
    }
    public List<TileChange> getChanges() {
        return changes;
    }

}
