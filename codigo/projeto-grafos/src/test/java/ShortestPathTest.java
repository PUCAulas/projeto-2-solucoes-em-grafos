import entities.ShortestPath;
import entities.Vertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ShortestPathTest {

    private ShortestPath shortestPath;

    private Vertex sourceVertex;

    private Vertex destinationVertex;


    @BeforeEach
    void setUp() {
        sourceVertex = new Vertex("São Paulo");
        destinationVertex = new Vertex("Rio de Janeiro");
        int distance = 430;
        shortestPath = new ShortestPath(sourceVertex, destinationVertex, distance);
    }


    @Test
    @DisplayName("Método get do atributo sourceVertex")
    void getSourceVertexTest() {
        Assertions.assertSame(sourceVertex, shortestPath.getSourceVertex());
    }

    @Test
    @DisplayName("Método get do atributo destinationVertex")
    void getDestinationVertexTest() {
        Assertions.assertSame(destinationVertex, shortestPath.getDestinationVertex());
    }

    @Test
    @DisplayName("Método get do atributo distance")
    void getDistanceTest() {
        Assertions.assertEquals(430, shortestPath.getDistance());
    }


}
