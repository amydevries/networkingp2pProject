package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;
import java.util.BitSet;
import java.util.Arrays;
import java.util.BitSet;

public class BitFieldMessageHandler implements IHandler{

    private Peer peer;

    public BitFieldMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        // update the bitfield for the peer that sent the message
        BitSet peerBits = BitSet.valueOf(peerMessage.getData());

        peerConnection.getPeerInfo().setBitField(peerBits);

        BitSet interestedBits = (BitSet) peerBits.clone();

        interestedBits.andNot(peer.getBitField());

        byte[] payload = new byte[0];

        if (interestedBits.isEmpty()) {
            peerConnection.sendData(PeerMessage.createActualMessage("not interested", payload));
        }
        else {
            peerConnection.sendData(PeerMessage.createActualMessage("interested", payload));
        }
    }
}
