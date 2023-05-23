package com.timemanager.validator;

import com.timemanager.exception.EntityValidationFailedException;

public interface RestValidator<T> {

  default void handleValidate(T t) {
    String error = this.validate(t);
    if (error.length() > 0) {
      throw new EntityValidationFailedException(error);
    }
  }

  String validate(T t);

}
