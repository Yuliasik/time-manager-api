package com.timemanager.task;

import com.timemanager.exception.EntityNotFoundException;
import com.timemanager.security.currentuser.CurrentUser;
import com.timemanager.task.dto.TaskCreateDto;
import com.timemanager.task.dto.TaskUpdateDto;
import com.timemanager.task.dto.TaskUpdateStateDto;
import com.timemanager.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service layout to work with {@link TaskRepository}.
 */
@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;
  private final TaskPlanningService taskPlanningService;
  private final TaskValidator taskValidator;
  private final UserRepository userRepository;
  private final TaskMapper taskMapper;

  /**
   * Method to get all {@link Task}.
   *
   * @return Map<LocalDate, Task> object and 200 Http status.
   */
  public Map<LocalDate, List<Task>> getAllByUserId(Pageable pageable) {
    final CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return taskRepository.findAllByUserIdPageableByNotesFromToday(
            userRepository.getByUsername(user.getUsername()).getId(), pageable
        )
        .stream()
        .collect(Collectors.groupingBy(Task::getPerformanceDate));
  }

  /**
   * Method to get {@link Task} by id if exist or else throw an exception.
   *
   * @param id {@link Long} of task to get.
   * @return {@link Task} from the database.
   */
  public Task getById(Long id) {
    return taskRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Task", id)
    );
  }

  /**
   * Method to create a new task.
   *
   * @param taskCreateDto {@link TaskCreateDto} to create.
   * @return created {@link Task}.
   */
  public Task create(TaskCreateDto taskCreateDto) {
    final Task task = taskMapper.fromCreateDto(taskCreateDto);
    taskValidator.handleValidate(task);
    Task save = taskRepository.save(task);
    taskPlanningService.planTasksByDays();
    return save;
  }

  /**
   * Method to update task if exist or else throw errors.
   *
   * @param taskUpdateDto {@link TaskUpdateDto} to update.
   * @return updated {@link Task}
   */
  public Task update(TaskUpdateDto taskUpdateDto) {
    final Task task = taskMapper.fromUpdateDto(taskUpdateDto);
    Long taskId = task.getId();
    if (taskId == null) {
      throw new EntityNotFoundException("Task", taskId);
    }
    taskValidator.handleValidate(task);
    Task save = taskRepository.save(task);
    taskPlanningService.planTasksByDays();
    return save;
  }

  /**
   * Method to delete a task by id.
   *
   * @param id of task to delete.
   */
  public void delete(Long id) {
    taskRepository.deleteById(id);
    taskPlanningService.planTasksByDays();
  }

  /**
   * Method to change state of task.
   *
   * @param taskUpdateStateDto {@link TaskUpdateStateDto} to update.
   */
  public void changeState(TaskUpdateStateDto taskUpdateStateDto) {
    final Long id = taskUpdateStateDto.getId();
    final Task referenceById = taskRepository.getReferenceById(id);
    referenceById.setState(taskUpdateStateDto.getState());
    taskRepository.save(referenceById);
  }

}
