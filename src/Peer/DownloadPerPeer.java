package Peer;

import java.util.Random;

public class DownloadPerPeer implements Comparable<DownloadPerPeer>{

    private int peerID;
    private int downloadAmount;
    private boolean used = false;

    public DownloadPerPeer(int peerID, int downloadAmount){
        this.peerID = peerID;
        this.downloadAmount = downloadAmount;
    }

    @Override
    public int compareTo(DownloadPerPeer downloadPerPeer) {
        if(downloadPerPeer.downloadAmount < this.downloadAmount) return -1;
        else if(downloadPerPeer.downloadAmount > this.downloadAmount) return 1;
        else{
            Random random = new Random();
            return random.nextInt(2);
        }
    }

    public int getPeerID() {
        return peerID;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getDownloadAmount(){
        return downloadAmount;
    }
}
