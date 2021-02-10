package com.bokudos.bokudosserver.external.stagebuilder.v1.service;

import com.bokudos.bokudosserver.external.stagebuilder.v1.data.StageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class StageService {

    @Value("${stagebuilder.url}")
    private String stageBuilderURL;

    @Autowired
    RestTemplate restTemplate;

    String getStageEndpoint(int stageId) {
        return stageBuilderURL + "Stage/" + stageId;
    }

    public StageDTO getStage(int stageId) {
        ResponseEntity<StageDTO> responseEntity = restTemplate.getForEntity(getStageEndpoint(stageId), StageDTO.class);
        return responseEntity.getStatusCode() == HttpStatus.OK ? responseEntity.getBody() : null;
    }

}
