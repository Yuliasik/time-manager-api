package com.timemanager.task;

import com.timemanager.task.calculated.CalculatedTask;
import com.timemanager.utils.DurationUtils;
import com.timemanager.utils.TaskWagesUtils;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanningTaskHandler {

  private static final LocalDate BACKLOG_DATE = LocalDate.EPOCH;
  private static final Comparator<Task> COMPARATOR_BY_WAGES =
      Comparator.comparingDouble(TaskWagesUtils::calculateWagesOfTask);

  private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

  private final Map<LocalDate, List<CalculatedTask>> tasksByDate = new HashMap<>();
  private final List<Long> taskIdsToBeRemoved = new ArrayList<>();
  private LocalDate iDate = LocalDate.now();
  private List<Task> preparedTasks;
  private Long lastDividedTaskId;

  private final Duration durationPerDay;

  public PlanningTaskHandler(List<Task> tasks, Duration durationPerDay, Integer bufferPercentage) {
    this.preparedTasks = prepareTasks(tasks);
    this.durationPerDay = DurationUtils.calculateDurationBuffer(durationPerDay, bufferPercentage);
  }

  public List<CalculatedTask> handlePlan() {
    Map<LocalDate, List<CalculatedTask>> resultMap = calculateTasksPerformanceDate();
    return mapToList(resultMap);
  }

  private List<Task> prepareTasks(List<Task> all) {
    return new ArrayList<>(all.stream()
        .sorted(COMPARATOR_BY_WAGES.reversed())
        .toList());
  }

  private Map<LocalDate, List<CalculatedTask>> calculateTasksPerformanceDate() {
    do {
      for (Task task : preparedTasks) {
        if (taskShouldNotProceed(task)) {
          continue;
        }

        Duration taskDuration = DurationUtils.getTaskDuration(task);

        List<CalculatedTask> iDateTasks = tasksByDate.getOrDefault(iDate, new ArrayList<>());
        Duration iDateTasksDuration = DurationUtils.calculateDurationForTasks(iDateTasks);
        Duration iDateTasksWithCurrentDuration = iDateTasksDuration.plus(taskDuration);

        if (durationPerDay.compareTo(iDateTasksWithCurrentDuration) >= 0) {
          addTaskToResult(task, iDateTasks);
          taskIdsToBeRemoved.add(task.getId());
        } else if (!iDateTasksDuration.equals(durationPerDay)) {
          Task croppedTask = cropTask(task, iDateTasksDuration);
          if (croppedTask == null) {
            continue;
          }
          lastDividedTaskId = task.getId();
          addTaskToResult(croppedTask, iDateTasks);
          break;
        } else {
          break;
        }
      }
      removeAllTasks(taskIdsToBeRemoved);
      taskIdsToBeRemoved.clear();
      iDate = iDate.plusDays(1);
    } while (!preparedTasks.isEmpty());
    return tasksByDate;
  }

  private void removeAllTasks(List<Long> ids) {
    preparedTasks = new ArrayList<>(preparedTasks.stream()
        .filter(task -> !ids.contains(task.getId()))
        .toList());
  }

  private boolean taskShouldNotProceed(Task task) {
    if (iDate.isAfter(task.getEndDate())) {
      addTaskToBacklogDate(task);
      taskIdsToBeRemoved.add(task.getId());
      return true;
    }
    if (taskShouldBeSkipped(task)) {
      return true;
    }
    if (taskShouldNotBeCalculated(task)) {
      taskIdsToBeRemoved.add(task.getId());
      return true;
    }
    return false;
  }

  private void addTaskToBacklogDate(Task task) {
    CalculatedTask calculatedTask = taskMapper.toCalculatedTask(task);
    if (tasksByDate.containsKey(BACKLOG_DATE)) {
      tasksByDate.get(BACKLOG_DATE).add(calculatedTask);
    } else {
      tasksByDate.put(BACKLOG_DATE, new ArrayList<>(List.of(calculatedTask)));
    }
  }

  private boolean taskShouldBeSkipped(Task task) {
    return iDate.isBefore(task.getStartDate());
  }

  private boolean taskShouldNotBeCalculated(Task task) {
    return !LocalDate.now().isEqual(iDate) && TaskState.COMPLETED.equals(task.getState());
  }

  private Task cropTask(Task task, Duration iDateTasksDuration) {
    Duration durationLeft = getDurationLeftForIDate(iDateTasksDuration);
    if (durationLeft == null) {
      return null;
    }
    Duration minused = DurationUtils.getTaskDuration(task).minus(durationLeft);
    removeAllTasks(List.of(task.getId()));
    Task build = taskMapper.customCopy(task, DurationUtils.stringFromDuration(minused));
    preparedTasks.add(0, build);
    return taskMapper.customCopy(task, DurationUtils.stringFromDuration(durationLeft));
  }

  private Duration getDurationLeftForIDate(Duration iDateTasksDuration) {
    long minutesOfDay = DurationUtils.getFullMinutes(durationPerDay);
    long minutesOfTasks = DurationUtils.getMinutes(iDateTasksDuration);
    if (minutesOfDay <= minutesOfTasks) {
      return null;
    }
    final long minutesLeft = minutesOfDay - minutesOfTasks;
    return Duration.ofMinutes(minutesLeft);
  }

  private void addTaskToResult(Task task, List<CalculatedTask> iDateTasks) {
    CalculatedTask calculatedTask = taskMapper.toCalculatedTask(task);
    if (lastDividedTaskId != null && lastDividedTaskId.equals(task.getId())) {
      calculatedTask.setDivided(true);
    }
    iDateTasks.add(calculatedTask);
    tasksByDate.put(iDate, iDateTasks);
  }

  private List<CalculatedTask> mapToList(Map<LocalDate, List<CalculatedTask>> tasksMap) {
    List<CalculatedTask> calculatedTaskList = new ArrayList<>();
    tasksMap.forEach((key, value) -> value.forEach(calculatedTask -> {
      calculatedTask.setPerformanceDate(key);
      calculatedTaskList.add(calculatedTask);
    }));
    return calculatedTaskList;
  }

}
