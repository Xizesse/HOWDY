package net;

public class Packet04Object extends Packet{
    private char itemID;
    private char give; // 'T' for true, 'F' for false
    //0 for the other player

    public Packet04Object(byte[] data) {
        super(04);
        String[] dataArray = new String(data).trim().split(",");
        if (dataArray.length > 1) {
            this.itemID = dataArray[0].charAt(2); // Assuming the ID is at position 2
            this.give = dataArray[1].trim().charAt(0); // Assuming the give char is the first character
        }

    }

    public Packet04Object(char itemID, boolean give) {
        super(04);
        this.itemID = itemID;
        this.give = give ? 'T' : 'F';
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
        return give=='T';
    }


}
