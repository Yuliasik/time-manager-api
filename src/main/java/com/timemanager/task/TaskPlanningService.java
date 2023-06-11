package com.timemanager.task;

import com.timemanager.security.currentuser.CurrentUser;
import com.timemanager.task.calculated.CalculatedTask;
import com.timemanager.task.calculated.CalculatedTaskService;
import com.timemanager.user.UserRepository;
import com.timemanager.user.settings.SettingsService;
import com.timemanager.user.settings.UserSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskPlanningService {

  private final TaskRepository taskRepository;
  private final CalculatedTaskService calculatedTaskService;
  private final UserRepository userRepository;
  private final SettingsService settingsService;

  @Transactional
  public void planTasksByDays() {
    calculatedTaskService.clearAllOfCurrentUser();

    final UserSettings userSettings = settingsService.getAllSettingsOfCurrentUser();
    int durationHours = userSettings.getDurationHour();
    Duration durationPerDay = Duration.ofHours(durationHours);
    Integer bufferPercentage = userSettings.getBufferPercentage();

    List<Task> tasks = getTasksOfCurrentUser();

    PlanningTaskHandler taskPlanningService = new PlanningTaskHandler(tasks, durationPerDay, bufferPercentage);
    final List<CalculatedTask> calculatedTaskList = taskPlanningService.handlePlan();

    calculatedTaskService.saveAllForCurrentUser(calculatedTaskList);
  }

  private List<Task> getTasksOfCurrentUser() {
    final CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userRepository.getByUsername(user.getUsername()).getId();
    return taskRepository.findAllByUserIdFromToday(userId);
  }

}
