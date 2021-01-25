package com.bokudos.bokudosserver.packets.out;

import com.bokudos.bokudosserver.enums.AssetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlayerAsset {

    @Enumerated(EnumType.STRING)
    private AssetType assetType = AssetType.PLAYER;
    private boolean jumping;
    private boolean jumpUsed;

    private double x;
    private double y;
    private double dx;
    private double dy;
    private double width;
    private double height;
}
