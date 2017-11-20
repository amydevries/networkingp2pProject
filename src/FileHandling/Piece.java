package FileHandling;

public class Piece {

    private byte[] data;
    private boolean full;
    private boolean requested = false;
    private CommonReader commonReader = CommonReader.getCommonReader();


    public Piece(){
        data = new byte[commonReader.getPieceSize()];
        full = false;
    }

    public Piece(byte[] data){
        this.data = data;
        full = true;
    }

    public byte[] getData(){
        if(data != null)
            return data;
        else
            return null;
    }

    public void setData(byte[] data){
        this.data = data;
        full = true;
    }

    public boolean hasBeenRequested(){
        return requested;
    }

    public void request(){
        requested = true;
    }
}
