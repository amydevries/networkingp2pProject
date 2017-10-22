package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;
import Peer.BitField;

import java.util.ArrayList;

public class BitFieldMessageHandler implements IHandler{

    private Peer peer;

    public BitFieldMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        // update the bitfield for the peer that sent the message
        BitField messageBitField = new BitField(peerMessage.getData().length);
        messageBitField.setBitField(peerMessage.getData());

        ArrayList<Integer> interestedBits =  peer.getBitField().compareTo(messageBitField);

        if (interestedBits.isEmpty())
            // TODO: send NOTINTERESTED meessage
            ;
        else
            // TODO: send INTERESTED messsage
            ;
    }
}
