package registry;


import ifaces.RegistryIface;
import nodes.ServerNode;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

public class Registry implements RegistryIface {
    ArrayList<ServerNode> serwers = new ArrayList<>();

    @Override
    public boolean register(ServerNode serverNode) throws RemoteException {
        if (containServer(serverNode.getName())) return false;
        else {
            serwers.add(serverNode);
            System.out.println("Registered: " + serverNode.getName());
            return true;
        }
    }

    @Override
    public ArrayList<ServerNode> getServers() throws RemoteException {
        return serwers;
    }

    private boolean containServer(String name) {
        Iterator<ServerNode> it = serwers.iterator();
        while (it.hasNext()) {
            ServerNode next = it.next();
            if (name.equals(next.getName())) return true;
        }
        return false;
    }

    public void startRemoteRMiRegistry(int port, String name) {
        RegistryIface registryIface;
        java.rmi.registry.Registry registry;
        try {
            registryIface = (RegistryIface) UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.createRegistry(port);
            registry.bind(name, registryIface);
            System.out.println("Server is ready");
            System.out.println("RMI registry port: " + port);
            System.out.println("Server binded name: " + name);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    public void useRemoteRMiRegistry(String ip, int port, String name) {
        RegistryIface registryIface;
        java.rmi.registry.Registry registry;
        try {
            registryIface = (RegistryIface) UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.getRegistry(ip, port);
            registry.bind(name, registryIface);
            System.out.println("Server is ready");
            System.out.println("RMI registry port: " + port);
            System.out.println("RMI registry host: " + ip);
            System.out.println("Server binded name: " + name);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Registry registry = new Registry();
        int port;
        switch (args.length){
            case 0:
                System.out.println("Not enough args");
                break;
            case 1:
                port = Integer.parseInt(args[0]);
                registry.startRemoteRMiRegistry(port, "Spis");
                break;
            case 2:
                port = Integer.parseInt(args[0]);
                registry.startRemoteRMiRegistry(port, args[1]);
                break;
            case 3:
                port = Integer.parseInt(args[0]);
                registry.useRemoteRMiRegistry(args[2], port, args[1]);
                break;

                default:
                    System.out.println("Too many args");
        }
    }


}
