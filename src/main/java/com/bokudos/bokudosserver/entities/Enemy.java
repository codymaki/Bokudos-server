package com.bokudos.bokudosserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Enemy extends AbstractObject {

    @Id
    @NotNull
    private UUID enemyId;

    @OneToOne
    @JoinColumn(name = "gameId")
    private Game game;
}
