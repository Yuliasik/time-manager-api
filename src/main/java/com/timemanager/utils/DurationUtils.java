package com.timemanager.utils;

import com.timemanager.task.Task;
import com.timemanager.task.calculated.CalculatedTask;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DurationUtils {

  public static Duration calculateDurationForTasks(List<CalculatedTask> tasks) {
    return tasks.stream()
        .map(CalculatedTask::getApproximatePerformanceTime)
        .map(DurationUtils::durationFromString)
        .reduce(Duration.ZERO, Duration::plus);
  }

  public static Duration getTaskDuration(Task task) {
    return durationFromString(task.getApproximatePerformanceTime());
  }

  public static Duration durationFromString(String duration) {
    String[] split = duration.split(":");
    return Duration.ofHours(Long.parseLong(split[0]))
        .plusMinutes(Long.parseLong(split[1]));
  }

  public static String stringFromDuration(Duration duration) {
    final long hours = DurationUtils.getHours(duration);
    final long minutes = DurationUtils.getMinutes(duration) - (hours / 60);
    return hours + ":" + minutes;
  }

  public static Duration calculateDurationBuffer(Duration duration, Integer bufferPercentage) {
    double secondsBuffer = (double) duration.getSeconds() * (bufferPercentage / 100.0);
    return duration.minusSeconds((long) secondsBuffer);
  }

  public static long getFullMinutes(Duration duration) {
    return duration.getSeconds() / 60;
  }

  public static long getMinutes(Duration duration) {
    return duration.getSeconds() / 60 - (getHours(duration) * 60);
  }

  public static long getHours(Duration duration) {
    return duration.getSeconds() / 3600;
  }

}
