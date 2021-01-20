package com.bokudos.bokudosserver.external.stagebuilder.v1.service;

import com.bokudos.bokudosserver.external.stagebuilder.v1.data.RegionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class RegionService {

    @Value("${stagebuilder.url}")
    private String stageBuilderURL;

    @Autowired
    RestTemplate restTemplate;

    String getRegionEndpoint(int stageId) {
        return stageBuilderURL + "Region/" + stageId;
    }

    public List<RegionDTO> getRegions(int stageId) {
        ResponseEntity<RegionDTO[]> responseEntity = restTemplate.getForEntity(getRegionEndpoint(stageId), RegionDTO[].class);
        return responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null ? Arrays.asList(responseEntity.getBody()) : new ArrayList<>();
    }
}
