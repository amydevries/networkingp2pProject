package FileHandling;

import java.util.ArrayList;

public class CommonReader extends ConfigurationReader {

    public int numberPreferedNeighbors;
    public int unchokingInterval;
    public int optimisticUnchokingInterval;
    public String fileName;
    public int fileSize;
    public int pieceSize;

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
            checkAndSetVariables(splited);
        }
    }

    private void checkAndSetVariables(String[] splited){
        checkAndSetNumberPreferedNeighbors(splited);
        checkAndSetUnchokingInterval(splited);
        checkAndSetOptimisticUnchokingInterval(splited);
        checkAndSetFileName(splited);
        checkAndSetFileSize(splited);
        checkAndSetPieceSize(splited);
    }

    private void checkAndSetNumberPreferedNeighbors(String[] splited){
        if(splited[0].equals("NumberOfPreferredNeighbors")) numberPreferedNeighbors = Integer.parseInt(splited[1]);
    }

    private void checkAndSetUnchokingInterval(String[] splited){
        if(splited[0].equals("UnchokingInterval")) unchokingInterval = Integer.parseInt(splited[1]);
    }

    private void checkAndSetOptimisticUnchokingInterval(String[] splited){
        if(splited[0].equals("OptimisticUnchokingInterval")) optimisticUnchokingInterval = Integer.parseInt(splited[1]);
    }

    private void checkAndSetFileName(String[] splited){
        if(splited[0].equals("FileName")) fileName = splited[1];
    }

    private void checkAndSetFileSize(String[] splited){
        if(splited[0].equals("FileSize")) fileSize = Integer.parseInt(splited[1]);
    }

    private void checkAndSetPieceSize(String[] splited){
        if(splited[0].equals("PieceSize")) pieceSize = Integer.parseInt(splited[1]);
    }
}
