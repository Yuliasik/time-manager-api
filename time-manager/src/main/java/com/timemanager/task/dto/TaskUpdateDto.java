package com.timemanager.task.dto;

import com.timemanager.task.TaskState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskUpdateDto {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String approximatePerformanceTime;
    private TaskState state;
}
