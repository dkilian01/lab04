package clients;

import ifaces.RegistryIface;
import ifaces.SerwerIface;

import knapsackProblem.Instance;
import knapsackProblem.Item;
import knapsackProblem.Solution;
import nodes.ServerNode;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Client {
    private SerwerIface serwerStub;
    private RegistryIface registryStub;
    private ArrayList<ServerNode> servers = new ArrayList<>();
    private Registry registry;

    private int chooseServer() {
        Scanner scaner = new Scanner(System.in);
        System.out.println("Choose server ");
        showServers();
        System.out.println("-1. - to exit ");
        String choose;
        while (true) {
            choose = scaner.nextLine();
            if (Integer.parseInt(choose) < servers.size()) {
                return Integer.parseInt(choose);
            }else if(Integer.parseInt(choose) == -1) return -1;
        }
    }

    private Instance makeInstance() {
        int seed;
        Scanner scaner = new Scanner(System.in);
        System.out.println("Entry seed value: ");
        seed = Integer.parseInt(scaner.nextLine());
        Instance instance = new Instance();
        instance.generateInstanceFromLong(seed, 30);
        return instance;
    }

    private void serverQuestions() {
        Scanner scaner = new Scanner(System.in);
        String ip;
        int port;
        String name;

        System.out.println("Entry registry ip:");
        ip = scaner.nextLine();
        System.out.println("Entry registry port:");
        port = Integer.parseInt(scaner.nextLine());
        System.out.println("Entry server name:");
        name = scaner.nextLine();

        getServers(ip, port, name);
    }

    private void getServers(String ip, int port, String name) {
        try {
            registry = LocateRegistry.getRegistry(ip, port);
            registryStub = (RegistryIface) registry.lookup(name);
            servers = registryStub.getServers();
            System.out.println("Get " + servers.size() + " server(s)");
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
    private void showInstance(Instance instance){
        System.out.println("Wygenerowana instancja: ");
        System.out.println("Knapsack max weight: " + instance.getKnapsackMaxWeight());
        System.out.println("Number of items in instance: " + instance.getListOfItems().size());
    }

    private void showServers() {
        Iterator<ServerNode> it = servers.iterator();
        int i = 0;
        System.out.println("List of servers: ");
        if(servers.size() == 0 ) System.out.println("0 servers available");
        while (it.hasNext()) {
            ServerNode next = it.next();
            System.out.println(i + ". - " + "server name: " + next.getName() + ", algorithm: " + next.getAlgorithm());
            i++;
        }
    }

    private void solveProblem() throws RemoteException, NotBoundException {
        int serverIndex = chooseServer();
        if(serverIndex == -1) return;
        serwerStub = (SerwerIface) registry.lookup(servers.get(serverIndex).getName());
        Instance instance = makeInstance();
        showInstance(instance);
        Solution solution = serwerStub.solve(instance);
        if (solution != null) {
            System.out.println("Solution: ");
            System.out.println("Solution weight: " + solution.getSolutionWeight());
            System.out.println("Solution value: " + solution.getSolutionValue());
            System.out.println("Items in knapsack: " + solution.getListOfItems().size());
            Iterator<Item> it = solution.getListOfItems().iterator();
            while (it.hasNext()) {
                Item next = it.next();
                System.out.println(next.toString());
            }
        }
    }

    public void start() {
        Scanner scan = new Scanner(System.in);
        String wybor;
        while (true) {
            System.out.println(" ");
            System.out.println("--------------------------------------------------");
            System.out.println("1. - Get servers");
            System.out.println("2. - Show  servers");
            System.out.println("3. - Solve some knapsack problem");
            System.out.println("0. - Exit");
            wybor = scan.nextLine();
            int choose = Integer.parseInt(wybor);
            switch (wybor) {
                case "1":
                    serverQuestions();
                    break;
                case "2":
                    showServers();
                    break;
                case "3":
                    try {
                        solveProblem();
                    } catch (
                            NotBoundException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Error, please try again");
            }
        }


    }

    public static void main(String args[]) {
        Client client = new Client();
        client.start();
    }
}
