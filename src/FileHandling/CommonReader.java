package FileHandling;

import java.util.ArrayList;

public class CommonReader extends ConfigurationReader {

    private int numberPreferredNeighbors;
    private int unchokingInterval;
    private int optimisticUnchokingInterval;
    private String fileName;
    private int fileSize;
    private int pieceSize;

    public static final CommonReader commonReader = new CommonReader("Common.cfg");

    public static CommonReader getCommonReader() { return commonReader;}

    private String nameOfFileReadingFrom = "Common.cfg";

    public CommonReader(){}

    public CommonReader(String fileName){
        nameOfFileReadingFrom = fileName;
        this.parse();
    }

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
        if(splited[0].equals("NumberOfPreferredNeighbors")) numberPreferredNeighbors = Integer.parseInt(splited[1]);
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

    public int getNumberPreferredNeighbors() {
        return numberPreferredNeighbors;
    }

    public int getUnchokingInterval() {
        return unchokingInterval;
    }

    public int getOptimisticUnchokingInterval() {
        return optimisticUnchokingInterval;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getPieceSize() {
        return pieceSize;
    }
}
