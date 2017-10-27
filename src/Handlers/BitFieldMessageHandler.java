package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.BitField;
import Peer.Message;

import java.util.ArrayList;

public class BitFieldMessageHandler implements IHandler{

    private Peer peer;

    public BitFieldMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, Message message) {

        // update the bitfield for the peer that sent the message
        BitField messageBitField = new BitField(message.getData().length);
        messageBitField.setBitField(message.getData());

        ArrayList<Integer> interestedBits =  peer.getBitField().getInterestingBits(messageBitField);

        byte[] payload = new byte[0];

        if (interestedBits.isEmpty()) {
            peerConnection.sendMessage(Message.createActualMessage("not interested", payload));
        }
        else {
            peerConnection.sendMessage(Message.createActualMessage("interested", payload));
        }
    }
}
