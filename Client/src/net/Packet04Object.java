package net;

public class Packet04Object extends Packet {
    private int map;
    private int index;
    private char give; // 'T' for true, 'F' for false

    public Packet04Object(byte[] data) {
        super(04);
        String[] dataArray = new String(data).trim().split(",");

        if (dataArray.length > 2) {
            try {
                this.map = Integer.parseInt(dataArray[0].substring(2));
                this.index = Integer.parseInt(dataArray[1]);
                this.give = dataArray[2].trim().charAt(0);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle the case where map or index is not a valid integer
            }
        }
    }

    public Packet04Object(int map, int itemID, boolean give) {
        super(04);
        this.map = map;
        this.index = itemID;
        this.give = give ? 'T' : 'F';
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
        System.out.println("Sending object packet");
    }

    @Override
    public void writeData(GameServer server) {
        // Implement if needed
    }

    @Override
    public byte[] getData() {
        return ("04" + this.map + "," + this.index + "," + this.give).getBytes();
    }

    public int getMap() {
        return this.map;
    }

    public int getitemIndex() {
        return this.index;
    }

    public boolean getGive() {
        return give == 'T';
    }
}