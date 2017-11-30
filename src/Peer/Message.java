package Peer;

import Sockets.BasicSocket;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.Arrays;

public class Message implements Serializable{

    private byte[] header = null;
    private byte[] type = null;
    private byte[] data = null;
    private byte[] peerID = null;

    public Message(int peerID){
        String header = "P2PFILESHARINGPROJ";
        this.header = header.getBytes();
        this.peerID = intToByteArray(peerID);
    }

    public Message(String type, byte[] payload){
        byte messageType = 50;
        switch(type){
            case "choke": messageType = 0; break;
            case "unchoke": messageType = 1; break;
            case "interested": messageType = 2; break;
            case "not interested": messageType = 3; break;
            case "have": messageType = 4; break;
            case "bitfield": messageType = 5; break;
            case "request": messageType =6; break;
            case "piece": messageType = 7; break;
            case "handshake": messageType = 99; break;
        }
        this.type = new byte[1];
        this.type[0] = messageType;
        this.header = intToByteArray(payload.length + 1);
        this.data = payload;

    }

    public Message(BasicSocket socket) throws IOException {
        synchronized (this) {
            Message message = socket.read();
            this.header = message.header;
            this.type = message.type;
            this.data = message.data;
            this.peerID = message.peerID;
        }
    }

    public static int byteArrayToInt(byte[] byteArray) {

        return ByteBuffer.wrap(byteArray).getInt();
    }

    public static byte[] intToByteArray(final int number){
        int byteNum = (40 - Integer.numberOfLeadingZeros (number < 0 ? ~number : number)) / 8;
        byte[] byteArray = new byte[4];

        for (int n = 0; n < byteNum; n++)
            byteArray[3 - n] = (byte) (number >>> (n * 8));

        return (byteArray);
    }

    public byte[] getHeader() {
        return header;
    }

    public int getType() {
        if(type == null) return 99;
        else return (type[0]);
    }

    public byte[] getData() {
        return data;
    }

    public int getPeerID() {return byteArrayToInt(peerID); }
}

