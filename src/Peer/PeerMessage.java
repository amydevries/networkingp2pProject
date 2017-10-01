package Peer;

import java.io.Serializable;

public class PeerMessage implements Serializable {

    int length;
    int messageType;
    byte[] messagePayload;


}
