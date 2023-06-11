package com.timemanager.user.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<UserSettings, Long> {
  UserSettings findByUserId(Long id);
}
