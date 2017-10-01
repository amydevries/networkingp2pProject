package Peer;

import java.nio.ByteBuffer;

public class PeerMessage{


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

}

