package IntervalTimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import FileHandling.CommonReader;
import Peer.PeerConnection;
import Peer.Peer;

/**
 * for controlling the timing for chocking and un-chocking
 */
public class IntervalTimer extends Thread{

    private int delay;

    public IntervalTimer(int delay){
        this.delay = delay;
    }

    public void run(){
        try {
            Thread.sleep(delay);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void timerStart(){
        ArrayList<PeerConnection> connections = new ArrayList<PeerConnection>();
        Hashtable<Integer, PeerConnection> connectionHashtable = Peer.getConnections();
        for(int key: connectionHashtable.keySet()){
            connections.add(connectionHashtable.get(key));
        }
        Collections.sort(connections);

        CommonReader comReader = new CommonReader();
        int numPrefNeighbors = comReader.getNumberPreferredNeighbors();

        //for the top n peers, unchoke the n that have uploaded the most
       /* for(int i=0; i < connections.size() && i < numPrefNeighbors; i++){
            if(connections.get(i).getConnectionEstablished() && connections.get(i).getPeerInfo().isChoked() &&
                    connections.get(i).getPeerInfo()){

            }
        }*/
    }
}
