package Peer;

import FileHandling.PeerInfoReader;
import Logger.PeerLogger;
import Peer.Peer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;

public class PeerInfo {

    private int peerID;
    private String hostID;
    private int port;
    private int fileFinished;
    private boolean isChoked;          //variable to determine if the one peer in the connection has choked the other (true == choked)

    BitField bitField;
    private PeerLogger peerLogger = new PeerLogger();
    private File peerLog;

    private ArrayList<Integer> piecesInterestedIn;   //stores the pieces that the peer is interested in

    public BitField getBitField() {
        return bitField;
    }

    public void setBitField(BitField bitField) {
        this.bitField = bitField;
    }

    public PeerInfo(int peerID, String hostID, int port, int fileFinished, File peerLog, ArrayList<Integer> piecesInterestedIn, boolean isChoked){
        this.peerID = peerID;
        this.hostID = hostID;
        this.port = port;
        this.fileFinished = fileFinished;
        this.peerLog = peerLog;
        this.piecesInterestedIn = piecesInterestedIn;
        this.isChoked = isChoked;
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
                this.piecesInterestedIn = peerInfoReader.getPiecesInterested(i);
                this.isChoked = peerInfoReader.getChokedList(i);
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
                        , peerInfoReader.getPeerPorts(i), peerInfoReader.getPeerFullFileOrNot(i),
                        new File("log_peer_" + peerInfoReader.getPeerIDS(i) + ".log"), peerInfoReader.getPiecesInterested(i), peerInfoReader.getChokedList(i));
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

    public ArrayList<Integer> getPiecesInterestedIn() { return piecesInterestedIn; }

    public void setPiecesInterestedIn(){ this.piecesInterestedIn = piecesInterestedIn; }

    public boolean isChoked(){
        synchronized (this){
            return isChoked;
        }
    }

    public void setIsChoked(boolean isChoked){ this.isChoked = isChoked; }

}
