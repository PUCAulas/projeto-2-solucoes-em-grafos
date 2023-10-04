package br.pucminas.solucoesemgrafos.entities;

import java.util.*;

public class Graph {

    List<Vertex> vertices;
    List<Edge> edges;
    int[][] adjacencyMatrix;

    public Graph(List<Vertex> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.adjacencyMatrix = new int[vertices.size()][vertices.size()];

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }

        for (Edge edge : edges) {
            int originIndex = vertices.indexOf(edge.getOrigin());
            int destinyIndex = vertices.indexOf(edge.getDestiny());
            adjacencyMatrix[originIndex][destinyIndex] = 1;
        }
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void printAdjacencyMatrix() {
        int n = vertices.size();

        System.out.println("Matriz de Adjacência:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }


    public boolean isConnected() {
        List<Vertex> allVertices = new ArrayList<>(vertices);

        for (int i = 0; i < allVertices.size(); i++) {

            Set<Vertex> visited = new HashSet<>();
            Queue<Vertex> queue = new LinkedList<>();
            Vertex startVertex = allVertices.get(i);

            queue.offer(startVertex);
            visited.add(startVertex);

            while (!queue.isEmpty()) {
                Vertex currentVertex = queue.poll();

                for (Edge edge : edges) {
                    if (edge.getOrigin().equals(currentVertex)) {
                        Vertex neighbor = edge.getDestiny();

                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.offer(neighbor);
                        }
                    }
                }
            }

            if (visited.size() != allVertices.size()) {
                return false;
            }
        }
        return true;
    }

    public List<Vertex> getUnreachableVertices(Vertex sourceVertex) {

        int initialPosition = sourceVertex.getId() - 1;
        List<Vertex> unreachableVertices = new ArrayList<>();

        for (int i = 0; i < adjacencyMatrix[initialPosition].length; i++) {
            if (!vertices.get(i).equals(sourceVertex)) {
                if (adjacencyMatrix[initialPosition][i] == 0) {
                    unreachableVertices.add(vertices.get(i));
                }
            }
        }
        return unreachableVertices;
    }
    
    public List<Vertex> getReachableVertices(Vertex sourceVertex) {

        int initialPosition = sourceVertex.getId() - 1;
        List<Vertex> reachableVertices = new ArrayList<>();

        for (int i = 0; i < adjacencyMatrix[initialPosition].length; i++) {
            if (!vertices.get(i).equals(sourceVertex)) {
                if (adjacencyMatrix[initialPosition][i] == 1) {
                    reachableVertices.add(vertices.get(i));
                }
            }
        }
        return reachableVertices;
    }
    
	private void DFS(int initialPosition, Set<Integer> visited, List<String> recommendations) {
        if (visited.contains(initialPosition)) 
        	return;
        
        visited.add(initialPosition);
        recommendations.add("\nVisite a cidade: " + vertices.get(initialPosition).getCityName());

        for (int i = 0; i < vertices.size(); i++) {
            if (adjacencyMatrix[initialPosition][i] == 1 && !visited.contains(i)) {
                recommendations.add("Viaje pela estrada de " + vertices.get(initialPosition).getCityName() + " para " + vertices.get(i).getCityName());
                DFS(i, visited, recommendations);
            }
        }
    }

    public List<String> visitAllRoadsAndCities(Vertex sourceVertex) {
        List<String> recommendations = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        int sourceVertexIndex = vertices.indexOf(sourceVertex);
        
        DFS(sourceVertexIndex, visited, recommendations);

        return recommendations;
    }

}
