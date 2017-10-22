package Peer;

import java.util.ArrayList;

public class BitField {

    private byte[] bitfield;
    private int numBits;

    public BitField(int numBits){
        this(numBits, false);
    }

    public BitField(int numBits, boolean hasFile){
        createNewBitField(numBits, hasFile);
    }

    private void createNewBitField(int size, boolean hasFile){
        bitfield = new byte[size];

        for(int i = 0; i < bitfield.length; ++i){
            bitfield[i] = (byte)(hasFile ? 1 : 0);
        }
    }

    public ArrayList<Integer> compareTo(byte[] bitfieldComparingTo){
        ArrayList<Integer> interestingPieces = new ArrayList<Integer>();

        for(int i = 0; i < bitfieldComparingTo.length; ++i)
            if((bitfield[i] == (byte) 0) && (bitfieldComparingTo[i] == (byte)1))
                interestingPieces.add(i);

        return interestingPieces;
    }

    public void getPiece(int index){
        bitfield[index] = (byte)1;
    }

    public Boolean isFull(){

        for(byte current: bitfield){
            if(current == (byte) 0) return false;
        }

        return true;
    }

    public byte[] getBitfield() {
        return bitfield;
    }

    public void setBitfield(byte[] bitfield) {
        this.bitfield = bitfield;
    }

    public int getNumBits() {
        return numBits;
    }

    public void setNumBits(int numBits) {
        this.numBits = numBits;
    }
}
