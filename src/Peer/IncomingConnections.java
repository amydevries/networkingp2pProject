package Peer;

import DefaultProcesses.peerProcess;
import FileHandling.PeerInfoReader;
import Sockets.BasicSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IncomingConnections implements Runnable {

    @Override
    public void run() {

        ServerSocket serverSocket = null;

        try{
            serverSocket = new ServerSocket(Peer.getPeerInfo().getPort());
            for(;;){
                Socket socket = serverSocket.accept();
                BasicSocket basicSocket = new BasicSocket(socket);
                Peer.executorService.execute(new PeerConnection(basicSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
