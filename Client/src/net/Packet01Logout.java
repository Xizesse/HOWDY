package net;

import net.GameServer;
import net.GameClient;
import net.Packet;
public class Packet01Logout extends Packet{
    int win = 0;
    //00 for login
    //001 to : player already logged in
    //000 to : no players logged in

    public Packet01Logout(byte[] data) {
        super(01);
        this.win = Integer.parseInt(readData(data));
    }
    public Packet01Logout(int win){
        super(01);
        this.win = win;
    }
    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {

    }

    @Override
    public  byte[] getData(){
        return ("01" + this.win).getBytes(); }

    public int getWin() {
        return win;
    }


}
