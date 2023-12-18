package net;

public class Packet07Ready extends Packet {

    private int ready;
    private int character;
    private int start;

    public Packet07Ready(byte[] data) {
        super(07);
        String[] dataArray = readData(data).split(",");
        this.ready = Integer.parseInt(dataArray[0]);
        this.character = Integer.parseInt(dataArray[1]);
        this.start = Integer.parseInt(dataArray[2]);

    }

    public Packet07Ready(int ready, int character, int start) {
        super(07);
        this.ready = ready;
        this.character = character;
        this.start = start;

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
        return ("07" + this.ready + "," + this.character + "," + this.start).getBytes();
    }

    public int getReady() {
        return ready;
    }
    public int getCharacter() {
        return character;
    }

    public int getStart() {
        return start;
    }

}

