package Peer;

import FileHandling.PeerInfoReader;
import Logger.PeerLogger;
import Peer.Peer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Logger;

public class PeerInfo {

    private int peerID;
    private String hostID;
    private int port;
    private int fileFinished;
    private PeerLogger peerLogger = new PeerLogger();
    private File peerLog;

    public PeerInfo(int peerID, String hostID, int port, int fileFinished, File peerLog){
        this.peerID = peerID;
        this.hostID = hostID;
        this.port = port;
        this.fileFinished = fileFinished;
        this.peerLog = peerLog;
    }

    public PeerInfo(int myID){
        PeerInfoReader peerInfoReader = new PeerInfoReader();
        peerInfoReader.parse();

        for(int i = 0; i < peerInfoReader.getNumberOfPeers(); ++i){
            if(myID == peerInfoReader.getPeerIDS(i)){
                this.peerID = peerInfoReader.getPeerIDS(i);
                this.hostID = peerInfoReader.getPeerHostNames(i);
                this.port = peerInfoReader.getPeerPorts(i);
                this.fileFinished = peerInfoReader.getPeerFullFileOrNot(i);
                //setup the file writer for the peer and create the log file
                peerLogger.setup(this.peerID);
                this.peerLog = new File("log_peer_" + this.peerID + ".log");
            }
        }
    }

    public Hashtable<Integer,PeerInfo> getNeighborPeers(int myID){
        PeerInfoReader peerInfoReader = new PeerInfoReader();
        peerInfoReader.parse();
        Hashtable<Integer,PeerInfo> neighbors = new Hashtable<Integer, PeerInfo>();

        for(int i = 0; i < peerInfoReader.getNumberOfPeers(); ++i){
            if(myID == peerInfoReader.getPeerIDS(i)) break;
            if(myID != peerInfoReader.getPeerIDS(i)){
                PeerInfo peerInfo = new PeerInfo(peerInfoReader.getPeerIDS(i), peerInfoReader.getPeerHostNames(i)
                        , peerInfoReader.getPeerPorts(i), peerInfoReader.getPeerFullFileOrNot(i), new File("log_peer_" + peerInfoReader.getPeerIDS(i) + ".log"));
                neighbors.put(peerInfoReader.getPeerIDS(i), peerInfo);
            }
        }
        return neighbors;
    }

    public int getPeerID() {
        return peerID;
    }

    public void setPeerID(int peerID) {
        this.peerID = peerID;
    }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getFileFinished() {
        return fileFinished;
    }

    public void setFileFinished(int fileFinished) {
        this.fileFinished = fileFinished;
    }

    public File getPeerLog() { return peerLog; }

    public void setPeerLog() { this.peerLog = peerLog; }
}
