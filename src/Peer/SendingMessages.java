package Peer;

import java.nio.ByteBuffer;
import Sockets.BasicSocket;
import peerbase.PeerConnection;

public class SendingMessages {

    public static void sendingHandshake(BasicSocket socket,int peerID){
        byte [] message = Message.createHandshakeMessage(peerID);
        socket.write(message);
    }

    public static void sendingChoke(BasicSocket socket){
       byte [] message = Message.createActualMessage("choke", new byte[0] );
        socket.write(message);
    }

    public static void sendingUnChoke(BasicSocket socket){
        byte [] message = Message.createActualMessage("unchoke", new byte[0] );
        socket.write(message);
    }

    public static void sendingInterested(BasicSocket socket){
        byte [] message = Message.createActualMessage("interested", new byte[0] );
        socket.write(message);
    }

    public static void sendingNotInterested(BasicSocket socket){
        byte [] message = Message.createActualMessage("not interested", new byte[0] );
        socket.write(message);
    }

    public static void sendingHave(BasicSocket socket, int messagePayload){
        ByteBuffer indexBuffer = ByteBuffer.allocate(4);
        indexBuffer.putInt(messagePayload);
        byte [] message = Message.createActualMessage("have", indexBuffer.array());
        socket.write(message);
    }

    public static void sendingBitField(BasicSocket socket, byte[] bitfield){
        byte [] message = Message.createActualMessage("bitfield", bitfield );
        socket.write(message);
    }

    public synchronized static void sendingRequest(BasicSocket socket, int index){
            ByteBuffer indexBuffer = ByteBuffer.allocate(4);
            indexBuffer.putInt(index);
            byte[] message = Message.createActualMessage("request", indexBuffer.array());
            socket.write(message);
    }

    public static void sendingPiece(BasicSocket socket,int index, byte[] messagePayload) {
        ByteBuffer indexBuffer = ByteBuffer.allocate(messagePayload.length + 4);
        indexBuffer.putInt(index);
        indexBuffer.put(messagePayload);
        byte[] message = Message.createActualMessage("piece", indexBuffer.array());
        socket.write(message);

    }

}
