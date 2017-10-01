package Peer;

import FileHandling.PeerInfoReader;

public class PeerInfo {

    private int peerID;
    private String hostID;
    private int port;
    private int fileFinished;

    public PeerInfo(int peerID, String hostID, int port, int fileFinished){
        this.peerID = peerID;
        this.hostID = hostID;
        this.port = port;
        this.fileFinished = fileFinished;
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
                break;
            }
        }
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
}
