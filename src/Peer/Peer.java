package Peer;

import Factory.SocketFactory;
import Handlers.*;
import Singleton.StorageSingleton;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Peer extends Thread{

    public static Integer CHOKEMESSAGE = 0;
    public static Integer UNCHOKEMESSAGE = 1;
    public static Integer INTERESTEDMESSAGE = 2;
    public static Integer NOTINTERESTEDMESSAGE = 3;
    public static Integer HAVEMESSAGE = 4;
    public static Integer BITFIELDMESSAGE = 5;
    public static Integer REQUESTMESSAGE = 6;
    public static Integer PIECEMESSAGE = 7;

    private PeerInfo peerInfo;
    private boolean shutdown;
    StorageSingleton storageSingleton = StorageSingleton.getInstance();
    private Hashtable<Integer, PeerInfo> peers = storageSingleton.getPeers();
    private Hashtable<Integer, IHandler> handlers = storageSingleton.getHandlers();


    public Peer(int myID){
        peerInfo = new PeerInfo(myID);

        peers = peerInfo.getNeighborPeers(myID);

        handlers.put(CHOKEMESSAGE,new ChokeMessageHandler(this));
        handlers.put(UNCHOKEMESSAGE,new UnChokeMessageHandler(this));
        handlers.put(INTERESTEDMESSAGE,new InterestedMessageHandler(this));
        handlers.put(NOTINTERESTEDMESSAGE, new NotInterestedMessageHandler(this));
        handlers.put(HAVEMESSAGE,new HaveMessageHandler(this));
        handlers.put(BITFIELDMESSAGE, new BitFieldMessageHandler(this));
        handlers.put(REQUESTMESSAGE, new RequestMessageHandler(this));
        handlers.put(PIECEMESSAGE, new PieceMessageHandler(this));


    }

    public void runFileSharing(){
        try{
            ServerSocket serverSocket = SocketFactory.getSocketFactory().makeServerSocket(peerInfo.getPort());

            while(!shutdown){
                try{
                    Socket clientSocket = serverSocket.accept();

                    PeerHandler peerHandler = new PeerHandler(clientSocket);
                    peerHandler.start();

                }catch (Exception e){}
            }

            serverSocket.close();

        }catch (Exception e){}

        shutdown = true;
    }

    // were not currently reading from the peerInfo file to start connections that is one of the first things we need to work on
    // read through peer info file
    // start connections with all previous peers
    // send handshake to peer
    // set peerInfo in peerConnection to the connected peer???
    // after handshake send bitfield message if the peer has some data, we could always send an empty bitfield
    // the receiving peer can then set in the peerConnection or in the peerInfo for the peerConnection what bytes the connected peer has
    // for a bitfield message if the receiving peer is missing bits that the sending peer has it sends an interested message
    // if a peer receives an "interested" message it puts the peer it received it from in a list of possible peers to send to

    // if the peer doesnt have a complete file
    // every p seconds the peer calculates the download rates for the peers in its interested section and sends data to the highest k
    // calculate download rate by keeping track of th number of bytes from each peer during the interval
    // unchoke those peers with the highest rate that are also on the interested list
    // if a neighbor is already unchoked no reason to send it an unchoked message
    // send choke message to all neighbors that missed the cut this time that were unchoked before

    // if the peer has a complete file
    // randomly select from the neighbors that are interested

    // randomly select an interested neighbor and unchoke them every m where m is the optimisticUnchokingInterval

    // when a peer finishes receiving a piece it updates its bitfield and it checking its neighbors bitfields to see if it should send them interested or not interested messages

    // when a peer is unchoked it sends a request message for a piece that it doesnt have but the peer who unchoked it has and it hasnt requested from other neighbor

    // a piece message contains the actual piece that a peer is sending to its neighbor


    public PeerInfo getPeer(int peerID){
        for (int key : peers.keySet())
            if (peerInfo.getPeerID() == peerID)
                return peers.get(key);
        return null;
    }

    public Hashtable<Integer, IHandler> getHandlers() {
        return handlers;
    }

    public PeerMessage sendToPeer(int peerID, int messageType, byte[] messageData){
        PeerInfo receivingPeerInfo = peers.get(peerID);

        return connectAndSend(receivingPeerInfo, messageType, messageData);
    }

    public PeerMessage connectAndSend(PeerInfo receivingPeerInfo, int messageType, byte[] messageData){
        PeerConnection peerConnection = new PeerConnection(receivingPeerInfo);

        PeerMessage messageToSend = new PeerMessage(messageType, messageData);

        return null;
    }

}
