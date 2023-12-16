package net;

public class Packet05Health extends Packet{
    private int entityID;
    private int health;
    private int map;


    public Packet05Health(byte[] data) {
        super(05);
        String[] dataArray = readData(data).split(",");
        this.entityID = Integer.parseInt(dataArray[0]);
        this.health = Integer.parseInt(dataArray[1]);
        this.map = Integer.parseInt(dataArray[2]);



    }

    public Packet05Health(int entityID, int health, int map) {
        super(05);
        this.entityID = entityID;
        this.health = health;
        this.map = map;

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
        return ("05" + this.entityID + "," + this.health + "," + this.map).getBytes();
    }

    public int getEntityID(){
        return entityID;
    }
    public int getHealth(){
        return health;
    }
    public int getMap(){
        return map;
    }


}
