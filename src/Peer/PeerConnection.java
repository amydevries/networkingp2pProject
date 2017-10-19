package Peer;

import Factory.SocketFactory;
import Logger.PeerLogger;
import Sockets.ISocket;

import java.io.IOException;

public class PeerConnection {

    private PeerInfo peerInfo;
    private ISocket iSocket;
    private PeerLogger peerLogger = new PeerLogger();

    public PeerConnection(PeerInfo peerInfo){
        this.peerInfo = peerInfo;
        try {
            iSocket = SocketFactory.getSocketFactory().makeSocket(peerInfo.getHostID(), peerInfo.getPort());

            //not sure if this is where the logger should go for the TCP connection
            //setup the logger for use; need to have "true" to indicate that the file already exists
            peerLogger.setup(peerInfo.getPeerID(), true);
            //Writes to log file: update the 1s with variables when they're known
            peerLogger.tcpConnection(1, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PeerConnection(PeerInfo peerInfo, ISocket iSocket){
        this.peerInfo = peerInfo;
        this.iSocket = iSocket;
    }

    public void close(){
        if(iSocket != null){
            iSocket.close();
        }
        iSocket = null;
    }

    public void sendData(byte[] bytes){
        iSocket.write(bytes);
    }

    public PeerMessage receiveData(){
        PeerMessage msg = null;
        try {
            msg = new PeerMessage(iSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public PeerInfo getPeerInfo() { return peerInfo; }
}
