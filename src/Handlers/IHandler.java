package Handlers;

import Peer.PeerConnection;
import Peer.Message;

public interface IHandler {

    public void handleMessage(PeerConnection peerConnection, Message message);
}
