// were not currently reading from the peerInfo file to start connections that is one of the first things we need to work on
// also have to work on creating the data structure for the bitfield

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



// having some difficulty with having the message handlers and the initial connections to the neighbor peers
// it seems like the handler handles messages that come in from other peers and doesnt initiate the connections
// we might have to change it to handle initiating the connections or having something else that keeps track of connections that a peer starts itself
//

package Peer;

import FileHandling.CommonReader;
import FileHandling.FileHandler;
import FileHandling.PeerInfoReader;
import Handlers.*;
import Logger.PeerLogger;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.logging.Logger;

public class Peer extends Thread{

    public static Integer HANDSHAKEMESSAGE = 99;
    public static Integer CHOKEMESSAGE = 0;
    public static Integer UNCHOKEMESSAGE = 1;
    public static Integer INTERESTEDMESSAGE = 2;
    public static Integer NOTINTERESTEDMESSAGE = 3;
    public static Integer HAVEMESSAGE = 4;
    public static Integer BITFIELDMESSAGE = 5;
    public static Integer REQUESTMESSAGE = 6;
    public static Integer PIECEMESSAGE = 7;

    private static PeerInfo peerInfo;
    private boolean shutdown;

    // peers we are interested and not interested in
    public static Hashtable<Integer, PeerInfo> interestedPeers = new Hashtable<Integer, PeerInfo>();
    public static Hashtable<Integer, PeerInfo> notInterestedPeers = new Hashtable<Integer, PeerInfo>();

    private Hashtable<Integer, PeerInfo> peers = new Hashtable<Integer,PeerInfo>();
    private Hashtable<Integer, IHandler> handlers = new Hashtable<Integer, IHandler>();
    private static Hashtable<Integer, PeerConnection> connections = new Hashtable<Integer, PeerConnection>();

    private PeerInfoReader peerReader = new PeerInfoReader("PeerInfo.cfg");
    private CommonReader comReader = new CommonReader("Common.cfg");

    private PeerLogger peerLogger = new PeerLogger();

    private FileHandler fileHandler;

    public BitField getBitField() {
        return peerInfo.getBitField();
    }

    public Peer(int myID){
        peerInfo = new PeerInfo(myID);
        peerReader.parse();
        peers = peerInfo.getNeighborPeers(myID);

        fileHandler = new FileHandler();

        handlers.put(HANDSHAKEMESSAGE,new HandshakeMessageHandler(this));
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
        System.out.println("in runFileSharing");
        //loop though peers and add them to the hashtable and connect with the ones that are already in the hashtable?
        System.out.println(peerReader.getNumberOfPeers());
        for(int i = 0; i < peerReader.getNumberOfPeers(); i++){

            PeerInfo infoToAdd = new PeerInfo(peerReader.getPeerIDS(i), peerReader.getPeerHostNames(i), peerReader.getPeerPorts(i),
                    peerReader.getPeerFullFileOrNot(i), peerReader.getPeerLogs(i), peerReader.getPiecesInterested(i), peerReader.getChokedList(i));

            peers.put(peerReader.getPeerIDS(i), infoToAdd);
            System.out.println("added to hashtable: " + peerReader.getPeerIDS(i));

            //for all the peers before this one, get its neighbors and connect to them
            initiateConnections();



        }

        try{
            ServerSocket serverSocket = new ServerSocket(peerInfo.getPort());

            while(!shutdown){
                try{
                    Socket clientSocket = serverSocket.accept();

                    // the peer handler handles the connection that comes in from the server socket
                    ReceivedMessageHandler receivedMessageHandler = new ReceivedMessageHandler(this, clientSocket);
                    receivedMessageHandler.start();

                }catch (Exception e){}
            }

            serverSocket.close();

        }catch (Exception e){}

        shutdown = true;
    }

    public static PeerInfo getPeerInfo(){
        return peerInfo;
    }

    public PeerInfo getPeer(int peerID){
        for (int key : peers.keySet())
            if (peerInfo.getPeerID() == peerID)
                return peers.get(key);
        return null;
    }

    public Hashtable<Integer, IHandler> getHandlers() {
        return handlers;
    }

    public static Hashtable<Integer, PeerConnection> getConnections() {
        return connections;
    }

    public void initiateConnections(){
        // loop through all peers
        for(int key: peers.keySet()){
            // send handshake message to each of them
            Message returnMessage = sendToPeer(peers.get(key), Message.createHandshakeMessage(key));

        }
    }

    public Message sendToPeer(PeerInfo receivingPeerInfo, byte[] messageToSend){
        PeerConnection peerConnection = new PeerConnection(this, receivingPeerInfo);
        //add PeerConnection to hashtable
        connections.put(receivingPeerInfo.getPeerID(), peerConnection);
        peerConnection.sendMessage(messageToSend);
        Message reply = peerConnection.receiveData();

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        //Writes to log file: update the 1s with variables when they're known
        peerLogger.tcpConnection(peerConnection.getPeerInfo().getPeerID(), receivingPeerInfo.getPeerID());
        return reply;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

}
