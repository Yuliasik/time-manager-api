package com.timemanager.task;

import com.timemanager.exception.EntityNotFoundException;
import com.timemanager.exception.EntityValidationFailedException;
import lombok.RequiredArgsConstructor;
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
    private final TaskValidator taskValidator;
    private final TaskMapper taskMapper;

    /**
     * Method to get all {@link Task}.
     *
     * @return Map<LocalDate, Task> object and 200 Http status.
     */
    public Map<LocalDate, List<Task>> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId).stream()
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
        String error = taskValidator.validate(task);
        if (error.length() > 0) {
            throw new EntityValidationFailedException(error);
        }
        return taskRepository.save(task);
    }

    /**
     * Method to update task if exist or else throw errors.
     *
     * @param task {@link Task} to update.
     * @return updated {@link Task}
     */
    public Task update(Task task) {
        Long taskId = task.getId();
        if (taskId == null) {
            throw new EntityNotFoundException("Task", taskId);
        }
        String error = taskValidator.validate(task);
        if (error.length() > 0) {
            throw new EntityValidationFailedException(error);
        }
        return taskRepository.save(task);
    }

    /**
     * Method to delete a task by id.
     *
     * @param id of task to delete.
     */
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

}
