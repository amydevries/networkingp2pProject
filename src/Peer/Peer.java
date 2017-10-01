package Peer;

import FileHandling.PeerInfoReader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Peer {

    private PeerInfo peerInfo;
    private PeerInfoReader peerInfoReader;
    public Peer(int peerID){
        peerInfoReader = new PeerInfoReader();
        peerInfoReader.parse();

        getPeerInfo(peerID);
        startConnections(peerID);
    }

    private void getPeerInfo(int peerID){
        for(int i = 0; i < peerInfoReader.getNumberOfPeers(); ++i){
            if(peerID == peerInfoReader.getPeerIDS(i)){
                peerInfo = new PeerInfo(peerInfoReader.getPeerIDS(i), peerInfoReader.getPeerHostNames(i),
                        peerInfoReader.getPeerPorts(i), peerInfoReader.getPeerFullFileOrNot(i));
            }
        }
    }

    private void startConnections(int peerID){
        for(int i = 0; i < peerInfoReader.getNumberOfPeers(); ++i){
            if(peerID == peerInfoReader.getPeerIDS(i)) break;
            new Handler(peerInfoReader.getPeerHostNames(i),peerInfoReader.getPeerIDS(i)).start();
        }
    }

    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for dealing with a single client's requests.
     */
    private static class Handler extends Thread {
        String hostName;
        int port;

        public Handler(String hostName, int port) {
            this.hostName = hostName;
            this.port = port;
        }



    }
}
