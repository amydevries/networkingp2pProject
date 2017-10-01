package Peer;

import Factory.SocketFactory;
import Sockets.BasicSocket;
import FileHandling.PeerInfoReader;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Peer extends Thread{

    private PeerInfo peerInfo;
    private boolean shutdown;


    public Peer(int myID){
        peerInfo = new PeerInfo(myID);

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
