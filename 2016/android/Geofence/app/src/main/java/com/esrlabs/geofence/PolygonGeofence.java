package com.esrlabs.geofence;

import android.location.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolygonGeofence implements Geofence {

    final List<Location> polygonPoints;

    public PolygonGeofence(Location... locations) {
        polygonPoints = new ArrayList<Location>(Arrays.asList(locations));
    }

    /**
     * It implements the Ray casting algorithm, with the convention that the line is horizontal and
     * goes from left to right.
     * @param location The point to be checked if is inside the defined polygon shaped geofence
     * @return True if the location is inside the defined polygon.
     */
    @Override
    public boolean containsLocation(Location location) {
        return pointIsInRegion(location);
    }

    private boolean pointIsInRegion(Location aTestLocation) {
        boolean inside = false;

        int count = polygonPoints.size();
        for (int i = 0; i < count; i++) {
            Location beginOfLineLocation = polygonPoints.get(i);
            Location endOfLineLocation = polygonPoints.get((i + 1) % count);
            if (rayCrossesSegment(aTestLocation, beginOfLineLocation, endOfLineLocation)) {
                inside = !inside;
            }
        }

        return inside;
    }

    private boolean rayCrossesSegment(Location point, Location beginOfLine, Location endOfLine) {
        double pointX = point.getLongitude();
        double pointY = point.getLatitude();
        double beginOfLineX = beginOfLine.getLongitude();
        double beginOfLineY = beginOfLine.getLatitude();
        double endOfLineX = endOfLine.getLongitude();
        double endOfLineY = endOfLine.getLatitude();

        if (beginOfLineY > endOfLineY) {
            return rayCrossesSegment(point, endOfLine, beginOfLine);
        }

        if (pointY == beginOfLineY || pointY == endOfLineY) {
            pointY += 0.00000001;
        }

        if ( (pointY > endOfLineY || pointY < beginOfLineY) || (pointX > Math.max(beginOfLineX, endOfLineX)) ) {
            return false;
        }

        if (pointX < Math.min(beginOfLineX, endOfLineX)) {
            return true;
        }

        double red = (pointY - beginOfLineY) / (double) (pointX - beginOfLineX);
        double blue = (endOfLineY - beginOfLineY) / (double) (endOfLineX - beginOfLineX);

        return (blue >= red);
    }
}
