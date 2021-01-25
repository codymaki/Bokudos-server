package com.bokudos.bokudosserver.external.stagebuilder.v1.data;

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

    public void printToConsole() {
        for(int i = 0; i < 100; i++) {
            StringBuilder row = new StringBuilder();
            for(int j = 0; j< 100; j++) {
                Boolean test = getTile(j, i);
                row.append(test != null && test ? "X" : " ");
            }
            System.out.println(row);
        }


    }
}
