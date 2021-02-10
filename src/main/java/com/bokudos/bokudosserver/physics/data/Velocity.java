package com.bokudos.bokudosserver.physics.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Velocity {
    private double dx;
    private double dy;
}
