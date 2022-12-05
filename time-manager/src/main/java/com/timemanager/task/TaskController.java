package com.timemanager.task;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller to handle API-operations with {@link Task} entity.
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * GET /task request handler to return all Task.
     *
     * @return Map<LocalDate, Task> object and 200 Http status.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<LocalDate, List<Task>>> getAllByUserId(
            @PageableDefault(size = 12) Pageable pageable
    ) {
        return ResponseEntity.ok(taskService.getAllByUserId(pageable));
    }

    /**
     * GET /{id} request handler to return Task by id.
     *
     * @param id - Task object id.
     * @return Task object and 200 Http status.
     */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    /**
     * POST /task request handler to create Task object.
     *
     * @param task - Task object to be created.
     * @return {@link Task} task
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody @NotNull TaskCreateDto task) {
        return taskService.create(task);
    }

    /**
     * PUT /task request handler to update Task object.
     * Should contain id field as identifier which Task object will be updated.
     *
     * @param task - Task object with modified fields to be updated.
     * @return updated Task object.
     */
    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Task update(@RequestBody @NotNull Task task) {
        return taskService.update(task);
    }

    /**
     * DELETE /{id} request handler to delete a task by id.
     *
     * @param id of task to delete.
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
