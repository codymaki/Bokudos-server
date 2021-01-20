package com.bokudos.bokudosserver.dtos;

import com.bokudos.bokudosserver.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GameDTO {

    @NotNull
    private UUID gameId;

    @NonNull
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @NonNull
    private int stageId;
}
