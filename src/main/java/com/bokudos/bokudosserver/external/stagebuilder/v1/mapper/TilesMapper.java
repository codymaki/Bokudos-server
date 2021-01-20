package com.bokudos.bokudosserver.external.stagebuilder.v1.mapper;

import com.bokudos.bokudosserver.external.stagebuilder.v1.data.RegionDTO;
import com.bokudos.bokudosserver.external.stagebuilder.v1.data.Tiles;

import java.util.Arrays;
import java.util.List;

public class TilesMapper {

    private final static int COLUMNS_PER_REGION = 100;
    private final static int ROWS_PER_REGION = 100;
    private final static List<String> EMPTY_TILE_VALUES = Arrays.asList("00", "0");
    private final static String ROW_SPLITTER = "n";
    private final static String COLUMN_SPLITTER = ",";

    public static Tiles mapFromRegions(List<RegionDTO> regions) {
        final Tiles tiles = new Tiles();
        regions.forEach(regionDTO -> {
            String[] rows = regionDTO.getData().split(ROW_SPLITTER);
            final int startR = regionDTO.getRow() * ROWS_PER_REGION;
            final int startC = regionDTO.getColumn() * COLUMNS_PER_REGION;
            for(int r = 0; r < rows.length; r++) {
                String[] columns = rows[r].split(COLUMN_SPLITTER);
                int row = startR + r;
                for(int c = 0; c < columns.length; c++) {
                    int column = startC + c;
                    if(!EMPTY_TILE_VALUES.contains(columns[c])) {
                        tiles.setTile(column, row, true);
                    }
                }
            }
        });
        return tiles;
    }
}
