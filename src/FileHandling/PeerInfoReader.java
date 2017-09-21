package FileHandling;

import java.util.ArrayList;

// TODO: Im unsure how the peerinfo file works and how were gonna set up peers so i havent done a lot with this

public class PeerInfoReader extends ConfigurationReader {

    private String nameOfFileReadingFrom = "Common.cfg";

    @Override
    protected String getNameOfFileReadingFrom() {
        return nameOfFileReadingFrom;
    }

    @Override
    protected void getValues(ArrayList<String> lines) {
        for(int i = 0; i < lines.size(); i++){
            String currentLine = lines.get(i);
            String[] splited = currentLine.split("\\s+");
            getPeerInfo(splited);
        }
    }

    private void getPeerInfo(String[] splited){

    }
}
