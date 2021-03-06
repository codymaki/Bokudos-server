package com.bokudos.bokudosserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

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
public class Player extends AbstractObject {

    @Id
    @NotNull
    private UUID playerId;

    @NonNull
    @Length(max = 20)
    private String name;

    @OneToOne
    @JoinColumn(name = "gameId")
    private Game game;
}
