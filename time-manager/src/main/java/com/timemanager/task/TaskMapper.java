package com.timemanager.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Duration;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "approximatePerformanceTime",
            target = "approximatePerformanceTime",
            qualifiedByName = "mapStringToDuration")
    Task fromCreateDto(TaskCreateDto taskCreateDto);


    @Named("mapStringToDuration")
    static Duration mapStringToDuration(String approximatePerformanceTime) {
        final String[] time = approximatePerformanceTime.split(":");
        return Duration.ofDays(Long.parseLong(time[0])).plusMinutes(Long.parseLong(time[1]));
    }

}
