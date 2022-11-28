package com.timemanager.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Duration;
import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "startDate",
            target = "startDate",
            qualifiedByName = "mapStringToLocalDate")
    @Mapping(source = "endDate",
            target = "endDate",
            qualifiedByName = "mapStringToLocalDate")
    @Mapping(source = "approximatePerformanceTime",
            target = "approximatePerformanceTime",
            qualifiedByName = "mapStringToDuration")
    Task fromCreateDto(TaskCreateDto taskCreateDto);

    @Named("mapStringToLocalDate")
    static LocalDate mapStringToLocalDate(String date) {
        return LocalDate.parse(date.substring(0, date.indexOf("T")));
    }

    @Named("mapStringToDuration")
    static Duration mapStringToDuration(String approximatePerformanceTime) {
        final String[] time = approximatePerformanceTime.split(":");
        return Duration.ofDays(Long.parseLong(time[0])).plusMinutes(Long.parseLong(time[1]));
    }

}
