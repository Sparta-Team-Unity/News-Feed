package com.sparta.newsfeed.domain.dto;

import java.util.List;

public class WaitsResponseDto {
    List<WaitsDto> waits;


    public WaitsResponseDto(List<WaitsDto> waits) {
        this.waits = waits;
    }

}
