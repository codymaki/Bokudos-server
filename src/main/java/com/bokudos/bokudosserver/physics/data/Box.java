package com.bokudos.bokudosserver.physics.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Box {
    private Point position;
    private Dimensions dimensions;
}
