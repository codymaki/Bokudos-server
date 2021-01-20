package com.bokudos.bokudosserver.external.stagebuilder;

import com.bokudos.bokudosserver.external.stagebuilder.v1.data.RegionDTO;
import com.bokudos.bokudosserver.external.stagebuilder.v1.data.Tiles;
import com.bokudos.bokudosserver.external.stagebuilder.v1.mapper.TilesMapper;
import com.bokudos.bokudosserver.external.stagebuilder.v1.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StageBuilder {

    @Autowired
    private RegionService regionService;

    public Tiles getTiles(int stageId) {
        List<RegionDTO> regions = regionService.getRegions(stageId);
        return TilesMapper.mapFromRegions(regions);
    }
}
