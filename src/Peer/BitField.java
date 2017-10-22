package Peer;
import java.util.BitSet;

public class BitField {

    private BitSet bits;


    public BitField(BitSet bits) {
        this.bits = bits;
    }

    public BitField(byte bytes[]) {
        this.bits = bits;
    }

    // TODO: test if this is working properly
    public byte[] toByteArray() {
        byte[] bytes = new byte[(int) Math.ceil(bits.length() / 8.)];
        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                bytes[i/8] |= 1 << (i % 8);
            }
        }
        return bytes;
    }

}
