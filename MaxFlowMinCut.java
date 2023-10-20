import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MaxFlowMinCut {
    private int[][] capacity;
    private int[][] flow;
    private int[] parent;
    private int numVertices;

    public MaxFlowMinCut(int numVertices) {
        this.numVertices = numVertices;
        capacity = new int[numVertices][numVertices];
        flow = new int[numVertices][numVertices];
        parent = new int[numVertices];
    }

    public void addEdge(int source, int dest, int capacity) {
        this.capacity[source][dest] = capacity;
    }

    public boolean bfs(int source, int sink) {
        Arrays.fill(parent, -1);
        parent[source] = source;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int next = 0; next < numVertices; next++) {
                if (parent[next] == -1 && capacity[current][next] - flow[current][next] > 0) {
                    parent[next] = current;
                    if (next == sink) return true;
                    queue.add(next);
                }
            }
        }

        return false;
    }

    public int maxFlow(int source, int sink) {
        int maxFlow = 0;

        while (bfs(source, sink)) {
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, capacity[u][v] - flow[u][v]);
            }

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                flow[u][v] += pathFlow;
                flow[v][u] -= pathFlow;
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("data/ejemplo.txt");
        Scanner scanner = new Scanner(file);

        int numVertices = scanner.nextInt();
        MaxFlowMinCut graph = new MaxFlowMinCut(numVertices);

        while (scanner.hasNext()) {
            int source = scanner.nextInt();
            int dest = scanner.nextInt();
            int capacity = scanner.nextInt();
            graph.addEdge(source, dest, capacity);
        }
        scanner.close();

        System.out.println("El flujo máximo es " + graph.maxFlow(0, numVertices - 1));
    }
}
