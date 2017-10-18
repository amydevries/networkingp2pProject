package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class BitFieldHandler implements IHandler{

    private Peer peer;

    public BitFieldHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

    }
}
