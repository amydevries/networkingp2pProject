package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class PieceHandler implements IHandler{

    private Peer peer;

    public PieceHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

    }
}
