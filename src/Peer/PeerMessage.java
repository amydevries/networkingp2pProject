package Peer;

import Sockets.ISocket;

import java.nio.ByteBuffer;

public class PeerMessage{

    private byte[] header;
    private byte[] type;
    private byte[] data;

    public byte[] getHeader() {
        return header;
    }

    public byte[] getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public PeerMessage(ISocket socket){
        header = new byte[4];
        type = new byte[1];
        int length;
        if(Integer.toString(socket.read(header)).equals("P2PF")){

        }else{
            length = byteArrayToInt(header);
            socket.read(type);
            switch (byteArrayToInt(type)) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    data = new byte[4];
                    socket.read(data);
                    break;
                case 5:
                    data = new byte[2];
                    socket.read(data);
                    break;
                case 6:
                    data = new byte[4];
                    socket.read(data);
                    break;
                case 7:
                    data = new byte[length - 1];
                    socket.read(data);
                    break;
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

