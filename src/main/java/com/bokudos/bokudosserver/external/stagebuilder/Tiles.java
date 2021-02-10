package com.bokudos.bokudosserver.external.stagebuilder;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class uses a HashMap, therefore it is not threadsafe.
 * A different Tiles instance should be used per game even if the tiles are built from the same stage.
 */
public class Tiles {

    private Map<Integer, Map<Integer, Boolean>> data;

    public Tiles() {
        data = new TreeMap<>();
    }

    public void setTile(int column, int row, Boolean value) {
        Map<Integer, Boolean> nested = data.computeIfAbsent(column, k -> new TreeMap<>());
        nested.put(row, value);
    }

    public Boolean getTile(int column, int row) {
        return data.containsKey(column) ? data.get(column).getOrDefault(row, null) : null;
    }
}
