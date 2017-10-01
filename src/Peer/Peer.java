package Peer;

import Connection.Neighbors;
import Connection.SocketClient;
import Connection.SocketServer;
import FileHandling.PeerInfoReader;

import java.util.ArrayList;
import java.util.List;

public class Peer extends Thread{

    static private List<Thread> threads = new ArrayList<Thread>();
    private List<Neighbors> socketClients = new ArrayList<Neighbors>();

    private PeerInfo peerInfo;
    private PeerInfoReader peerInfoReader;
    private SocketServer socketServer;

    public Peer(int myID){
        peerInfoReader = new PeerInfoReader();
        peerInfoReader.parse();

        getPeerInfo(myID);
        startConnections(myID);
        startServer(peerInfo.getPort());

        threads.add(this);
        this.start();
    }

    private void getPeerInfo(int myID){
        for(int i = 0; i < peerInfoReader.getNumberOfPeers(); ++i){
            if(myID == peerInfoReader.getPeerIDS(i)){
                peerInfo = new PeerInfo(peerInfoReader.getPeerIDS(i), peerInfoReader.getPeerHostNames(i),
                        peerInfoReader.getPeerPorts(i), peerInfoReader.getPeerFullFileOrNot(i));
            }
        }
    }

    private void startConnections(int myID){
        for(int i = 0; i < peerInfoReader.getNumberOfPeers(); ++i){
            if(myID == peerInfoReader.getPeerIDS(i)) break;
            SocketClient temp = new SocketClient(peerInfoReader.getPeerHostNames(i),peerInfoReader.getPeerIDS(i));
            temp.start();
            socketClients.add(temp);
        }
    }

    private void startServer(int port){
        socketServer = new SocketServer(port);
    }

}
