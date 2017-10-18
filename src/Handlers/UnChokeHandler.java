package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class UnChokeHandler implements IHandler{

    private Peer peer;

    public UnChokeHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

    }
}
