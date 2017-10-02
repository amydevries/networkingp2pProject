package Singleton;

import Handlers.IHandler;
import Peer.PeerInfo;

import java.util.Hashtable;

public class StorageSingleton {


    private Hashtable<Integer,PeerInfo> peers = new Hashtable<Integer,PeerInfo>();
    private Hashtable<String, IHandler> handlers = new Hashtable<String, IHandler>();

    public static final StorageSingleton instance = new StorageSingleton();

    private StorageSingleton(){}

    public static StorageSingleton getInstance(){
        return instance;
    }

    public Hashtable<Integer, PeerInfo> getPeers() {
        return peers;
    }

    public void setPeers(Hashtable<Integer, PeerInfo> peers) {
        this.peers = peers;
    }

    public Hashtable<String, IHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(Hashtable<String, IHandler> handlers) {
        this.handlers = handlers;
    }
}
