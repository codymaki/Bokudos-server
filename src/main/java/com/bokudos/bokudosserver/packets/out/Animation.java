package com.bokudos.bokudosserver.packets.out;

import com.bokudos.bokudosserver.packets.out.enums.Action;
import com.bokudos.bokudosserver.packets.out.enums.Direction;
import com.bokudos.bokudosserver.packets.out.enums.Movement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Animation {

    private Direction direction;
    private Movement movement;
    private Action action;
    private int frame;
    private int ticks;
}
