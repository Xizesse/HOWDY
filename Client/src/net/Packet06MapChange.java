
package net;


import java.util.List;

public class Packet06MapChange extends Packet{
    private int level;
    private int nChanges;
    private List<TileChange> changes;
    public Packet06MapChange(int packetId) {
        super(06);
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
        builder.append("06");
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



}
