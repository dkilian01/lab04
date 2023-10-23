package ifaces;

import nodes.ServerNode;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RegistryIface extends Remote {
    boolean register(ServerNode serverNode) throws RemoteException;
    ArrayList<ServerNode> getServers() throws RemoteException;
}
