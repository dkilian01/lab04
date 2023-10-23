package servers;

import ifaces.RegistryIface;
import ifaces.SerwerIface;
import knapsackProblem.*;
import nodes.ServerNode;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements SerwerIface {
    Algorithm algorithm;


    @Override
    public Solution solve(Instance instance) throws RemoteException {
        System.out.println("Resolving some problem...");
        if(instance != null){
            Solution solution = algorithm.foundSolution(instance);
            System.out.println("Done");
            return solution;
        }
        System.out.println("Some problems :(");
        return null;
    }

    public static void main(String args[]) {
        Server server = new Server();
        switch (args.length) {
            case 0:
                System.out.println("Not enough args");
                break;
            case 1:
                System.out.println("Not enough args");
                break;
            case 2:
                if (!server.setAlgorithm(args[0])) return;
                server.startServer("localhost", Integer.parseInt(args[1]), "Server", "Spis");
                break;
            case 3:
                if (!server.setAlgorithm(args[0])) return;
                server.startServer("localhost", Integer.parseInt(args[1]), args[2], "Spis");
                break;
            case 4:
                if (!server.setAlgorithm(args[0])) return;
                server.startServer("localhost", Integer.parseInt(args[1]), args[2], args[3]);
                break;
            case 5:
                if (!server.setAlgorithm(args[0])) return;
                server.startServer(args[4], Integer.parseInt(args[1]), args[2], args[3]);
                break;
            default:
                System.out.println("Too many args");

        }
    }

    public void startServer(String ip, int port, String serverName, String registryName) {
        try {
            SerwerIface serwerIface = (SerwerIface) UnicastRemoteObject.exportObject(this, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(ip, port);
            registry.bind(serverName, serwerIface);

            RegistryIface serwersRegistry = (RegistryIface) registry.lookup(registryName);
            boolean serwersRegistryResponse = serwersRegistry.register(new ServerNode(serverName, algorithm.getName()));
            if (serwersRegistryResponse) {
                System.err.println("Server is ready");
                System.out.println("Server binded name: " + serverName);
                System.out.println("Server algorithm: " + algorithm.getName());
            } else System.err.println("Server name already exist in servers registry");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public boolean setAlgorithm(String name) {
        switch (name) {
            case "bruteForce":
                algorithm = new BruteForce();
                break;
//            case "bestOfGreedyAndRandomSolution":
//                algorithm = new bestOfGreedyAndRandomSolution();
//                break;
            case "greedy":
                algorithm = new Greedy();
                break;
            case "randomSolution":
                algorithm = new RandomSolution();
                break;
            default:
                System.out.println("Bad algorithm name");
                return false;
        }
        return true;
    }

}
