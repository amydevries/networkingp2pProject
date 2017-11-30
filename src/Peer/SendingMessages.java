package Peer;

import java.nio.ByteBuffer;
import Sockets.BasicSocket;
import peerbase.PeerConnection;

public class SendingMessages {

    public static void sendingHandshake(BasicSocket socket,int peerID){
        Message message = new Message(peerID);
        socket.write(message);
    }

    public static void sendingChoke(BasicSocket socket){
        Message message = new Message("choke", new byte[0] );
        socket.write(message);
    }

    public static void sendingUnChoke(BasicSocket socket){
        Message message = new Message("unchoke", new byte[0] );
        socket.write(message);
    }

    public static void sendingInterested(BasicSocket socket){
        Message message = new Message("interested", new byte[0] );
        socket.write(message);
    }

    public static void sendingNotInterested(BasicSocket socket){
        Message message = new Message("not interested", new byte[0] );
        socket.write(message);
    }

    public static void sendingHave(BasicSocket socket, int messagePayload){
        ByteBuffer indexBuffer = ByteBuffer.allocate(4);
        indexBuffer.putInt(messagePayload);
        Message message = new Message("have", indexBuffer.array());
        socket.write(message);
    }

    public static void sendingBitField(BasicSocket socket, byte[] bitfield){
        Message message = new Message("bitfield", bitfield );
        socket.write(message);
    }

    public synchronized static void sendingRequest(BasicSocket socket, int index){
            ByteBuffer indexBuffer = ByteBuffer.allocate(4);
            indexBuffer.putInt(index);
            Message message = new Message("request", indexBuffer.array());
            socket.write(message);
    }

    public static void sendingPiece(BasicSocket socket,int index, byte[] messagePayload) {
        ByteBuffer indexBuffer = ByteBuffer.allocate(messagePayload.length + 4);
        indexBuffer.putInt(index);
        indexBuffer.put(messagePayload);
        Message message = new Message("piece", indexBuffer.array());
        socket.write(message);

    }

}
