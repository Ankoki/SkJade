package com.ankoki.skjade.utils;

import ch.njol.util.VectorMath;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Shapes {
    private static final double _2PI = 6.283185307179586;
    private static final double _3PI = Math.PI + Math.PI + Math.PI;

    /*
function torus(center: location, majorRadius: number, minorRadius: number, density: number = 1) :: locations:
    set {_pointsOnMajorCircle} to 6.283185307179586 * {_majorRadius} * {_density}
    set {_pointsOnMinorCircle} to 6.283185307179586 * {_minorRadius} * {_density}
    set {_deltaMajor} to 360 / {_pointsOnMajorCircle}
    set {_deltaMinor} to 360 / {_pointsOnMinorCircle}
    set {_thetaMajor} to 0
    loop {_pointsOnMajorCircle} times:
        set {_tubeOffset} to vector (cos({_thetaMajor}) * {_majorRadius}), 0, (sin({_thetaMajor}) * {_majorRadius})
        set {_tube} to {_center} ~ {_tubeOffset}
        set {_rotationAngle} to 540 - {_thetaMajor}
        set {_thetaMinor} to 0
        loop {_pointsOnMinorCircle} times:
            set {_offset} to vector (cos({_thetaMinor}) * {_minorRadius}), (sin({_thetaMinor}) * {_minorRadius}), 0
            rotate {_offset} around y-axis by {_rotationAngle}
            set {_points::%loop-value-1%/%loop-value-2%} to {_tube} ~ {_offset}
            add {_deltaMinor} to {_thetaMinor}
        add {_deltaMajor} to {_thetaMajor}
    return {_points::*}
     */
    public static List<Location> getTorus2(Location centre, double majorRadius, double minorRadius, double density) {
        List<Location> points = new ArrayList<>();
        double majorPoints = _2PI * majorRadius * density;
        double minorPoints = _2PI * minorRadius * density;
        double deltaMajor = _2PI / majorPoints;
        double deltaMinor = _2PI / minorPoints;
        double thetaMajor = 0;
        for (int i = 0; i < majorPoints; i++) {
            Vector tubeOffset = new Vector(Math.cos(thetaMajor) * minorRadius, 0, Math.sin(thetaMajor * majorRadius));
            Location tube = centre.clone();
            tube.add(tubeOffset);
            double rotAngle = _3PI - thetaMajor;
            double thetaMinor = 0;
            for (int ii = 0; ii < minorPoints; ii++) {
                Vector offset = new Vector(Math.cos(thetaMinor) * minorRadius, Math.sin(thetaMinor) * minorRadius, 0);
                VectorMath.rotY(offset, rotAngle);
                Location fin = tube.clone();
                fin.add(offset);
                points.add(fin);
                thetaMinor += deltaMinor;
            }
            thetaMajor += deltaMajor;
        }
        return points;
    }
    /**
     * (W.I.P) A method to get all the points to make up a giant donut looking thing.
     *
     * @param centre The center of the torus.
     * @param majorRadius The radius of the outside.
     * @param minorRadius The radius of the inside.
     * @param density The density of the particles.
     * @return All locations to make up the torus.
     */
    public static List<Location> getTorus(Location centre, double majorRadius, double minorRadius, double density) {
        List<Location> torusPoints = new ArrayList<>();
        double pointsOnMajor = _2PI * majorRadius * density;
        double pointsOnMinor = _2PI * minorRadius * density;
        double deltaMajor = _3PI / pointsOnMajor;
        double deltaMinor = _3PI / pointsOnMinor;
        double thetaMajor = 0;
        for (int i = 0; i < pointsOnMajor; i++) {
            Vector tubeOffset = new Vector(Math.cos(thetaMajor * majorRadius), 0, Math.sin(thetaMajor * majorRadius));
            Location tube = centre.clone();
            tube.add(tubeOffset);
            double rotationAngle = 540 - thetaMajor;
            double thetaMinor = 0;
            for (int ii = 0; ii < pointsOnMinor; ii++) {
                Vector offset = new Vector(Math.cos(thetaMinor * minorRadius), Math.sin(thetaMinor * minorRadius), 0);
                VectorMath.rotY(offset, rotationAngle);
                Location toOffset = tube.clone();
                toOffset.add(offset);
                torusPoints.add(toOffset);
                thetaMinor += deltaMinor;
            }
            thetaMajor += deltaMajor;
        }
        return torusPoints;
    }

    /**
     * A method to get the list of locations in a circle.
     *
     * @param centre The centre of the circle.
     * @param radius The radius of the circle.
     * @param totalPoints The total amount of points to make up the circle.
     * @return The list of locations to make up a circle.
     */
    public static List<Location> getCircle(Location centre, double radius, double totalPoints) {
        World world = centre.getWorld();
        double increment = _2PI/totalPoints;
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < totalPoints; i++) {
            double angle = i * increment;
            double x = centre.getX() + (radius * Math.cos(angle));
            double z = centre.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, centre.getY(), z));
        }
        return locations;
    }

    /**
     * A method to get a list of locations of the points of a star.
     *
     * @param centre The centre of the star.
     * @param radius The radius of the circle which makes up the outer edge of the star.
     * @param vertices The amount of vertices a star has.
     * @return A list of locations of all points of a star.
     */
    public static List<Location> getStarPoints(Location centre, double radius, int vertices) {
        List<Location> locations = new ArrayList<>();
        double delta = _2PI / vertices;
        boolean bug = false;
        for (double theta = 0; theta < _2PI; theta += delta) {
            if (!bug && vertices == 6) {
                bug = true;
                continue;
            }
            Vector offset = new Vector(Math.sin(theta) * radius, 0, Math.cos(theta) * radius);
            Location vertex = centre.clone();
            vertex.add(offset);
            if (!locations.contains(vertex)) {
                locations.add(vertex);
            }
        }
        return locations;
    }

    /**
     * A method to get a list of locations to make up a line.
     *
     * @param pointOne The first point of the line.
     * @param pointTwo The second point of the line.
     * @param space The space between each point of a line.
     * @return A list of locations to make up a line.
     */
    public static List<Location> getLine(Location pointOne, Location pointTwo, double space) {
        List<Location> points = new ArrayList<>();
        double distance = pointOne.distance(pointTwo);
        Vector p1 = pointOne.toVector();
        Vector p2 = pointTwo.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        for (double length = 0; length < distance; p1.add(vector)) {
            points.add(p1.toLocation(pointOne.getWorld()));
            length += space;
        }
        return points;
    }

    /**
     * A method to get a list of locations to make up a cone.
     *
     * @param centre The centre of the bottom of the cone.
     * @param radius The radius of the face.
     * @param height The height of the cone.
     * @param density The density of the cone.
     * @param pointsPerCircle The amount of points per circle.
     * @return A list of locations to make up a cone.
     */
    public static List<Location> drawCone(Location centre, double radius, double height, double density, double pointsPerCircle) {
        List<Location> points = new ArrayList<>();
        Location loc = centre.clone();
        double space = 1 / density;
        double amount = height / space;
        double interval = radius / amount;
        for (int i = 0; i <= amount; i++) {
            loc.add(0, space, 0);
            points.addAll(Shapes.getCircle(loc, radius, pointsPerCircle));
            radius = radius - interval;
        }
        return points;
    }
}
