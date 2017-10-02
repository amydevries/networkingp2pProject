package Peer;

import Factory.SocketFactory;
import Handlers.IHandler;
import Singleton.StorageSingleton;
import Sockets.BasicSocket;
import FileHandling.PeerInfoReader;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Peer extends Thread{

    private PeerInfo peerInfo;
    private boolean shutdown;
    StorageSingleton storageSingleton = StorageSingleton.getInstance();
    private Hashtable<Integer,PeerInfo> peers = storageSingleton.getPeers();
    private Hashtable<String, IHandler> handlers = storageSingleton.getHandlers();


    public Peer(int myID){
        peerInfo = new PeerInfo(myID);

        peers = peerInfo.getNeighborPeers(myID);

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

    public Hashtable<String, IHandler> getHandlers() {
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
