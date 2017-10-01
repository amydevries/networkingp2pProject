package Sockets;

public interface ISocket {

    public void write(byte[] bytes);

    public int read();

    public int read(byte[] bytes);

    public void close();
}
