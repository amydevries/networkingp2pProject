package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class RequestHandler implements IHandler{

    private Peer peer;

    public RequestHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

    }
}
