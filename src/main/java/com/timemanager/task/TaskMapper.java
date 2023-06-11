package com.timemanager.task;

import com.timemanager.task.calculated.CalculatedTask;
import com.timemanager.task.dto.TaskCreateDto;
import com.timemanager.task.dto.TaskUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

  Task fromCreateDto(TaskCreateDto taskCreateDto);

  Task fromUpdateDto(TaskUpdateDto taskUpdateDto);

  @Mapping(source = "task.id", target = "originalTaskId")
  CalculatedTask toCalculatedTask(Task task);

  @Mapping(target = "id", source = "task.id")
  @Mapping(target = "approximatePerformanceTime", source = "approximatePerformanceTime")
  Task customCopy(Task task, String approximatePerformanceTime);

}
