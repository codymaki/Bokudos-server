package com.bokudos.bokudosserver.external.stagebuilder.v1.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegionDTO {

    private int id;
    private int stageId;
    private int row;
    private int column;
    private String data;

}
