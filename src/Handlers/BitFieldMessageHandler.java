package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class BitFieldMessageHandler implements IHandler{

    private Peer peer;

    public BitFieldMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        // recieve a bitfield from a recently handshaken peer

        // update the bitfield for the peer that sent the message

        // if the peer that sent the message has information we dont have add the sending peer to our list of interesting peers
            // send an interested message to the peer that sent the bitfield

        // else if the peer that sent the message doesnt have any interesting pieces
            // send a not interested message


    }
}
