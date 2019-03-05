package com.epam.domain;

public class RoadSegment {

    private final String start;
    private final String end;
    private final int segmentTotalCost;

    public RoadSegment(String start, String end, int segmentTotalCost) {
        this.start = start;
        this.end = end;
        this.segmentTotalCost = segmentTotalCost;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getSegmentTotalCost() {
        return segmentTotalCost;
    }
}