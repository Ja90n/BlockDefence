package com.ja90n.blockdefence.util;

import com.ja90n.blockdefence.BlockDefence;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public class PathGenerator {

    private BlockDefence blockDefence;
    private HashMap<Double, ArrayList<Location>> generatedPaths;

    public PathGenerator(BlockDefence blockDefence){
        this.blockDefence = blockDefence;
        generatedPaths = new HashMap<>();
    }

    public ArrayList<Location> getPath(double movementSpeed){
        return generatePath(movementSpeed);
        /*
        if (!generatedPaths.containsKey(movementSpeed)){
            generatedPaths.put(movementSpeed,generatePath(movementSpeed));
            return generatedPaths.get(movementSpeed);
        } else {
            return generatedPaths.get(movementSpeed);
        }

         */
    }

    public ArrayList<Location> generatePath(double movementSpeed){
        ArrayList<Location> path = new ArrayList<>();
        ArrayList<Location> configLocations = new ArrayList<>();

        // Gets all locations provided from the config file
        FileConfiguration config = blockDefence.getConfig();
        for (String str : config.getConfigurationSection("locations.").getKeys(false)){
            configLocations.add(getPathLocation(str));
        }

        int size = configLocations.size()-1;
        int i = 0;
        // Loops through all the locations from the config
        while (size != i){
            if (configLocations.get(i) != null){
                // Determines how many points there should be in the line
                double pointsInLine = configLocations.get(i).distance(configLocations.get(i+1))*20;
                pointsInLine = pointsInLine * movementSpeed;
                int pointsBetweenPoints = (int) Math.round(pointsInLine);

                // Adds these points to the path
                path.addAll(createLine(configLocations.get(i),configLocations.get(i+1),pointsBetweenPoints));
            }
            i++;
        }
        return path;
    }

    public ArrayList<Location> createLine(Location point1, Location point2, int pointsInLine) {
        double p1X = point1.getX();
        double p1Y = point1.getY();
        double p1Z = point1.getZ();
        double p2X = point2.getX();
        double p2Y = point2.getY();
        double p2Z = point2.getZ();

        double lineAveX = (p2X-p1X)/pointsInLine;
        double lineAveY = (p2Y-p1Y)/pointsInLine;
        double lineAveZ = (p2Z-p1Z)/pointsInLine;

        World world = point1.getWorld();
        ArrayList<org.bukkit.Location> line = new ArrayList<>();
        for(int i = 0; i <= pointsInLine; i++){
            org.bukkit.Location loc = new org.bukkit.Location(world, p1X + lineAveX * i, p1Y + lineAveY * i, p1Z + lineAveZ * i);
            line.add(loc);
        }
        return line;
    }

    public Location getPathLocation(String id){
        FileConfiguration config = blockDefence.getConfig();
        return new Location(Bukkit.getWorld(config.getString("locations." + id + ".world")),
                config.getDouble("locations." + id + ".x"),
                config.getDouble("locations." + id + ".y"),
                config.getDouble("locations." + id + ".z")
        );
    }
}
