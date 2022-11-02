package com.ja90n.blockdefence.towers;

import org.bukkit.Location;

import java.util.ArrayList;

public interface Moveable extends Tower{
    void move();
    ArrayList<Location> getPath();
}
