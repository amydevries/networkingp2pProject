package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class RequestMessageHandler implements IHandler{

    private Peer peer;

    public RequestMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        // recieved a request message form an unchoked peer
        // double check to make sure the peer really is unchoked

        // if the peer is unchoked start transmitting the piece it asked for

        // else if it isnt unchoked then just ignore it

    }
}
