package com.timemanager.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to handle CRUD-operations with {@link Task}.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByUserId(Long userId);

}
