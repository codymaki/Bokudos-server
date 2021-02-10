package com.bokudos.bokudosserver.entities;

import com.bokudos.bokudosserver.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Game {

    @Id
    @NotNull
    private UUID gameId;

    @NonNull
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @NonNull
    private int stageId;
}
