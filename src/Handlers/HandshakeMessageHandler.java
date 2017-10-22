package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;
import java.util.Arrays;
import java.util.BitSet;

public class HandshakeMessageHandler implements IHandler {

    private Peer peer;

    public HandshakeMessageHandler(Peer peer){
        this.peer = peer;
    }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        // check that PeerID is matching
        if (peerConnection.getPeerInfo().getPeerID() != peerMessage.getPeerID()) {
            // TODO: throw appropritae exception

        }

        if (!peer.getBitField().isEmpty()) {
            byte[] myBitFieldArray = peer.getBitField().toByteArray();


            // TODO: send BITFIELD message with peer.getBitField()
        }

        // once the connection between the peers is established, update the isConnectionEstablished variable in PeerConnection
        peerConnection.setConnectionEstablished(true);
    }
}
