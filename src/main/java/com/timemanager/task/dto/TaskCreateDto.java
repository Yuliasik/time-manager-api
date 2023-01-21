package com.timemanager.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskCreateDto {

    private Long userId;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String approximatePerformanceTime;

}
