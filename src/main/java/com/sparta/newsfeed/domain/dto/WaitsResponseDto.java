package com.sparta.newsfeed.domain.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class WaitsResponseDto {
    List<WaitsDto> waits;
    public WaitsResponseDto(List<WaitsDto> waits) {
        this.waits = waits;
    }

}
