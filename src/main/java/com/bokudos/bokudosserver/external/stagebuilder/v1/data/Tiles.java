package com.bokudos.bokudosserver.external.stagebuilder.v1.data;

import java.util.HashMap;

public class Tiles extends HashMap<Integer, HashMap<Integer, Boolean>> {

    public void setTile(int column, int row, Boolean value) {
        HashMap<Integer, Boolean> nested = get(column);
        if(nested == null) {
            nested = new HashMap<>();
            put(column, nested);
        }
        nested.put(row, value);
    }

    public Boolean getTile(int column, int row) {
        return containsKey(column) ? get(column).getOrDefault(row, null) : null;
    }
}
