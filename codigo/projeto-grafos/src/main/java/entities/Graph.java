package entities;

import records.ShortestPath;

import java.util.*;

public class Graph {

    List<Vertex> vertices;
    List<Edge> edges;
    int[][] adjacencyMatrix;

    /**
     * Construtor padrao do grafo
     *
     * @param vertices vertices do grafo
     * @param edges    arestas do grafo
     */
    public Graph(List<Vertex> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
        initializeAdjacencyMatrix();
        fillAdjacencyMatrix();
    }

    /**
     * Inicializa a matriz de adjacencia vazia, colocando o valor de 0 em cada posicao
     */
    private void initializeAdjacencyMatrix() {
        this.adjacencyMatrix = new int[vertices.size()][vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }
    }

    /**
     * Preenche a matriz de adjacencia. Nas posicoes em que existe um caminho entre os dois vertices, ou seja,
     * existe uma aresta entre a cidade da linha e a da coluna da matriz, e colocado o valor de 1 nessa posicao
     */
    private void fillAdjacencyMatrix() {
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

    /**
     * Imprime a matriz de adjacencia
     */
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

    /**
     * Busca em largura - Questao (a)
     *
     * @return boolean indicando se grafo e conexo ou nao
     */
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
            if (visited.size() != allVertices.size())
                return false;
        }
        return true;
    }

    /**
     * Lista as cidades que nao sao alcancadas por uma cidade de referencia
     * (sourceVertex) - Questao (b)
     *
     * @param sourceVertex vertice de referencia
     * @return lista de vertices que nao sao alcancados pelo vertice de referencia
     */
    public List<Vertex> getUnreachableVertices(Vertex sourceVertex) {

        int matrixRow = this.vertices.indexOf(sourceVertex);
        List<Vertex> unreachableVertices = new ArrayList<>();

        for (int i = 0; i < adjacencyMatrix[matrixRow].length; i++) {
            if (!(matrixRow == i) && adjacencyMatrix[matrixRow][i] == 0)
                unreachableVertices.add(vertices.get(i));
        }
        return unreachableVertices;
    }

    /**
     * Lista as cidades que sao alcancadas por uma cidade de referencia
     * (sourceVertex)
     *
     * @param sourceVertex vertice de referencia
     * @return lista de vertices que sao alcancados pelo vertice de referencia
     */
    public List<Vertex> getReachableVertices(Vertex sourceVertex) {

        int matrixRow = this.vertices.indexOf(sourceVertex);
        List<Vertex> reachableVertices = new ArrayList<>();

        for (int i = 0; i < adjacencyMatrix[matrixRow].length; i++) {
            if (!(matrixRow == i) && adjacencyMatrix[matrixRow][i] != 0)
                reachableVertices.add(vertices.get(i));
        }
        return reachableVertices;
    }

    /**
     * Busca em profundidade
     *
     * @param initialPosition posicao inicial de busca
     * @param visited         marcacao em cada vertice para que a busca nao re-visite um vertice marcado
     * @param currentRoute    rota atual que está sendo explorada
     * @param allRoutes       lista de rotas recomendadas
     */
    private void DFS(int initialPosition, List<Integer> visited, Route currentRoute, List<Route> allRoutes) {
        visited.add(initialPosition);
        currentRoute.addVertex(vertices.get(initialPosition));

        boolean hasUnvisitedNeighbor = false;
        for (int i = 0; i < vertices.size(); i++) {
            if (adjacencyMatrix[initialPosition][i] == 1 && !visited.contains(i)) {
                hasUnvisitedNeighbor = true;
                DFS(i, new ArrayList<>(visited), new Route(currentRoute), allRoutes);
            }
        }
        if (!hasUnvisitedNeighbor)
            allRoutes.add(currentRoute);
    }

    /**
     * Fornece recomendacoes de cidades para visitar, a partir de um vertice -
     * Questao (c)
     *
     * @param sourceVertex vertice de referencia
     * @return lista de rotas recomendadas
     */
    public List<Route> visitAllRoadsAndCities(Vertex sourceVertex) {
        List<Route> allRoutes = new ArrayList<>();
        List<Integer> visited = new ArrayList<>();
        int sourceVertexIndex = vertices.indexOf(sourceVertex);

        DFS(sourceVertexIndex, visited, new Route(), allRoutes);
        return allRoutes;
    }

    /**
     * Encontra os caminhos mais curtos de um vértice de origem para todos os outros vértices usando o algoritmo de Dijkstra.
     * Questão (d)
     *
     * @param sourceVertex Vértice de origem para qual encontrar o menor caminho.
     * @return Lista de objetos ShortestPath com informações.
     */
    public List<ShortestPath> shortestPathsFromSource(Vertex sourceVertex) {

        int sourceVertexIndex = vertices.indexOf(sourceVertex);
        int numVertices = vertices.size();
        int[] distance = new int[numVertices];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[sourceVertexIndex] = 0;
        List<ShortestPath> shortestPaths = new ArrayList<>();
        // Prioriza a fila para selecionar o vértice com a menor distância.
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>((v1, v2) -> distance[vertices.indexOf(v1)] - distance[vertices.indexOf(v2)]);
        priorityQueue.add(sourceVertex);

        while (!priorityQueue.isEmpty()) {

            Vertex currentVertex = priorityQueue.poll();
            int currentIndex = vertices.indexOf(currentVertex);

            for (Edge edge : edges) {
                if (edge.getOrigin().equals(currentVertex)) {

                    Vertex neighbor = edge.getDestiny();
                    int neighborIndex = vertices.indexOf(neighbor);
                    int newDistance = distance[currentIndex] + edge.getDistance();

                    if (newDistance < distance[neighborIndex]) {
                        distance[neighborIndex] = newDistance;
                        priorityQueue.add(neighbor);
                        // Armazena informações do caminho mais curto
                        shortestPaths.add(new ShortestPath(sourceVertex, neighbor, newDistance));
                    }
                }
            }
        }
        return shortestPaths;
    }
}
