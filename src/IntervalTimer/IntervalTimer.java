package IntervalTimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;

import FileHandling.CommonReader;
import Logger.PeerLogger;
import Peer.PeerConnection;
import Peer.Peer;
import Peer.Message;

/**
 * for controlling the timing for chocking and un-chocking
 */
public class IntervalTimer extends Thread{

    private int delay;
    private static int peerID;

    public IntervalTimer(int delay, int peerID){
        this.delay = delay;
        this.peerID = peerID;
    }

    public void run(){
        try {
            Thread.sleep(delay);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void unchokingIntervalTimerStart(){
        ArrayList<PeerConnection> connections = new ArrayList<PeerConnection>();
        Hashtable<Integer, PeerConnection> connectionHashtable = Peer.getConnections();
        for(int key: connectionHashtable.keySet()){
            connections.add(connectionHashtable.get(key));
        }
        Collections.sort(connections);

        CommonReader comReader = new CommonReader();
        int numPrefNeighbors = comReader.getNumberPreferredNeighbors();

        //for the top n peers, unchoke the n that have uploaded the most
        int i;
        for(i=0; i < connections.size() && i < numPrefNeighbors; i++){
            if(connections.get(i).getConnectionEstablished() && connections.get(i).getPeerInfo().isChoked() &&
                    Peer.interestedPeers.containsKey(connections.get(i).getPeerInfo().getPeerID())){
                //unchoke these peers. only send the unchoke message if they were choked previously
                if(connections.get(i).getPeerInfo().isChoked()){
                    connections.get(i).getPeerInfo().setIsChoked(false);
                    connections.get(i).sendMessage(Message.createActualMessage("unchoke", new byte[0]));
                }
            }
        }

        for(; i < connections.size(); i++){
           //choke the remaining peers
            if(connections.get(i).getConnectionEstablished() && !connections.get(i).getPeerInfo().isChoked() ){
                //unchoke these peers
                connections.get(i).getPeerInfo().setIsChoked(true);
                connections.get(i).sendMessage(Message.createActualMessage("choke", new byte[0]));
            }
        }

        for(int k = 0; k < connections.size(); k++){
            if(connections.get(k).getConnectionEstablished()){
                connections.get(k).resetPiecesReceived();
            }
        }

        PeerLogger peerLogger = new PeerLogger();
        int[] neighbors = new int[connections.size()];
        for(int k = 0; k < connections.size(); k++){
            neighbors[k]= connections.get(k).getPeerInfo().getPeerID();
        }
        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerID, true);
        peerLogger.changePreferredNeighbors(peerID, neighbors);
    }

    public static void optimisticTimerStart(){
        //select a random neighbor that is choked currently but is one that we're interested in

        //keep an array list of only the neighbors that are choked
        ArrayList<PeerConnection> chokedConnections = new ArrayList<PeerConnection>();
        Hashtable<Integer, PeerConnection> connectionHashtable = Peer.getConnections();
        for(int key: connectionHashtable.keySet()){
            //check to see if it is unchoked AND we are interested in it
            if(connectionHashtable.get(key).isChoked() && Peer.interestedPeers.containsKey(chokedConnections.get(key).getPeerInfo().getPeerID())) {
                chokedConnections.add(connectionHashtable.get(key));
            }
        }

        Random random = new Random();
        int randomNeighborID = 0;
        if(chokedConnections.size() > 0){
            //generate random number to select random neighbor
            int randomNeighbor = Math.abs(random.nextInt()) % chokedConnections.size();
            randomNeighborID = chokedConnections.get(randomNeighbor).getPeerInfo().getPeerID();

            //send unchoking message
            chokedConnections.get(randomNeighborID).getPeerInfo().setIsChoked(false);
            chokedConnections.get(randomNeighborID).sendMessage(Message.createActualMessage("unchoke", new byte[0]));

            PeerLogger peerLogger = new PeerLogger();
            //setup the logger for use; need to have "true" to indicate that the file already exists
            peerLogger.setup(peerID, true);
            peerLogger.changeOptimisticallyUnchockedNeighbor(peerID, randomNeighborID);
        }
    }
}
