package Peer;

import java.nio.ByteBuffer;
import Sockets.BasicSocket;

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
    public static void sendingnotInterested(BasicSocket socket){
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
        byte [] message = Message.createActualMessage("butfield", bitfield );
        socket.write(message);
    }
    public static void sendingRequest(BasicSocket socket, int index){
        synchronized(Peer.getFileHandler().getPiece(index)){};
        if(!Peer.getFileHandler().getPiece(index).hasBeenRequested()){
            Peer.getFileHandler().getPiece(index).request();
            //TODO: FINISH THIS AND PIECE MESSAGE
        }
        byte [] message = Message.createActualMessage("not interested", new byte[0] );
        socket.write(message);
    }



}
