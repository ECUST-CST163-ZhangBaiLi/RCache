package com.bailizhang.lynxdb.socket.request;

import com.bailizhang.lynxdb.core.exceptions.NullFieldException;

import java.nio.channels.SelectionKey;

public class SocketRequest {
    public static final byte BLANK_FLAG = (byte) 0x00;
    public static final byte KEEP_CONNECTION = (byte) 0x01;

    protected final SelectionKey selectionKey;
    protected Byte status;
    protected Integer serial;
    protected byte[] data;

    protected SocketRequest(SelectionKey key) {
        selectionKey = key;
    }

    public SelectionKey selectionKey() {
        if(selectionKey == null) {
            throw new NullFieldException("selectionKey");
        }
        return selectionKey;
    }

    public void status(byte val) {
        if(status == null) {
            status = val;
        }
    }

    public byte status() {
        if(status == null) {
            throw new NullFieldException("status");
        }
        return status;
    }

    public void serial(int val) {
        if(serial == null) {
            serial = val;
        }
    }

    public int serial() {
        if(serial == null) {
            throw new NullFieldException("serial");
        }
        return serial;
    }

    public void data(byte[] val) {
        if(data == null) {
            data = val;
        }
    }

    public byte[] data() {
        if(data == null) {
            throw new NullFieldException("data");
        }
        return data;
    }

    public boolean isKeepConnection() {
        return (status & KEEP_CONNECTION) == KEEP_CONNECTION;
    }

    public boolean isBroadcast() {
        return selectionKey == null;
    }
}
