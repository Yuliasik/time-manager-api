package com.timemanager.task.calculated;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalculatedTaskRepository extends JpaRepository<CalculatedTask, Long> {

  void deleteAllByUserId(Long userId);

  @Query(value = """
      SELECT DISTINCT(t.performanceDate)
      FROM CalculatedTask t
      WHERE t.userId=:userId AND (t.performanceDate>=:date OR t.performanceDate = '1970-01-01')
      ORDER BY t.performanceDate""")
  List<LocalDate> findAllNotesByUserIdFromDate(Long userId, LocalDate date, Pageable pageable);

  List<CalculatedTask> findAllByUserIdAndPerformanceDateIn(Long userId, List<LocalDate> performanceDates);

  default List<CalculatedTask> findAllByUserIdPageableByNotesFromToday(Long userId, Pageable pageable) {
    final List<LocalDate> allNotesPageable = this.findAllNotesByUserIdFromDate(userId, LocalDate.now(), pageable);
    return this.findAllByUserIdAndPerformanceDateIn(userId, allNotesPageable);
  }


}
