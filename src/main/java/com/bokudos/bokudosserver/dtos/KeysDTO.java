package com.bokudos.bokudosserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KeysDTO {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean attack;
}
