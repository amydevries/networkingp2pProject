package Handlers;

import Peer.PeerConnection;
import Peer.PeerMessage;

public interface IHandler {

    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage);
}
