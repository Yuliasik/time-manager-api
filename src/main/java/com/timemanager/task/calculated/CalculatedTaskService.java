package com.timemanager.task.calculated;

import com.timemanager.security.currentuser.CurrentUser;
import com.timemanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CalculatedTaskService {

  private final CalculatedTaskRepository calculatedTaskRepository;
  private final UserRepository userRepository;

  public void clearAllOfCurrentUser() {
    final Long userId = getCurrentUserId();
    calculatedTaskRepository.deleteAllByUserId(userId);
  }

  public void saveAllForCurrentUser(List<CalculatedTask> tasks) {
    final Long userId = getCurrentUserId();
    tasks.forEach(task -> {
      task.setUserId(userId);
      calculatedTaskRepository.save(task);
    });
  }

  private Long getCurrentUserId() {
    final CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userRepository.getByUsername(user.getUsername()).getId();
  }

}
