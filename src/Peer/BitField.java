package Peer;

import java.util.ArrayList;

public class BitField {

    private byte[] bitField;
    private int numBits;

    public BitField(int numBits){
        this(numBits, false);
    }

    public BitField(int numBits, boolean hasFile){
        createNewBitField(numBits, hasFile);
    }

    private void createNewBitField(int size, boolean hasFile){
        bitField = new byte[size];

        for(int i = 0; i < bitField.length; ++i){
            bitField[i] = (byte)(hasFile ? 1 : 0);
        }
    }

    public ArrayList<Integer> compareTo(BitField bitFieldComparingTo){
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


    public Boolean isFull(){

        for(byte current: bitField){
            if(current == (byte) 0) return false;
        }

        return true;
    }

    public byte[] getBitField() {
        return bitField;
    }

    public void setBitField(byte[] bitField) {
            this.bitField = new byte[bitField.length * 8];

            int k = 0;
            for (int i = 0; i < bitField.length; i++) {
                for (int mask = 0x80; mask > 0; mask = mask >> 1) {
                    this.bitField[k] = (byte) (((bitField[i] & mask) != 0) ? 1:0);
                }
            }
    }

    public int length() {
        return numBits;
    }

    public void setNumBits(int numBits) {
        this.numBits = numBits;
    }
}
