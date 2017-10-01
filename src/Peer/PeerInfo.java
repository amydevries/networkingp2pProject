package Peer;

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
