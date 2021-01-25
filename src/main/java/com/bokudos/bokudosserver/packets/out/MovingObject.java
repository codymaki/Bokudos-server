package com.bokudos.bokudosserver.packets.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MovingObject {
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    protected double width;
    protected double height;
}
