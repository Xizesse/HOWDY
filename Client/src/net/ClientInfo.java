package net;

import java.net.InetAddress;

public class ClientInfo {
    public InetAddress ipAddress;
    public int port;

    public ClientInfo(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }
}
