package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.Message;

public class HandshakeMessageHandler implements IHandler {

    private Peer peer;

    public HandshakeMessageHandler(Peer peer){
        this.peer = peer;
    }

    @Override
    public void handleMessage(PeerConnection peerConnection, Message message) {

        // check that PeerID is matching
        if (peerConnection.getPeerInfo().getPeerID() != message.getPeerID()) {
            // TODO: throw appropritae exception

        }

        if (!peer.getBitField().isFull()) {
            byte[] myBitFieldArray = peer.getBitField().getBitField();


            // TODO: send BITFIELD message with peer.getBitField()
        }

        // once the connection between the peers is established, update the isConnectionEstablished variable in PeerConnection
        peerConnection.setConnectionEstablished(true);
    }
}
