package Peer;

import Factory.SocketFactory;
import Logger.PeerLogger;
import Sockets.ISocket;

import java.io.IOException;

public class PeerConnection {

    // peer connection now remembers what peer it is acting as a connection for so we can more easily access bitfield/other information
    // idk if this is necessary it might need to be removed later
    private Peer parentPeer;
    private PeerInfo peerInfo;
    private ISocket iSocket;
    private PeerLogger peerLogger = new PeerLogger();

    public PeerConnection(Peer parentPeer, PeerInfo receivingPeerInfo){
        this.parentPeer = parentPeer;
        this.peerInfo = receivingPeerInfo;
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

    public PeerConnection(Peer parentPeer, ISocket iSocket){
        this.parentPeer = parentPeer;
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

    public Peer getParentPeer() {
        return parentPeer;
    }
}
