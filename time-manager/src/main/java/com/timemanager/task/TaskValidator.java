package com.timemanager.task;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TaskValidator {

    private static final String PREFIX = "Task contains validation errors: ";

    public String validate(Task task) {
        List<String> errors = new ArrayList<>();

        if (Objects.isNull(task)) {
            return PREFIX + "Task cannot be null";
        }

        if (Objects.isNull(task.getTitle())) {
            errors.add("Title cannot be null");
        } else if (task.getTitle().length() > 50) {
            errors.add("Length of title must be lower than or equals to 50");
        }

        if (Objects.nonNull(task.getDescription()) && task.getDescription().length() > 150) {
            errors.add("Length of description must be lower than 150");
        }

        if (Objects.isNull(task.getPerformanceDate())) {
            task.setPerformanceDate(LocalDate.parse(String.valueOf(LocalDate.now())));
        }

        if (Objects.isNull(task.getUserId())) {
            errors.add("User id cannot be null");
        }

        if (Objects.isNull(task.getState())) {
            task.setState(TaskState.PLANNED);
        }

        if (!errors.isEmpty()) {
            return errors.stream().collect(Collectors.joining(", ", PREFIX, "!"));
        }
        return "";
    }
}
