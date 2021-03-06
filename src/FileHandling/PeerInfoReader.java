package FileHandling;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

public class PeerInfoReader extends ConfigurationReader {

    private String nameOfFileReadingFrom = "PeerInfo.cfg";

    private int numberOfPeers;

    private Vector<Integer> peerIDs = new Vector<Integer>();
    private Vector<String> peerHostNames =  new Vector<String>();;
    private Vector<Integer> peerPorts =  new Vector<Integer>();;
    private Vector<Integer> peerFullFileOrNot =  new Vector<Integer>();;

    public PeerInfoReader(){
        this.parse();
    }

    public static final PeerInfoReader peerInfoReader = new PeerInfoReader("PeerInfo.cfg");

    public static PeerInfoReader getPeerInfoReader() { return peerInfoReader;}

    public PeerInfoReader(String fileName){
        nameOfFileReadingFrom = fileName;
        this.parse();
    }

    @Override
    protected String getNameOfFileReadingFrom() {
        return nameOfFileReadingFrom;
    }

    //getting values from file
    @Override
    protected void getValues(ArrayList<String> lines) {
        numberOfPeers = lines.size();
        for(int i = 0; i < lines.size(); i++){
            String currentLine = lines.get(i);
            String[] splited = currentLine.split("\\s+");
            getPeerInfo(splited);
        }
    }

    //adding to vectors based on what was read from file
    private void getPeerInfo(String[] splited){
        peerIDs.add(Integer.parseInt(splited[0]));
        peerHostNames.add(splited[1]);
        peerPorts.add(Integer.parseInt(splited[2]));
        peerFullFileOrNot.add(Integer.parseInt(splited[3]));
    }

    public int getPeerIDS(int index) {
        return peerIDs.get(index);
    }

    public String getPeerHostNames(int index) {
        return peerHostNames.get(index);
    }

    public int getPeerPorts(int index) {
        return peerPorts.get(index);
    }

    public int getPeerFullFileOrNot(int index) {
        return peerFullFileOrNot.get(index);
    }

    public int getNumberOfPeers() {
        return numberOfPeers;
    }

}
