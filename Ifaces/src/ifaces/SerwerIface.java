package ifaces;

import knapsackProblem.Instance;
import knapsackProblem.Solution;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SerwerIface extends Remote {
    Solution solve(Instance instance) throws RemoteException;
}
