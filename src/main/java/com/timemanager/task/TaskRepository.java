package com.timemanager.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository to handle CRUD-operations with {@link Task}.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            SELECT t
            FROM Task t
            WHERE t.userId=:userId AND (t.endDate>=:date)""")
    List<Task> findAllTasksByUserIdFromDate(Long userId, LocalDate date);

    default List<Task> findAllByUserIdFromToday(Long userId) {
        return findAllTasksByUserIdFromDate(userId, LocalDate.now());
    }

}
