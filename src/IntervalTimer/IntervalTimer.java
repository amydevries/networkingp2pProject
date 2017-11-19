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
public class IntervalTimer implements Runnable {

    private int delay;
    private static int peerID;
    private static boolean programFinished = false;

    public IntervalTimer(int delay, int peerID) {
        this.delay = delay;
        this.peerID = peerID;
    }

    public void run() {
        try {
            Thread.sleep(delay);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void unchokingIntervalTimerStart() {
        ArrayList<PeerConnection> connections = new ArrayList<PeerConnection>();

        CommonReader comReader = CommonReader.getCommonReader();
        int numPrefNeighbors = comReader.getNumberPreferredNeighbors();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    public void run() {
                        if (programFinished) {
                            this.cancel();
                        }

                        Collections.sort(connections);

                        //for the top n peers, unchoke the n that have uploaded the most
                        int i;
                        for (i = 0; i < connections.size() && i < numPrefNeighbors; i++) {
                            if (connections.get(i).getConnectionEstablished() && connections.get(i).getPeerInfo().isChoked() &&
                                    connections.get(i).getPeerInfo().isInterested()) {
                                //unchoke these peers. only send the unchoke message if they were choked previously
                                connections.get(i).sendMessage(Message.createActualMessage("unchoke", new byte[0]));
                            }

                        }

                        for (; i < connections.size(); i++) {
                            //choke the remaining peers
                            if (connections.get(i).getConnectionEstablished() && !connections.get(i).getPeerInfo().isChoked()) {
                                //unchoke these peers
                                connections.get(i).getPeerInfo().setIsChoked(true);
                                connections.get(i).sendMessage(Message.createActualMessage("choke", new byte[0]));
                            }
                        }

                        for (int k = 0; k < connections.size(); k++) {
                            if (connections.get(k).getConnectionEstablished()) {
                                connections.get(k).resetPiecesReceived();
                            }
                        }

                        PeerLogger peerLogger = new PeerLogger();
                        int[] neighbors = new int[comReader.getNumberPreferredNeighbors()];
                        for (int k = 0; k<connections.size() && k < comReader.getNumberPreferredNeighbors(); k++) {
                            neighbors[k] = connections.get(k).getPeerInfo().getPeerID();
                        }
                        //setup the logger for use; need to have "true" to indicate that the file already exists
                        peerLogger.setup(peerID, true);
                        peerLogger.changePreferredNeighbors(peerID, neighbors);

                        if(Peer.connections.size()> 0){
                            programFinished = true;
                            for(int k =0; k < connections.size(); k++){
                                if(!connections.get(k).getPeerInfo().getBitField().isFull()){
                                    programFinished = false;
                                }
                            }
                            if(programFinished){
                                Peer.executorService.shutdownNow();
                            }
                        }


                    }
                }
                , 0, comReader.getUnchokingInterval() * 1000);


    }

    public static void optimisticTimerStart() {
        //select a random neighbor that is choked currently but is one that we're interested in

        //keep an array list of only the neighbors that are choked
        ArrayList<Integer> potentialConnections = new ArrayList<Integer>();

        CommonReader comReader = CommonReader.getCommonReader();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    public void run() {
                        if (programFinished) {
                            this.cancel();
                        }

                        for (int i = 0; i < Peer.connections.size(); i++) {
                            //check to see if it is unchoked AND we are interested in it
                            if (Peer.connections.get(i).getPeerInfo() != null && Peer.connections.get(i).isChoked() && Peer.connections.get(i).getInterestingPieces().size() > 0) {
                                potentialConnections.add(i);
                            }
                        }

                        Random random = new Random();
                        int randomNeighborID = 0;
                        if (potentialConnections.size() > 0) {
                            //generate random number to select random neighbor
                            int randomNeighbor = Math.abs(random.nextInt()) % potentialConnections.size();
                            Peer.connections.get(randomNeighbor).setChoked(false);
                            Peer.connections.get(randomNeighbor).sendUnchoke();

                            PeerLogger peerLogger = new PeerLogger();
                            //setup the logger for use; need to have "true" to indicate that the file already exists
                            peerLogger.setup(peerID, true);
                            peerLogger.changeOptimisticallyUnchockedNeighbor(peerID, randomNeighborID);
                        }
                    }

                }
                , 0, comReader.getOptimisticUnchokingInterval() * 1000);

    }
}
