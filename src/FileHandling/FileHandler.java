package FileHandling;

import DefaultProcesses.peerProcess;
import Peer.BitField;
import Peer.Peer;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class FileHandler {

    private int numberOfPiecesDownloaded = 0;
    private BitField bitField;
    private ArrayList<Piece> pieces;

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
            fileInputStream = new FileInputStream(new File(commonReader.getFileName()));
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

    public void receive(int index, byte[]data){
        synchronized (pieces.get(index)){
            pieces.get(index).setData(data);
            bitField.setPiece(index);
            Peer.getPeerInfo().getBitField().setPiece(index);
            for(int i=0; i<Peer.connections.size();i++){
                if(Peer.connections.get(i).getConnectionEstablished()) {
                    Peer.connections.get(i).sendHave(index);
                    synchronized (Peer.connections.get(i).getInterestingPieces()) {
                        Peer.connections.get(i).getInterestingPieces().remove(new Integer(index));
                    }
                }
            }
        }
    }

    public void writingFile(){
        byte[] finishedFile = new byte[commonReader.getFileSize()];
        for(int i = 0; i < pieces.size(); i++){
            byte[] temp = pieces.get(i).getData();
            for(int j = 0; j < temp.length; j++){
                finishedFile[i+j] = temp[j];
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./peer_"+Peer.getPeerInfo().getPeerID()+"/"+commonReader.getFileName());
            fileOutputStream.write(finishedFile);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(File src, File dst) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dst);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
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
