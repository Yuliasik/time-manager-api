package com.timemanager.task.dto;

import com.timemanager.task.TaskState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskUpdateStateDto {

    private Long id;
    private TaskState state;
    private String performanceTime;

}
