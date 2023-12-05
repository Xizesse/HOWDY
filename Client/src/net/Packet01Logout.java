package net;

public class Packet01Logout extends Packet{
    public Packet01Logout(int packetId) {
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
