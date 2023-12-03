package net;

public class Packet03Attack extends Packet{
    private int entityID;
    int attacks;
    //0 for the other player
    //private int x,y;
    //private String direction;

    public Packet03Attack(byte[] data) {
        super(03);
        String[] dataArray = readData(data).split(",");
        this.entityID = Integer.parseInt(dataArray[0]);
        this.attacks = Integer.parseInt(dataArray[1]);


    }

    public Packet03Attack(int entityID, int attacks) {
        super(03);
        this.entityID = entityID;
        this.attacks = attacks;


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
        return ("03" + this.entityID + "," + this.attacks).getBytes();
    }


    public int getEntityID(){
        return entityID;
    }
    public int getAttacks(){
        return attacks;
    }

}
