package com.esrlabs.geofence;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolygonGeofence implements Geofence {

    private final List<Location> polygonPoints;

    /**
     * Initiates a Polygon, that is a closed structure.
     * This means, that the first and the last Location is the same and every pair of
     * locations forms a distinct line of a simple polygon.
     *
     * @param locations A closed ring of locations as corners
     */
    public PolygonGeofence(Location... locations) {
        polygonPoints = new ArrayList<>();
        Collections.addAll(polygonPoints, locations);
    }

    /**
     * It implements the Ray casting algorithm, with the convention that the line is horizontal and
     * goes from left to right.
     *
     * @param location The point to be checked if is inside the defined polygon shaped geofence
     * @return True if the location is inside the defined polygon.
     */
    @Override
    public boolean containsLocation(Location location) {
        int numberOfCorners = polygonPoints.size();

        List<Location> corners = new ArrayList<>(polygonPoints);
        // TODO: implement this function according to the docstring
        return false;
    }
}
