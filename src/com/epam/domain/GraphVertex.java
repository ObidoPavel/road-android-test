package com.epam.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphVertex implements Comparable<GraphVertex> {

    private final String name;
    private int vertexCost = Integer.MAX_VALUE;
    private GraphVertex previous = null;
    private final Map<GraphVertex, Integer> neighbours = new HashMap<>();

    public GraphVertex(String name) {
        this.name = name;
    }

    public void setVertexCost(int vertexCost) {
        this.vertexCost = vertexCost;
    }

    public void setPrevious(GraphVertex previous) {
        this.previous = previous;
    }

    public int getVertexCost() {
        return vertexCost;
    }

    public String getName() {
        return name;
    }

    public Map<GraphVertex, Integer> getNeighbours() {
        return neighbours;
    }

    public int compareTo(GraphVertex other) {
        if (this.vertexCost == other.getVertexCost())
            return name.compareTo(other.getName());

        return Integer.compare(this.vertexCost, other.getVertexCost());
    }

    public List<String> getPath() {
        List<String> path = new ArrayList<>();
        GraphVertex current = this.previous;
        path.add(this.name);
        path.add(current.name);

        do {
            current = current.previous;
            path.add(current.name);
        } while (current != current.previous);

        Collections.reverse(path);
        return path;
    }
}