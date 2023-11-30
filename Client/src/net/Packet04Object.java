package net;

public class Packet04Object extends Packet {
    private int index;
    private char give; // 'T' for true, 'F' for false

    public Packet04Object(byte[] data) {
        super(04);
        String[] dataArray = new String(data).trim().split(",");

        if (dataArray.length > 1) {
            String indexString = dataArray[0].substring(2);
            try {
                this.index = Integer.parseInt(indexString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle the case where the index is not a valid integer
            }

            // Parse the give character
            this.give = dataArray[1].trim().charAt(0);
        }
    }

    public Packet04Object(int itemID, boolean give) {
        super(04);
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
    }

    @Override
    public byte[] getData() {
        return ("04" + this.index + "," + this.give).getBytes();
    }

    public int getitemIndex() {
        return (int) index;
    }



    public boolean getGive() {
        return give == 'T';
    }
}