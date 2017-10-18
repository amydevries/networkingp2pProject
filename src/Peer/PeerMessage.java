package Peer;

import Sockets.ISocket;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.io.IOException;

public class PeerMessage{

    private byte[] header;
    private byte[] type;
    private byte[] data;
    private byte[] peerId;

    public byte[] getHeader() {
        return header;
    }

    public byte[] getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public PeerMessage(ISocket socket) throws IOException {
        header = new byte[4];
        type = new byte[1];
        peerId  = new byte[4];

        int length;

        socket.read(header);
        String header_str = new String(header, Charset.forName("US-ASCII"));

        if (header_str.equals("P2PF")) {
                byte [] header_cont = new byte[14];
                byte [] zeros = new byte[10];

                peerId = new byte[4];

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

                socket.read(peerId);
        } else {
            length = byteArrayToInt(header);
            socket.read(type);

            data = new byte[length];

            if (length > 0) {
                if (socket.read(data) != length) {
                    throw new IOException("EOF in PeerMessage constructor: " +
                            "Unexpected message data length");
                }

            }
        }
    }


    public byte[] createActualMessage(int length, int messageType, byte[] messagePayload, String type){

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
        int size = messagePayload.length + 5;

        ByteBuffer actualMsgBuffer = ByteBuffer.allocate(size);
        actualMsgBuffer.putInt(size);
        actualMsgBuffer.putInt(messageType);
        actualMsgBuffer.put(messagePayload);
        actualMessage = actualMsgBuffer.array();

        return actualMessage;
    }

    public byte[] createHandshakeMessage(int peerID){
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

}

