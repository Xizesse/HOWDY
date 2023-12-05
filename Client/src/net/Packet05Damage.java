package net;

public class Packet05Damage extends Packet{

    public Packet05Damage(int packetId) {
        super(packetId);
    }

    @Override
    public void writeData(GameClient client) {

    }

    @Override
    public void writeData(GameServer server) {

    }

    @Override
    public byte[] getData() {
        return new byte[0];
    }
}
