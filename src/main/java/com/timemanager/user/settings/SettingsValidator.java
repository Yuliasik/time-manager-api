package com.timemanager.user.settings;

import com.timemanager.validator.RestValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SettingsValidator implements RestValidator<UserSettings> {

  private static final String PREFIX = "Settings contains validation errors: ";

  public String validate(UserSettings userSettings) {
    List<String> errors = new ArrayList<>();

    if (Objects.isNull(userSettings)) {
      return PREFIX + "Settings cannot be null";
    }

    if (Objects.isNull(userSettings.getDurationHour())) {
      userSettings.setDurationHour(8);
    }

    if (userSettings.getDurationHour() > 24) {
      errors.add("Duration hours cannot be more than 24");
    }

    if (Objects.isNull(userSettings.getBufferPercentage())) {
      userSettings.setBufferPercentage(10);
    }

    if (userSettings.getBufferPercentage() > 100) {
      errors.add("Buffer percentage cannot be more than 100");
    }

    if (!errors.isEmpty()) {
      return errors.stream().collect(Collectors.joining(", ", PREFIX, "!"));
    }
    return "";
  }
}
