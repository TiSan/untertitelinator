package de.tisan.church.untertitelinator.instancer.packets;

import de.tisan.church.untertitelinator.instancer.UTInstance;

public class ConnectionStatusPacket extends Packet{

    ConnectionType connectionType;
    UTInstance module;
    boolean connected;

    public ConnectionStatusPacket() {

    }

    public ConnectionStatusPacket(ConnectionType type, UTInstance module, boolean connected) {
        this.connectionType = type;
        this.module = module;
        this.connected = connected;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public boolean isConnected() {
        return connected;
    }

    public UTInstance getModule() {
        return module;
    }

    @Override
    public String toString() {
        return "ConnectionType: " + connectionType + ", Module: " + module + ", isConnected: " + connected;
    }

    public enum ConnectionType {
        SERVER, CLIENT
    }
}
