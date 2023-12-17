package net;

public class Packet07Ready extends Packet {

    private int ready;

    private String character;

    public Packet07Ready(byte[] data) {
        super(07);
    }

    public Packet07Ready(int ready, String direction) {
        super(07);
        this.ready = ready;
        this.character = direction;

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
        return ("07").getBytes();
    }

    public int getReady() {
        return ready;
    }
    public String getCharacter() {
        return character;
    }

}

