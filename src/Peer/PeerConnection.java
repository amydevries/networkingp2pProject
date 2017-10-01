package Peer;

import Factory.SocketFactory;
import Sockets.ISocket;

import java.io.IOException;

public class PeerConnection {

    private PeerInfo peerInfo;
    private ISocket iSocket;

    public PeerConnection(PeerInfo peerInfo){
        this.peerInfo = peerInfo;
        try {
            iSocket = SocketFactory.getSocketFactory().makeSocket(peerInfo.getHostID(), peerInfo.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PeerConnection(PeerInfo peerInfo, ISocket iSocket){
        this.peerInfo = peerInfo;
        this.iSocket = iSocket;
    }

    public void sendData(byte[] bytes){
        iSocket.write(bytes);
    }

    public byte[] receiveData(){
        //return iSocket.read();
        return null;
    }
}
