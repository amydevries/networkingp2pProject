package Peer;

import Factory.SocketFactory;
import Handlers.*;
import Singleton.StorageSingleton;
import Sockets.BasicSocket;
import FileHandling.PeerInfoReader;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Peer extends Thread{

    public static Integer CHOKEMESSAGE = 0;
    public static Integer UNCHOKEMESSAGE = 1;
    public static Integer INTERESTEDMESSAGE = 2;
    public static Integer NOTINTERESTEDMESSAGE = 3;
    public static Integer HAVEMESSAGE = 4;
    public static Integer BITFIELDMESSAGE = 5;
    public static Integer REQUESTMESSAGE = 6;
    public static Integer PIECEMESSAGE = 7;

    private PeerInfo peerInfo;
    private boolean shutdown;
    StorageSingleton storageSingleton = StorageSingleton.getInstance();
    private Hashtable<Integer,PeerInfo> peers = storageSingleton.getPeers();
    private Hashtable<Integer, IHandler> handlers = storageSingleton.getHandlers();


    public Peer(int myID){
        peerInfo = new PeerInfo(myID);

        peers = peerInfo.getNeighborPeers(myID);

        handlers.put(CHOKEMESSAGE,new ChokeHandler(this));
        handlers.put(UNCHOKEMESSAGE,new UnChokeHandler(this));
        handlers.put(INTERESTEDMESSAGE,new InterestedHandler(this));
        handlers.put(NOTINTERESTEDMESSAGE, new NotInterestedHandler(this));
        handlers.put(HAVEMESSAGE,new HaveHandler(this));
        handlers.put(BITFIELDMESSAGE, new BitFieldHandler(this));
        handlers.put(REQUESTMESSAGE, new RequestHandler(this));
        handlers.put(PIECEMESSAGE, new PieceHandler(this));

        try{
            ServerSocket serverSocket = SocketFactory.getSocketFactory().makeServerSocket(peerInfo.getPort());

            while(!shutdown){
                try{
                    Socket clientSocket = serverSocket.accept();

                    PeerHandler peerHandler = new PeerHandler(clientSocket);
                    peerHandler.start();
                }catch (Exception e){}
            }

            serverSocket.close();

        }catch (Exception e){}

        shutdown = true;
    }

    public PeerInfo getPeer(int peerID){
        for (int key : peers.keySet())
            if (peerInfo.getPeerID() == peerID)
                return peers.get(key);
        return null;
    }

    public Hashtable<Integer, IHandler> getHandlers() {
        return handlers;
    }


/*
    private void startConnections(int myID){
        for(int i = 0; i < peerInfoReader.getNumberOfPeers(); ++i){
            if(myID == peerInfoReader.getPeerIDS(i)) break;
            BasicSocket temp = new BasicSocket(peerInfoReader.getPeerHostNames(i),peerInfoReader.getPeerIDS(i));
            temp.start();
            socketClients.add(temp);
        }
    }
*/

}
