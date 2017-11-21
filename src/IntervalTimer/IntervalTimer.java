package IntervalTimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;

import FileHandling.CommonReader;
import FileHandling.FileHandler;
import FileHandling.PeerInfoReader;
import Logger.PeerLogger;
import Peer.PeerConnection;
import Peer.Peer;
import Peer.Message;

import static java.lang.System.exit;

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
        System.out.println("Entering unchoking timer");

        CommonReader comReader = CommonReader.getCommonReader();
        int numPrefNeighbors = comReader.getNumberPreferredNeighbors();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    public void run() {
                        if (programFinished) {
                            this.cancel();
                        }

                        Collections.sort(Peer.connections);
                        System.out.println("collections sorted");
                        System.out.println("connections.size: " + Peer.connections.size());
                        for(int i = 0; i < Peer.connections.size(); i++){
                            System.out.println("***Peer id " + i + " : " + Peer.connections.get(i).getPeerInfo().getPeerID());
                        }

                        //for the top n peers, unchoke the n that have uploaded the most
                        int i;
                        for (i = 0; i < Peer.connections.size() && i < numPrefNeighbors; i++) {
                            if (Peer.connections.get(i).getConnectionEstablished() && Peer.connections.get(i).getPeerInfo().isChoked() &&
                                    Peer.connections.get(i).getPeerInfo().isInterested()) {
                                //unchoke these peers. only send the unchoke message if they were choked previously
                                Peer.connections.get(i).sendUnchoke();
                                System.out.println("in the unchoking loop in interval timer");
                                Peer.connections.get(i).setChoked(false);
                            }

                        }

                        for (; i < Peer.connections.size(); i++) {
                            //choke the remaining peers
                            if (Peer.connections.get(i).getConnectionEstablished() && !Peer.connections.get(i).getPeerInfo().isChoked()) {
                                //unchoke these peers
                                Peer.connections.get(i).getPeerInfo().setIsChoked(true);
                                Peer.connections.get(i).sendChoke();
                                System.out.println("in the choking loop in interval timer ");
                            }
                        }

                        for (int k = 0; k < Peer.connections.size(); k++) {
                            if (Peer.connections.get(k).getConnectionEstablished()) {
                                Peer.connections.get(k).resetPiecesReceived();
                            }
                        }

                        PeerLogger peerLogger = new PeerLogger();
                        int[] neighbors = new int[comReader.getNumberPreferredNeighbors()];
                        for (int k = 0; k < Peer.connections.size() && k < comReader.getNumberPreferredNeighbors(); k++) {
                            neighbors[k] = Peer.connections.get(k).getPeerInfo().getPeerID();
                        }
                        //setup the logger for use; need to have "true" to indicate that the file already exists
                        peerLogger.setup(peerID, true);
                        peerLogger.changePreferredNeighbors(peerID, neighbors);

                        if(Peer.connections.size()> 0){
                            programFinished = true;
                            if(!Peer.getPeerInfo().getBitField().isFull()) programFinished = false;
                            for(int k =0; k < Peer.connections.size(); k++){
                                if(!Peer.connections.get(k).getPeerInfo().getBitField().isFull()){
                                    programFinished = false;
                                }
                            }
                            if(programFinished){
                                System.out.println("!Program finished!");
                                PeerInfoReader peerReader = PeerInfoReader.getPeerInfoReader();
                                for(int k = 0; k< peerReader.getNumberOfPeers(); k ++){
                                    System.out.println("checking peers from reader " + peerReader.getPeerIDS(k));
                                    if(peerReader.getPeerFullFileOrNot(k) != 1){
                                        System.out.println("Writing to non-original file");
                                        Peer.getFileHandler().writingFile();
                                    }
                                }

                                Peer.executorService.shutdownNow();
                                exit(0);
                            }
                        }


                    }
                }
                , 0, comReader.getUnchokingInterval() * 1000);


    }

    public static void optimisticTimerStart() {
        //select a random neighbor that is choked currently but is one that we're interested in


        System.out.println("Entering optimistic timer");
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
                            System.out.println("$$unchoked ");
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
