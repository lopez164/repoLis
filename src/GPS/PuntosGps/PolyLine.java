/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS.PuntosGps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 *
 * @author lelopez
 */
public class PolyLine {
    JXMapViewer jXMapViewer1;
    
    public void PolyLine(){
        
    }
    private void init(){
        
        GeoPosition frankfurt = new GeoPosition(50,  7, 0, 8, 41, 0);
        GeoPosition wiesbaden = new GeoPosition(50,  5, 0, 8, 14, 0);
        GeoPosition mainz     = new GeoPosition(50,  0, 0, 8, 16, 0);
        GeoPosition darmstadt = new GeoPosition(49, 52, 0, 8, 39, 0);
        GeoPosition offenbach = new GeoPosition(50,  6, 0, 8, 46, 0);

        // Create a track from the geo-positions
        List<GeoPosition> track = Arrays.asList(frankfurt, wiesbaden, mainz, darmstadt, offenbach);
        RoutePainter routePainter = new RoutePainter(track);
        
        
        // Set the focus
        jXMapViewer1.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

        // Create waypoints from the geo-positions
        Set<Waypoint> waypoints = new HashSet<Waypoint>(Arrays.asList(
                new DefaultWaypoint(frankfurt),
                new DefaultWaypoint(wiesbaden),
                new DefaultWaypoint(mainz),
                new DefaultWaypoint(darmstadt),
                new DefaultWaypoint(offenbach)));

        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter.setWaypoints(waypoints);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(routePainter);
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        jXMapViewer1.setOverlayPainter(painter);
    }
    
}
