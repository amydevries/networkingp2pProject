package FileHandling;

import Peer.BitField;
import Peer.Peer;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class FileHandler {

    private BitField bitField;
    private ArrayList<Piece> pieces;
    private Integer numberOfPiecesDownloaded;

    private CommonReader commonReader = new CommonReader("Common.cfg");

    public FileHandler(){
        int fileSize = commonReader.getFileSize();
        int pieceSize = commonReader.getPieceSize();

        int numberPieces = 0;

        if (excessDataForPieces(fileSize, pieceSize))
            numberPieces = fileSize/pieceSize + 1;
        else numberPieces = fileSize/pieceSize;

        boolean fileFinshed = Peer.getPeerInfo().getFileFinished() != 0;

        bitField = new BitField(numberPieces, fileFinshed);
        
        if(fileFinshed){
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

    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
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
}
