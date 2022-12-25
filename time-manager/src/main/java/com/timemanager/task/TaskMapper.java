package com.timemanager.task;

import com.timemanager.task.dto.TaskCreateDto;
import com.timemanager.task.dto.TaskUpdateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task fromCreateDto(TaskCreateDto taskCreateDto);

    Task fromUpdateDto(TaskUpdateDto taskUpdateDto);

}
