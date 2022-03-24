package de.tisan.church.untertitelinator.instancer.packets;

import de.tisan.church.untertitelinator.instancer.UTInstanceType;

public class KeepAlivePacket extends Packet {

    private static final long serialVersionUID = 5091431982503004621L;
    private long ts;
    private UTInstanceType instance;


    public KeepAlivePacket() {
        ts = System.currentTimeMillis();
    }

    public KeepAlivePacket(UTInstanceType instance) {
        this();
        this.instance = instance;
    }

    public UTInstanceType getInstance() {
        return instance;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public void setInstance(UTInstanceType instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "KeepAlivePacket";
    }

}
