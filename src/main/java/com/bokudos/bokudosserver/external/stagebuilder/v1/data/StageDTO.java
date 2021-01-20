package com.bokudos.bokudosserver.external.stagebuilder.v1.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StageDTO {

    private int stageId;
    private String name;
    private int userId;
    private int gameId;
    private boolean published;

}
