package com.epam.impl;

import com.epam.api.GpsNavigator;
import com.epam.api.Path;
import com.epam.domain.GraphVertex;
import com.epam.domain.RoadSegment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GpsNavigatorImpl implements GpsNavigator {

    private Map<String, GraphVertex> graph = null;

    @Override
    public void readData(String filePath) {
        List<RoadSegment> roadMap = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            roadMap = stream
                    .map(this::createRoadSegmentFromString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        graph = new HashMap<>(roadMap.size());

        for (RoadSegment roadSegment : roadMap) {
            String start = roadSegment.getStart();
            if (!graph.containsKey(start))
                graph.put(start, new GraphVertex(start));
            String end = roadSegment.getEnd();
            if (!graph.containsKey(end))
                graph.put(end, new GraphVertex(end));
        }

        for (RoadSegment roadSegment : roadMap) {
            graph.get(roadSegment.getStart()).getNeighbours().put(graph.get(roadSegment.getEnd()), roadSegment.getSegmentTotalCost());
        }

    }

    private RoadSegment createRoadSegmentFromString(final String line) {
        final String[] strings = line.split(" ");
        final int roadSegmentTotalCost = Integer.valueOf(strings[2]) * Integer.valueOf(strings[3]);
        return new RoadSegment(strings[0], strings[1], roadSegmentTotalCost);
    }

    @Override
    public Path findPath(String pointA, String pointB) {
        findAllClosesRoutes(pointA);
        if (!graph.containsKey(pointB)) {
            System.err.printf("Graph doesn't contain end point \"%s\"\n", pointB);
            return new Path(Collections.singletonList(""), 0);
        }
        GraphVertex graphVertex = graph.get(pointB);
        List<String> path = graphVertex.getPath();
        int pathTotalCost = graphVertex.getVertexCost();
        return new Path(path, pathTotalCost);
    }

    private void findAllClosesRoutes(String startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph doesn't contain start point \"%s\"\n", startName);
            return;
        }
        final GraphVertex source = graph.get(startName);
        NavigableSet<GraphVertex> q = new TreeSet<>();

        for (GraphVertex v : graph.values()) {
            v.setPrevious(v == source ? source : null);
            v.setVertexCost(v == source ? 0 : Integer.MAX_VALUE);
            q.add(v);
        }

        findClosesRoutes(q);
    }

    private void findClosesRoutes(final NavigableSet<GraphVertex> q) {
        GraphVertex u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst();
            if (u.getVertexCost() == Integer.MAX_VALUE)
                break;

            for (Map.Entry<GraphVertex, Integer> a : u.getNeighbours().entrySet()) {
                v = a.getKey();

                final int alternateDist = u.getVertexCost() + a.getValue();
                if (alternateDist < v.getVertexCost()) {
                    q.remove(v);
                    v.setVertexCost(alternateDist);
                    v.setPrevious(u);
                    q.add(v);
                }
            }
        }
    }
}
