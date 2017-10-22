package Peer;

import Sockets.ISocket;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.io.IOException;

public class PeerMessage{

    private byte[] header;
    private byte[] type;
    private byte[] data;
    private byte[] peerID;

    public byte[] getHeader() {
        return header;
    }

    public byte[] getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public int getPeerID() {return byteArrayToInt(peerID); }

    public PeerMessage() {}

    public PeerMessage(ISocket socket) throws IOException {
        header = new byte[4];

        peerID = new byte[4];

        int length;

        socket.read(header);
        String header_str = new String(header, Charset.forName("US-ASCII"));

        if (header_str.equals("P2PF")) {
                byte [] header_cont = new byte[14];
                byte [] zeros = new byte[10];

                peerID = new byte[4];

                socket.read(header_cont);  // TODO: chech that we read 14 bytes and if not throw the exception
                String header_cont_str = new String(header_cont, Charset.forName("US-ASCII"));
                if(header_cont_str.equals("ILESHARINGPROJ")) {
                    // TODO: throw excepetion
                    return;
                }
                socket.read(zeros);

                for (int i = 0; i < 10; i++) {
                    if (zeros[i] != 0) {
                        // TODO: throw exception
                    }
                }

                socket.read(peerID);
        } else {
            length = byteArrayToInt(header);
            type = new byte[1];
            socket.read(type);

            data = new byte[length-1];

            if (length > 0) {
                if (socket.read(data) != length) {
                    throw new IOException("EOF in PeerMessage constructor: " +
                            "Unexpected message data length");
                }

            }
        }
    }

    public PeerMessage(int type, byte[] data){
        this.type = intToByteArray(type);
        this.data = data;
    }


    static public byte[] createActualMessage(String type, byte[] messagePayload){

        int messageType = 0;


        //determine the message type code
        switch(type){
            case "choke": messageType = 0; break;
            case "unchoke": messageType = 1; break;
            case "interested": messageType = 2; break;
            case "not interested": messageType = 3; break;
            case "have": messageType = 4; break;
            case "bitfield": messageType = 5; break;
            case "request": messageType =6; break;
            case "piece": messageType = 7; break;
        }

        byte[] actualMessage;

        //the size of the actual message is the payload + 1 for type + 4 for message length
        int length = messagePayload.length + 1;

        ByteBuffer actualMsgBuffer = ByteBuffer.allocate(length);
        actualMsgBuffer.putInt(length);
        actualMsgBuffer.putInt(messageType);
        actualMsgBuffer.put(messagePayload);
        actualMessage = actualMsgBuffer.array();

        return actualMessage;
    }

    static public byte[] createHandshakeMessage(int peerID){
        byte[] handshakeMsg;
        //Handshake message is 32 bytes
        ByteBuffer shakeMsgBuffer = ByteBuffer.allocate(32);
        shakeMsgBuffer.put(("P2PFILESHARINGPROJ").getBytes());
        //18 zero-bits
        shakeMsgBuffer.put(ByteBuffer.allocate(18));
        shakeMsgBuffer.putInt(peerID);
        handshakeMsg = shakeMsgBuffer.array();

        return handshakeMsg;
    }

    public static int byteArrayToInt(byte[] byteArray) {
        int integer = 0;
        for (int i = 0; i < byteArray.length; i++) {
            integer = (integer << 8) | ( ((int)byteArray[i]) & 0xff );
        }

        return integer;
    }

    public static byte[] intToByteArray(final int number){
        int byteNum = (40 - Integer.numberOfLeadingZeros (number < 0 ? ~number : number)) / 8;
        byte[] byteArray = new byte[4];

        for (int n = 0; n < byteNum; n++)
            byteArray[3 - n] = (byte) (number >>> (n * 8));

        return (byteArray);
    }

}

