package FileHandling;

import DefaultProcesses.peerProcess;
import Logger.PeerLogger;
import Peer.BitField;
import Peer.Peer;
import org.apache.commons.io.IOUtils;
import Logger.PeerLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class FileHandler {

    private int numberOfPiecesDownloaded = 0;
    private BitField bitField;
    private ArrayList<Piece> pieces;
    private PeerLogger peerLogger = PeerLogger.getLogger();

    private CommonReader commonReader = CommonReader.getCommonReader();

    public FileHandler(){
        int fileSize = commonReader.getFileSize();
        int pieceSize = commonReader.getPieceSize();

        int numberPieces = 0;

        if (excessDataForPieces(fileSize, pieceSize))
            numberPieces = fileSize/pieceSize + 1;
        else numberPieces = fileSize/pieceSize;

        boolean fileFinished = Peer.getPeerInfo().getFileFinished() != 0;

        if(fileFinished){
            File src = new File(commonReader.getFileName());
            File dst = new File("./peer_"+Peer.getPeerInfo().getPeerID()+"/"+commonReader.getFileName());
            try {
                copyFile(src, dst);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bitField = new BitField(numberPieces, fileFinished);
        
        if(fileFinished){
            pieces = new ArrayList<Piece>();
            readFromFile(numberPieces);
        }
        else {
            pieces = new ArrayList<Piece>();

            for(int i = 0; i < numberPieces; ++i) pieces.add(new Piece());
        }
    }

    private boolean excessDataForPieces(int fileSize, int pieceSize){
        return (fileSize % pieceSize !=0 ? true:false);
    }

    private void readFromFile(int numberPieces) {
        FileInputStream fileInputStream = null;

        byte[] data = new byte[0];

        try{
            fileInputStream = new FileInputStream(new File("./peer_"+Peer.getPeerInfo().getPeerID()+"/"+commonReader.getFileName()));
            data = new byte[commonReader.getFileSize()];
            fileInputStream.read(data);
        }catch (Exception e){ e.printStackTrace(); }
        finally {
            try {
                fileInputStream.close();
            }catch (Exception e) { e.printStackTrace(); }
        }

        for(int i = 0; i < numberPieces; ++i){
            byte[] dataToAdd = new byte[commonReader.getPieceSize()];

            for(int j = 0; j < commonReader.getPieceSize(); ++j){
                int pieceOffSet = i * dataToAdd.length;
                pieceOffSet += j;
                if(pieceOffSet >= data.length) dataToAdd[j] = (byte)0;
                else dataToAdd[j] = data[pieceOffSet];
            }

            pieces.add(new Piece(dataToAdd));
        }
    }

    public synchronized void receive(int index, byte[]data){
        synchronized (pieces.get(index)){
            if(!pieces.get(index).isFull()){
                pieces.get(index).setData(data);
                bitField.setPiece(index);
            }
        }
    }

    public void writingFile(){
        int numberOfPieces = pieces.size();
        int totalFileSize = numberOfPieces * commonReader.getPieceSize();

        byte[] finishedFile = new byte[totalFileSize];
        int currentLocation = 0;
        for(int i = 0; i < pieces.size(); i++){
            byte[] temp = pieces.get(i).getData();
            for(int j = 0; j < temp.length; j++){
                finishedFile[currentLocation] = temp[j];
                currentLocation++;
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./peer_"+Peer.getPeerInfo().getPeerID()+"/"+commonReader.getFileName());
            fileOutputStream.write(finishedFile);
            fileOutputStream.close();
            peerLogger.completedDownload(Peer.getPeerInfo().getPeerID());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(File src, File dst) throws IOException {
        Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    public ArrayList<Piece> getPieces() {
    return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    public void setPiece(int index, Piece piece){
        this.pieces.set(index, piece);
    }

    public BitField getBitField() {
        return bitField;
    }

    public void setBitField(BitField bitField) {
        this.bitField = bitField;
    }

    public Piece getPiece(int index){
        return pieces.get(index);
    }

    public void increaseNumberOfPiecesDownloaded(){
        numberOfPiecesDownloaded++;
    }

    public int getNumberOfPiecesDownloaded(){
        return numberOfPiecesDownloaded;
    }

    public boolean isFull(){
        return bitField.isFull();
    }
}
