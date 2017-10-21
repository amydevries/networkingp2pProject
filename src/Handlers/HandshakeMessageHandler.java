package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class HandshakeMessageHandler implements IHandler {

    private Peer peer;

    public HandshakeMessageHandler(Peer peer){
        this.peer = peer;
    }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {


        //once the connection between the peers is established, update the isConnectionEstablished variable in PeerConnection
        peerConnection.setConnectionEstablished(true);
    }
}