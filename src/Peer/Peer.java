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
import Logger.PeerLogger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

public class Peer extends Thread{

    public static ExecutorService executorService;

    private static boolean programFinished = false;
    private static PeerInfo peerInfo;

    private static Hashtable<Integer, PeerInfo> peers = new Hashtable<Integer,PeerInfo>();
    public static ArrayList<PeerConnection> connections = new ArrayList<PeerConnection>();

    private PeerInfoReader peerReader = PeerInfoReader.getPeerInfoReader();
    private CommonReader commonReader = CommonReader.getCommonReader();
    private PeerLogger peerLogger;

    private static FileHandler fileHandler;

    public BitField getBitField() {
        return peerInfo.getBitField();
    }


    public Peer(int myID){
        peerInfo = new PeerInfo(myID);
        peerReader.parse();
        peers = peerInfo.getNeighborPeers(myID);

        fileHandler = new FileHandler();
        peerLogger = PeerLogger.getLogger();
    }

    public void runFileSharing(){
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("in runFileSharing");
        //loop though peers and add them to the hashtable and connect with the ones that are already in the hashtable?
        System.out.println(peerReader.getNumberOfPeers());
        for(int i = 0; i < peerReader.getNumberOfPeers(); i++){

            PeerInfo infoToAdd = new PeerInfo(peerReader.getPeerIDS(i), peerReader.getPeerHostNames(i), peerReader.getPeerPorts(i),
                    peerReader.getPeerFullFileOrNot(i));

            peers.put(peerReader.getPeerIDS(i), infoToAdd);
            System.out.println("added to hashtable: " + peerReader.getPeerIDS(i));


        }

        executorService = Executors.newCachedThreadPool();

        //listens to incoming connections
        executorService.execute(new IncomingConnections());

        for(int key: peers.keySet()){
            if(key < peerInfo.getPeerID()){
                executorService.execute(new PeerConnection(peers.get(key)));
            }
        }

        //read the delay from the config file and then pass in the peerID
        CommonReader comRead = CommonReader.getCommonReader();

        System.out.println("&&&&&&&&&&&&& unchoking interval: " + comRead.getUnchokingInterval());
        Timer unchokingIntervalTimer = new Timer();
        unchokingIntervalTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                unChokingInterval();
            }
        }, 0, comRead.getUnchokingInterval() * 1000);

        System.out.println("&&&&&&&&&&&&& optimistic unchoking interval: " + comRead.getOptimisticUnchokingInterval());
        Timer optimisticUnchokingIntervalTimer = new Timer();
        optimisticUnchokingIntervalTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                optimisticUnchokingInterval();
            }
        }, 0, comRead.getOptimisticUnchokingInterval() * 1000);

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

    public static FileHandler getFileHandler() {
        return fileHandler;
    }

    public static Hashtable<Integer, PeerInfo> getPeers() {
        return peers;
    }

    private void unChokingInterval(){
        PeerLogger peerLogger = PeerLogger.getLogger();

        System.out.println("************* peerFileHandler Full status: "+ fileHandler.isFull());
        if(fileHandler.isFull() && connections.size() > 0){

            ArrayList<Integer> interestedConnections = new ArrayList<>();
            for(int i = 0; i < connections.size(); ++i){
                if(connections.get(i).getPeerInfo().isInterested()) interestedConnections.add(connections.get(i).getPeerInfo().getPeerID());
                connections.get(i).sendChoke();
                connections.get(i).setChoked(true);
            }

            int preferredNeighbor = 0;
            Random random = new Random();
            for(int interestedConnection = 0; interestedConnection < interestedConnections.size() && preferredNeighbor < commonReader.getNumberPreferredNeighbors(); ++interestedConnection){
                int randomNeighbor= Math.abs(random.nextInt(interestedConnections.size()));
                for(int  connection = 0; connection < connections.size(); ++connection){
                    if(connections.get(connection).getPeerInfo().getPeerID() == interestedConnections.get(randomNeighbor)){
                        connections.get(connection).sendUnchoke();
                        System.out.println("^^^^^^^^^^^^^^^^^ full and sending unchoke");
                        connections.get(connection).setChoked(false);
                        preferredNeighbor++;
                        interestedConnections.remove(randomNeighbor);
                    }
                }
            }

        }
        else {

            Collections.sort(connections);
            System.out.println("collections sorted");
            System.out.println("connections.size: " + connections.size());
            for (int i = 0; i < connections.size(); i++) {
                System.out.println("***Peer id " + i + " : " + connections.get(i).getPeerInfo().getPeerID() + " interested status: " + connections.get(i).getPeerInfo().isInterested() +
                " interesting status: " + (connections.get(i).getInterestingPieces().size() > 0));
            }

            //for the top n peers, unchoke the n that have uploaded the most
            int i;
            int neighborsSendingTo = 0;
            for (i = 0; i < connections.size() && neighborsSendingTo < commonReader.getNumberPreferredNeighbors(); i++) {
                if (connections.get(i).getConnectionEstablished() && connections.get(i).getPeerInfo().isChoked() &&
                        connections.get(i).getPeerInfo().isInterested()) {
                    //unchoke these peers. only send the unchoke message if they were choked previously
                    connections.get(i).sendUnchoke();
                    System.out.println("in the unchoking loop in interval timer");
                    connections.get(i).setChoked(false);
                    neighborsSendingTo++;
                }
                else if(connections.get(i).getConnectionEstablished() && !connections.get(i).getPeerInfo().isChoked()){
                    connections.get(i).getPeerInfo().setIsChoked(true);
                    connections.get(i).sendChoke();
                }

            }

            for (; i < connections.size(); i++) {
                //choke the remaining peers
                if (connections.get(i).getConnectionEstablished() && !connections.get(i).getPeerInfo().isChoked()) {
                    //unchoke these peers
                    connections.get(i).getPeerInfo().setIsChoked(true);
                    connections.get(i).sendChoke();
                    System.out.println("in the choking loop in interval timer ");
                }
            }

            for (int k = 0; k < connections.size(); k++) {
                if (connections.get(k).getConnectionEstablished()) {
                    connections.get(k).resetPiecesReceived();
                }
            }

            int[] neighbors = new int[commonReader.getNumberPreferredNeighbors()];
            for (int k = 0; k < connections.size() && k < commonReader.getNumberPreferredNeighbors(); k++) {
                neighbors[k] = connections.get(k).getPeerInfo().getPeerID();
            }
            //setup the logger for use; need to have "true" to indicate that the file already exists
            peerLogger.changePreferredNeighbors(peerInfo.getPeerID(), neighbors);
        }
        System.out.println("%$%$%$%$%$%$% before checking shutdown");
        if(connections.size()> 0){
            programFinished = true;
            if(!peerInfo.getBitField().isFull()) programFinished = false;
            for(int k =0; k < connections.size(); k++){
                if(!connections.get(k).getPeerInfo().getBitField().isFull()){
                    programFinished = false;
                }
            }
            if(programFinished){
                System.out.println("!Program finished!");
                PeerInfoReader peerReader = PeerInfoReader.getPeerInfoReader();
                for(int k = 0; k< peerReader.getNumberOfPeers(); k ++){
                    System.out.println("checking peers from reader " + peerReader.getPeerIDS(k));
                    if(peerReader.getPeerFullFileOrNot(k) != 1 && peerReader.getPeerIDS(k) == peerInfo.getPeerID()){
                        System.out.println("Writing to non-original file");
                        fileHandler.writingFile();
                    }
                }

                executorService.shutdownNow();
                exit(0);
            }
        }
        System.out.println("finished with normal timer");
    }

    private void optimisticUnchokingInterval(){
        ArrayList<Integer> potentialConnections = new ArrayList<Integer>();
        PeerLogger peerLogger = PeerLogger.getLogger();

        for (int i = 0; i < connections.size(); i++) {
            //check to see if it is unchoked AND we are interested in it
            if (connections.get(i).getConnectionEstablished() && connections.get(i).getPeerInfo() != null && connections.get(i).isChoked() && connections.get(i).getPeerInfo().isInterested()) {
                potentialConnections.add(connections.get(i).getPeerInfo().getPeerID());
            }
        }

        Random random = new Random();
        int randomNeighborID = 0;
        if (potentialConnections.size() > 0) {

            //generate random number to select random neighbor
            int randomNeighbor= Math.abs(random.nextInt(potentialConnections.size()));
            System.out.println("^^^randomNeighbor: " + randomNeighbor);
            for(int i = 0; i < connections.size(); ++i){
                if(potentialConnections.get(randomNeighbor) == connections.get(i).getPeerInfo().getPeerID()){
                    connections.get(i).setChoked(false);
                    System.out.println("$$unchoked ");
                    connections.get(i).sendUnchoke();
                    //setup the logger for use; need to have "true" to indicate that the file already exists
                    peerLogger.changeOptimisticallyUnchockedNeighbor(peerInfo.getPeerID(), potentialConnections.get(randomNeighborID));
                }
            }

        }
        System.out.println("finished with optimistic timer");
    }

}
