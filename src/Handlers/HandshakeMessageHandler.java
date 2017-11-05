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
            throw new RuntimeException();
        }

        if (!peer.getBitField().isFull()) {
            byte[] myBitFieldArray = peer.getBitField().getBitField();


            peerConnection.sendMessage(Message.createActualMessage("bitfield", myBitFieldArray));
        }

        // once the connection between the peers is established, update the isConnectionEstablished variable in PeerConnection
        peerConnection.setConnectionEstablished(true);
    }
}
