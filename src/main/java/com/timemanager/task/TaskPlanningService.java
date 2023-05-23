package com.timemanager.task;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timemanager.security.currentuser.CurrentUser;
import com.timemanager.user.UserRepository;
import com.timemanager.user.settings.SettingsService;
import com.timemanager.user.settings.UserSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskPlanningService {

    private static final LocalDate BACKLOG_DATE = LocalDate.EPOCH;

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SettingsService settingsService;

    public void planTasksByDays() {
        final UserSettings userSettings = settingsService.getAllSettingsByUserId();
        int durationHours = userSettings.getDurationHour();
        double bufferPercentage = userSettings.getBufferPercentage();
        Duration durationPerDay = calculateDuration(durationHours, bufferPercentage);

        final CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userRepository.getByUsername(user.getUsername()).getId();
        List<Task> all = taskRepository.findAllByUserIdPageableByNotesFromToday(userId);

        List<Task> tasks = prepareTasks(all);

        Map<LocalDate, List<Task>> tasksByDate = calculateTasksPerformanceDate(tasks, durationPerDay);
        saveTasksToDb(tasksByDate);
    }

    private Map<LocalDate, List<Task>> calculateTasksPerformanceDate(List<Task> tasks, Duration durationPerDay) {
        Map<LocalDate, List<Task>> tasksByDate = new HashMap<>();
        LocalDate iDate = LocalDate.now();
        do {
            List<Task> tasksToBeRemoved = new ArrayList<>();
            for (Task task : tasks) {
                if (iDate.isAfter(task.getEndDate())) {
                    addTaskToBacklogDate(tasksByDate, task);
                    tasksToBeRemoved.add(task);
                    continue;
                }
                if (taskShouldNotBeCalculated(task, iDate)) {
                    tasksToBeRemoved.add(task);
                    continue;
                }
                if (taskShouldBeSkipped(task, iDate)) {
                    continue;
                }
                Duration taskDuration = durationFromString(task.getApproximatePerformanceTime());
                if (tasksByDate.containsKey(iDate)) {
                    List<Task> taskList = tasksByDate.get(iDate);
                    Duration durationForTasks = calculateDurationForTasks(taskList).plus(taskDuration);

                    if (durationPerDay.compareTo(durationForTasks) >= 0) {
                        taskList.add(task);
                        tasksByDate.put(iDate, taskList);
                    } else if (taskAfterDate(task, iDate)) {
                        addTaskToBacklogDate(tasksByDate, task);
                    }
                } else {
                    if (durationPerDay.compareTo(taskDuration) >= 0) {
                        tasksByDate.put(iDate, new ArrayList<>(List.of(task)));
                    } else if (taskAfterDate(task, iDate)) {
                        addTaskToBacklogDate(tasksByDate, task);
                    }
                }
                tasksToBeRemoved.add(task);
            }
            tasks.removeAll(tasksToBeRemoved);
            iDate = iDate.plusDays(1);
        } while (!tasks.isEmpty());
        return tasksByDate;
    }

    private List<Task> prepareTasks(List<Task> all) {
        return new ArrayList<>(all.stream()
                .sorted(Comparator.comparing(Task::getEndDate)
                        .thenComparing(Task::getPriority)
                        .thenComparing(Task::getStartDate))
                .toList());
    }

    private Duration calculateDuration(int durationHours, double bufferPercentage) {
        double calculatedDurationHour = durationHours + (bufferPercentage / 100);
        int minutes = (int) ((calculatedDurationHour - (int) calculatedDurationHour) * 100);
        return Duration.ofHours(durationHours).plusMinutes(minutes);
    }

    private Duration calculateDurationForTasks(List<Task> tasks) {
        return tasks.stream()
                .map(Task::getApproximatePerformanceTime)
                .map(this::durationFromString)
                .reduce(Duration.ZERO, Duration::plus);
    }

    private Duration durationFromString(String duration) {
        String[] split = duration.split(":");
        return Duration.ofHours(Long.parseLong(split[0]))
                .plusMinutes(Long.parseLong(split[1]));
    }

    private boolean taskShouldNotBeCalculated(Task task, LocalDate iDate) {
        return !LocalDate.now().isEqual(iDate) && TaskState.COMPLETED.equals(task.getState());
    }

    private boolean taskShouldBeSkipped(Task task, LocalDate iDate) {
        return iDate.isBefore(task.getStartDate());
    }

    private boolean taskAfterDate(Task task, LocalDate iDate) {
        return task.getEndDate().equals(iDate);
    }

    private void addTaskToBacklogDate(Map<LocalDate, List<Task>> tasksMap, Task task) {
        if (tasksMap.containsKey(BACKLOG_DATE)) {
            tasksMap.get(BACKLOG_DATE).add(task);
        } else {
            tasksMap.put(BACKLOG_DATE, new ArrayList<>(List.of(task)));
        }
    }

    private void saveTasksToDb(Map<LocalDate, List<Task>> tasksMap) {
        List<Task> resultTasks = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Task>> entry : tasksMap.entrySet()) {
            entry.getValue().forEach(task -> task.setPerformanceDate(entry.getKey()));
            resultTasks.addAll(entry.getValue());
        }
        taskRepository.saveAll(resultTasks);
    }

}
