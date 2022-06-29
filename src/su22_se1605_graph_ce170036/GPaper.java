package su22_se1605_graph_ce170036;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Pham Nhat Quang
 */
public class GPaper extends JPanel {

    /**
     * Maximum number of vertex allowed
     */
    public static final int MAX_VERTEX = 50;

    /**
     * Separator used when display information
     */
    public static final String SEPARATOR = "  ";

    private JTextArea txtGraphInfo = null;
    private int graphType = 0;

    private int numberOfVertices = 0;
    private int[][] graph;
    private ArrayList<GVertex> vertices;
    private ArrayList<GEdge> edges;
    private int startIndex = -1;
    private int stopIndex = -1;
    private int edgeValue = 1;

    private Graphics2D g = null;
    private int mouseX, mouseY, selectedVertexIndex = - 1, selectedEdgeIndex = -1;
    private boolean isShift = false, isCtrl = false;

    private String result = ""; //Result of BFS, DFS traversal

    boolean isVisited[];
    Queue<Integer> q;
    Stack<Integer> s;

    /**
     * Create and initialize new GPaper instance
     */
    public GPaper() {
        distance = new int[MAX_VERTEX];
        theVertexBefore = new int[MAX_VERTEX];

        q = new LinkedList<>();
        s = new Stack<>();
        isVisited = new boolean[MAX_VERTEX];

        traversalReset();
        resetPrim();

        this.graph = new int[MAX_VERTEX][MAX_VERTEX];
        for (int i = 0; i < MAX_VERTEX; i++) {
            for (int j = 0; j < MAX_VERTEX; j++) {
                this.graph[i][j] = 0;
            }
        }

        this.numberOfVertices = 0;
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();

        this.mouseX = 0;
        this.mouseY = 0;
        this.selectedVertexIndex = -1;

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e); //To change body of generated methods, choose Tools | Templates.
                mouseX = e.getX();
                mouseY = e.getY();
                moveVertex_dragged();
            }

        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
                mouseX = e.getX();
                mouseY = e.getY();

                isCtrl = e.isControlDown();
                isShift = e.isShiftDown();

                checkMouseClicked();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
                mouseX = e.getX();
                mouseY = e.getY();

                moveVertex_start();
            }

        });
    }

    private void traversalReset() {
        result = "";
        for (int i = 0; i < isVisited.length; i++) {
            isVisited[i] = false;
        }
        q.clear();
        s.clear();
    }

    /**
     *
     */
    private void checkMouseClicked() {
        if (isCtrl) {
            addVertex();
        } else if (isShift) {
            removeVertex();
            removeEdge();
        } else {
            selectVertex();
            selectEdge();
        }
        repaint();
    }

    /**
     *
     */
    private void moveVertex_dragged() {
        if (selectedVertexIndex > -1) {
            this.vertices.get(selectedVertexIndex).setX(mouseX);
            this.vertices.get(selectedVertexIndex).setY(mouseY);
            repaint();
        }
    }

    /**
     *
     */
    private void moveVertex_start() {
        selectedVertexIndex = findVertexByLocation(mouseX, mouseY);
    }

    /**
     *
     * @param mouseX
     * @param mouseY
     * @return
     */
    private int findVertexByLocation(int mouseX, int mouseY) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).isInside(mouseX, mouseY)) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param value
     * @return
     */
    private int findVertexByValue(int value) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getValue() == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param mouseX
     * @param mouseY
     * @return
     */
    private int findEdgeByLocation(int mouseX, int mouseY) {
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).isInside(mouseX, mouseY)) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param from
     * @param to
     * @return
     */
    private int findEdgeByVertex(int from, int to) {
        GEdge edge;
        for (int i = 0; i < edges.size(); i++) {
            edge = edges.get(i);
            if ((edge.getStart().getValue() == from && edge.getEnd().getValue() == to)
                    || edge.getStart().getValue() == to && edge.getEnd().getValue() == from) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     */
    private void addVertex() {
        this.vertices.add(new GVertex(mouseX, mouseY, this.numberOfVertices));
        this.numberOfVertices++;
        for (int i = 0; i < this.numberOfVertices; i++) {
            graph[i][this.numberOfVertices - 1] = 0;
            graph[this.numberOfVertices - 1][i] = 0;
        }
        updateGraphInfo();
    }

    /**
     *
     */
    private void removeVertex() {
        int vertexIndex = findVertexByLocation(mouseX, mouseY);
        System.out.println(vertexIndex);
        if (vertexIndex > -1) {
            GVertex vertex = this.vertices.get(vertexIndex);

            vertex.setSelected(true);
            repaint();

            if (JOptionPane.showConfirmDialog(this, "Do you really want to delete this vertex " + vertex.getLabel() + "?", "Warning",
                    JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                removeVertex(vertexIndex);
            } else {
                vertex.setSelected(false);
            }
        }

    }

    private void removeVertex(int index) {
        for (int from = index; from < this.numberOfVertices - 1; from++) {
            for (int to = 0; to < this.numberOfVertices; to++) {
                graph[from][to] = graph[from + 1][to];
                graph[to][from] = graph[to][from + 1];
            }
        }
        --this.numberOfVertices;
        GEdge edge;
        for (int i = this.edges.size() - 1; i >= 0; i--) {
            edge = this.edges.get(i);
            if (edge.getStart().getValue() == index || edge.getEnd().getValue() == index) {
                this.edges.remove(i);
            }
        }

        this.vertices.remove(index);
        for (int i = index; i < this.numberOfVertices; i++) {
            GVertex current = this.vertices.get(i);
            current.setValue(current.getValue() - 1);
        }
        updateGraphInfo();
    }

    /**
     *
     */
    private void removeEdge() {
        int edgeIndex = findEdgeByLocation(mouseX, mouseY);
        if (edgeIndex > -1) {
            GEdge edge = this.edges.get(edgeIndex);

            String edgeLabel = edge.getStart().getLabel() + " - " + edge.getEnd().getLabel();
            edge.setSelected(true);
            repaint();

            if (JOptionPane.showConfirmDialog(this, "Do you really want to delete this edge " + edgeLabel + "?", "Warning",
                    JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                removeEdge(edgeIndex);
            } else {
                edge.setSelected(false);
            }
        }
    }

    private void removeEdge(int index) {
        GEdge edge = this.edges.get(index);
        int from = edge.getStart().getValue();
        int to = edge.getEnd().getValue();

        graph[from][to] = 0;
        graph[to][from] = 0;
        this.edges.remove(index);
        updateGraphInfo();
    }

    /**
     *
     */
    private void selectVertex() {
        selectedVertexIndex = findVertexByLocation(mouseX, mouseY);
        if (selectedVertexIndex > -1) {
            if (startIndex == -1) {
                startIndex = selectedVertexIndex;
                this.vertices.get(startIndex).setSelected(true);
            } else if (startIndex == selectedVertexIndex) {
                this.vertices.get(startIndex).setSelected(false);
                startIndex = -1;
            } else {
                this.vertices.get(startIndex).setSelected(false);
                stopIndex = selectedVertexIndex;
                addEdge();
            }
        }
    }

    /**
     *
     */
    private void selectEdge() {
        selectedEdgeIndex = findEdgeByLocation(mouseX, mouseY);
        if (selectedEdgeIndex > -1) {
            this.edges.get(selectedEdgeIndex).setSelected(true);
            repaint();
            updateEdge();
            this.edges.get(selectedEdgeIndex).setSelected(false);
            selectedEdgeIndex = -1;
        }

    }

    /**
     * Update value of an edge
     */
    public void updateEdge() {
        GEdge edge = this.edges.get(selectedEdgeIndex);
        this.startIndex = edge.getStart().getValue();
        this.stopIndex = edge.getEnd().getValue();
        try {
            this.edgeValue = Integer.parseInt(JOptionPane.showInputDialog(this, "Please enter new value for this edge: ", edge.getValue() + ""));
            edge.setValue(edgeValue);
            graph[stopIndex][startIndex] = edgeValue;
            graph[startIndex][stopIndex] = edgeValue;
            startIndex = stopIndex = -1;
            updateGraphInfo();
        } catch (NumberFormatException e) {
            System.err.println(e);
        }
    }

    /**
     *
     */
    private void updateGraphInfo() {
        String giStr = "";
        if (this.graphType == 0) {
            giStr += this.numberOfVertices + "";
            for (int i = 0; i < this.numberOfVertices; i++) {
                giStr += "\n" + graph[i][0];
                for (int j = 1; j < this.numberOfVertices; j++) {
                    giStr += SEPARATOR + graph[i][j];
                }
            }
        } else {
            int countEdge = 0;
            for (int i = 0; i < this.numberOfVertices; i++) {
                for (int j = i + 1; j < this.numberOfVertices; j++) {
                    if (graph[i][j] > 0) {
                        giStr += "\n" + vertices.get(i).getLabel() + " " + vertices.get(j).getLabel() + " " + graph[i][j];
                        ++countEdge;
                    }
                }
            }
            giStr = this.numberOfVertices + " " + countEdge + giStr;
        }
        this.txtGraphInfo.setText(giStr);
    }

    /**
     * Set the type of graph to display
     *
     * @param graphType Type o graph representation (0 for matrix, 1 for list)
     */
    public void setGraphType(int graphType) {
        this.graphType = graphType;
        updateGraphInfo();
    }

    /**
     * Clear data of GPaper
     */
    public void clear() {
        for (int i = 0; i < numberOfVertices; i++) {
            for (int j = 0; j < numberOfVertices; j++) {
                graph[i][j] = 0;
            }
        }
        this.vertices.clear();
        this.edges.clear();
        numberOfVertices = 0;
        updateGraphInfo();
        repaint();
    }

    /**
     * Set the text of a graph information TextArea
     *
     * @param txtGraphInfo TextArea to set text
     */
    public void setTxtGraphInfo(JTextArea txtGraphInfo) {
        this.txtGraphInfo = txtGraphInfo;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        this.g = (Graphics2D) graphics;
        this.g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (int i = 0; i < edges.size(); i++) {
            edges.get(i).draw(g);
        }
        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i).draw(g);
        }

        if (!result.equals("")) {
            g.setColor(Color.red);
            g.drawString(result, 10, 20);
            result = "";
        }

        if (isDrawPrimPath) {
            String str = "";
            if (isGraphConnected) {
                int sum = 0;
                for (int i = 0; i < numberOfVertices; i++) {
                    sum += distance[i];
                }

                str = "The minimum spanning tree has the value: " + sum;

                int fromVertex;
                int toVertex;
                int edgeIndex;
                for (int i = 0; i < numberOfVertices; i++) {
                    fromVertex = theVertexBefore[i];
                    toVertex = i;
                    
                    if (fromVertex != toVertex){
                        edgeIndex = findEdgeByVertex(fromVertex, toVertex);
                        if (edgeIndex!=-1){
                            edges.get(edgeIndex).setSelected(true);
                            edges.get(edgeIndex).draw(g);
                             edges.get(edgeIndex).setSelected(false);
                        }
                    }
                }
                
                for (int i = 0; i < numberOfVertices; i++) {
                    vertices.get(i).setSelected(true);
                    vertices.get(i).draw(g);
                    vertices.get(i).setSelected(false);
                }
            } else {
                str = "The graph is not connected";
            }

            g.setColor(Color.red);
            g.drawString(str, 10, 20);
            isDrawPrimPath = false;
        }
    }

    /**
     *
     */
    private void addEdge() {
        selectedEdgeIndex = findEdgeByVertex(startIndex, stopIndex);
        if (selectedEdgeIndex == -1) {
            this.edgeValue = Integer.parseInt(JOptionPane.showInputDialog(this,
                    "Please enter the value for edge", "1"));
            this.edges.add(new GEdge(edgeValue, vertices.get(startIndex), vertices.get(stopIndex), false));
            graph[stopIndex][startIndex] = edgeValue;
            graph[startIndex][stopIndex] = edgeValue;
            startIndex = stopIndex = -1;
        }
        updateGraphInfo();
    }

    /**
     * Get vertices field
     *
     * @return data of vertices
     */
    public ArrayList<GVertex> getVertices() {
        return vertices;
    }

    /**
     * Get number of vertices
     *
     * @return the number of vertices
     */
    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    /**
     * Get graph field
     *
     * @return data of graph
     */
    public int[][] getGraph() {
        return graph;
    }

    /**
     * Read data in matrix save file
     *
     * @param fileOpen file to read
     */
    public void readMatrixDataFile(File fileOpen) {
        try (Scanner sc = new Scanner(fileOpen)) {
            this.edges.clear();
            this.vertices.clear();
            this.numberOfVertices = sc.nextInt();
            int x, y;
            for (int i = 0; i < this.numberOfVertices; i++) {
                x = sc.nextInt();
                y = sc.nextInt();
                this.vertices.add(new GVertex(x, y, i));
            }
            for (int i = 0; i < this.numberOfVertices; i++) {
                for (int j = 0; j < this.numberOfVertices; j++) {
                    this.graph[i][j] = sc.nextInt();
                    if (i < j && this.graph[i][j] > 0) {
                        this.edges.add(new GEdge(this.graph[i][j], this.vertices.get(i), this.vertices.get(j), false));
                    }
                }
            }
            updateGraphInfo();
            repaint();
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }

    /**
     * Read data in list save file
     *
     * @param fileOpen file to read
     */
    public void readListDataFile(File fileOpen) {
        try (Scanner sc = new Scanner(fileOpen)) {
            this.edges.clear();
            this.vertices.clear();
            this.numberOfVertices = sc.nextInt();
            int countEdge = sc.nextInt();
            int x, y;
            for (int i = 0; i < this.numberOfVertices; i++) {
                x = sc.nextInt();
                y = sc.nextInt();
                this.vertices.add(new GVertex(x, y, i));
            }

            for (int i = 0; i < this.numberOfVertices; i++) {
                for (int j = 0; j < this.numberOfVertices; j++) {
                    this.graph[i][j] = 0;
                }
            }
            int start, end, value;
            for (int i = 0; i < countEdge; i++) {
                start = sc.nextInt();
                end = sc.nextInt();
                value = sc.nextInt();
                this.edges.add(new GEdge(value, this.vertices.get(start), this.vertices.get(end), false));
                this.graph[start][end] = this.graph[end][start] = value;
            }
            updateGraphInfo();
            repaint();
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }

    /**
     * Perform Breadth First Search and store traversal result in result String
     */
    public void BFS() {
        traversalReset();
        int startVertex = Integer.parseInt(JOptionPane.showInputDialog(this, "Please enter value of vertex to start traversal", "0"));
        isVisited[startVertex] = true;
        int fromVertex;
        q.add(startVertex);
        result = "The BFS traversal from vertex " + startVertex + " is: ";

        while (!q.isEmpty()) {
            fromVertex = q.poll();
            result += fromVertex + ", ";
            for (int toVertex = 0; toVertex < numberOfVertices; toVertex++) {
                if (!isVisited[toVertex] && graph[fromVertex][toVertex] > 0) {
                    q.add(toVertex);
                    isVisited[toVertex] = true;
                }
            }
        }
        repaint();
    }

    /**
     * Perform Depth First Search and store traversal result in result String
     */
    public void DFS() {
        traversalReset();
        int startVertex = Integer.parseInt(JOptionPane.showInputDialog(this, "Please enter value of vertex to start traversal", "0"));
        int fromVertex;
        s.push(startVertex);
        result = "The DFS traversal from vertex " + startVertex + " is: ";

        while (!s.isEmpty()) {
            fromVertex = s.pop();
            if (!isVisited[fromVertex]) {
                result += fromVertex + ", ";
                isVisited[fromVertex] = true;
                for (int toVertex = numberOfVertices - 1; toVertex >= 0; toVertex--) {
                    if (!isVisited[toVertex] && graph[fromVertex][toVertex] > 0) {
                        s.push(toVertex);
                    }
                }
            }

        }
        repaint();
    }

    int[] distance; //Store distance of visited edges in Prim's algorithm
    int[] theVertexBefore; //beforeVertex -> currentVertex
    boolean isGraphConnected; //To mark if the graph is connected
    boolean isDrawPrimPath = false; //Inform paint method to draw Prim path or not

    /**
     * Find unvisited vertex with shortest distance
     * @return index of vertex
     */
    public int findNearestVertex() {
        int minIndex = -1, minValue = Integer.MAX_VALUE;
        for (int i = 0; i < numberOfVertices; i++) {
            if (!isVisited[i] && distance[i] < minValue) {
                minValue = distance[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * Perform Prim's algorithm to find minimum spanning tree
     */
    public void Prim() {
        resetPrim();
        distance[0] = 0;
        int currentVertex;
        isDrawPrimPath = true;
        isGraphConnected = true;

        for (int i = 0; i < numberOfVertices; i++) {
            currentVertex = findNearestVertex();
            if (currentVertex == -1) {
                isGraphConnected = false;
                break;
            } else {
                isVisited[currentVertex] = true;
                for (int toVertex = 0; toVertex < numberOfVertices; toVertex++) {
                    if (!isVisited[toVertex] && graph[currentVertex][toVertex] > 0
                            && graph[currentVertex][toVertex] < distance[toVertex]) {
                        distance[toVertex] = graph[currentVertex][toVertex];
                        theVertexBefore[toVertex] = currentVertex;
                    }
                }
            }
        }
        repaint();

    }

    /**
     *
     */
    public void resetPrim() {
        for (int i = 0; i < MAX_VERTEX; i++) {
            distance[i] = Integer.MAX_VALUE;
            theVertexBefore[i] = i;
            isVisited[i] = false;
        }
    }
}
