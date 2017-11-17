package Peer;

import java.util.ArrayList;

public class BitField {

    private byte[] bitField;
    private int numBits;

    public BitField(int numBits){
        this(numBits, false);
    }

    public BitField(int numBits, boolean fileCompete){
        this.numBits = numBits;
        bitField = new byte[numBits];

        for(int i = 0; i < bitField.length; ++i){
            bitField[i] = (byte)(fileCompete ? 1 : 0);
        }
    }

    public ArrayList<Integer> getInterestingBits(BitField bitFieldComparingTo){
        ArrayList<Integer> interestingPieces = new ArrayList<Integer>();

        for(int i = 0; i < bitFieldComparingTo.length(); ++i)
            if((bitField[i] == (byte) 0) && (bitFieldComparingTo.getPiece(i) == (byte)1))
                interestingPieces.add(i);

        return interestingPieces;
    }

    public byte getPiece(int index){
        return bitField[index];
    }

    public void setPiece(int index){
        bitField[index] = (byte)1;
    }

    public int getNumberOfPieces(){
        int numPieces = 0;
        for(int i = 0 ; i < bitField.length; ++i){
            if(bitField[i] == (byte)1) numPieces++;
        }

        return numPieces;
    }

    public Boolean isFull(){

        for(byte current: bitField){
            if(current == (byte) 0) return false;
        }

        return true;
    }

    public byte[] getBitField() {
        synchronized (this) {
            return bitField;
        }
    }

    public void setBitField(byte[] bitField) {
        this.bitField = bitField;
    }

    public int length() {
        return numBits;
    }

    public void setNumBits(int numBits) {
        this.numBits = numBits;
    }
}
