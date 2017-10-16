package Handlers;

import Factory.SocketFactory;
import Logger.PeerLogger;
import Peer.PeerConnection;
import Peer.PeerMessage;
import Sockets.BasicSocket;
import java.util.Hashtable;
import Handlers.IHandler;
import java.util.Dictionary;
import java.lang.String;

import java.io.IOException;

public class HendlerReader  implements IHandler {

    private Hashtable<Integer,IHandler> handlers = new Hashtable<Integer, IHandler>();

    public static Integer ChokeMessage = 0;
    public static Integer UnChokeMessage = 1;
    public static Integer InterestedMessage = 2;
    public static Integer NotInterestedMessage = 3;
    public static Integer HaveMessage = 4;
    public static Integer BitFieldMessage = 5;
    public static Integer RequestMessage = 6;
    public static Integer PieceMessage = 7;


    public void createHandlers(){
        handlers.put(ChokeMessage,new ChokeHandler());
        handlers.put(UnChokeMessage,new UnChokeHandler());
        handlers.put(InterestedMessage,new InterestedHandler());
        handlers.put(NotInterestedMessage, new NotInterestedHandler());
        handlers.put(HaveMessage,new HaveHandler());
        handlers.put(BitFieldMessage, new BitFieldHandler());
        handlers.put(RequestMessage, new RequestHandler());
        handlers.put(PieceMessage, new PieceHandler());
    }


    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage){

    };

};

// XXXXXXX
public PeerMessage(SocketInterface s) throws IOException {

        type = new byte[1];

        type[0] = 3;
        data = new byte[4];
        data[0] = 0;
        data[1] = 1;
        data[2] = 2;
        data[3] = 3;

        return;

        //  read the length
        byte[] thelen = new byte[4]; // for reading length of message data
        if (s.read(thelen) != 4)
        throw new IOException("EOF in PeerMessage constructor: thelen");

        int len = byteArrayToInt(thelen);

        // read the type
        type = new byte[1];

        if (s.read(type) != 1)
        throw new IOException("EOF in PeerMessage constructor: type");

        // read the data
        data = new byte[len];

        if (s.read(data) != len)
        throw new IOException("EOF in PeerMessage constructor: " +
        "Unexpected message data length");
        }
        }









