package com.timemanager.utils;

import com.timemanager.task.Task;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskWagesUtils {
  private static final Double COEFFICIENT_OF_DURATION = 0.5;
  private static final Double COEFFICIENT_OF_DEADLINE = 1.0;
  private static final Integer MAX_PRIORITY_VALUE = 5;

  public static double calculateWagesOfTask(Task task) {
    Integer priority = task.getPriority();
    Duration duration = DurationUtils.durationFromString(task.getApproximatePerformanceTime());
    double wageOfDuration = getWageOfDuration(duration);
    double wageOfEndDate = getWageOfEndDate(task.getEndDate());
    return (MAX_PRIORITY_VALUE - priority)
        + (COEFFICIENT_OF_DEADLINE / wageOfEndDate)
        + (COEFFICIENT_OF_DURATION / wageOfDuration);
  }

  private static double getWageOfDuration(Duration duration) {
    long hours = DurationUtils.getHours(duration);
    long minutes = DurationUtils.getMinutes(duration);
    double value = 0.01;
    if (hours != 0) {
      value += hours;
    }
    if (minutes != 0) {
      value += minutes / 60.0;
    }
    return value;
  }

  private static double getWageOfEndDate(LocalDate endDate) {
    double daysToDeadline = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    if (daysToDeadline == 0) {
      daysToDeadline = 0.5;
    }
    return daysToDeadline;
  }

}
