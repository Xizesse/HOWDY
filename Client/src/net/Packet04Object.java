package net;

public class Packet04Object extends Packet{

    private char itemID;
    //0 for the other player
    private boolean give;

    public Packet04Object(byte[] data) {
        super(04);
    }

    public Packet04Object(char itemID, boolean give) {
        super(04);
        this.itemID = itemID;
        this.give = give;
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
        return ("04" + this.itemID + "," + this.give).getBytes();
    }

    public char getItemID(){
        return itemID;
    }

    public boolean getGive(){
        return give;
    }


}
