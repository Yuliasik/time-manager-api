package com.timemanager.task;

import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT DISTINCT(t.performanceDate) FROM Task t WHERE t.userId=:userId AND (t.performanceDate>=:date OR t.performanceDate = '1970-01-01') ORDER BY t.performanceDate ")
    List<LocalDate> findAllNotesByUserIdFromDate(Long userId, LocalDate date, Pageable pageable);

    @Query(value = "SELECT DISTINCT(t.performanceDate) FROM Task t WHERE t.userId=:userId AND (t.performanceDate>=:date OR t.performanceDate = '1970-01-01') ORDER BY t.performanceDate")
    List<LocalDate> findAllNotesByUserIdFromDate(Long userId, LocalDate date);

    List<Task> findAllByUserIdAndPerformanceDateIn(Long userId, List<LocalDate> performanceDates);

    default List<Task> findAllByUserIdPageableByNotesFromToday(Long userId, Pageable pageable) {
        final List<LocalDate> allNotesPageable = this.findAllNotesByUserIdFromDate(userId, LocalDate.now(), pageable);
        return this.findAllByUserIdAndPerformanceDateIn(userId, allNotesPageable);
    }

    default List<Task> findAllByUserIdPageableByNotesFromToday(Long userId) {
        final List<LocalDate> allNotesPageable = this.findAllNotesByUserIdFromDate(userId, LocalDate.now());
        return this.findAllByUserIdAndPerformanceDateIn(userId, allNotesPageable);
    }

}
