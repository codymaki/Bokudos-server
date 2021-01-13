package com.bokudos.bokudosserver.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Game {

    @NotNull
    private UUID gameId;

    @NonNull
    private GameStatus gameStatus;

}
