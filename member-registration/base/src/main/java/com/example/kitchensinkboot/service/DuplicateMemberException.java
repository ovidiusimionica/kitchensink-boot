package com.example.kitchensinkboot.service;

import jakarta.validation.ValidationException;
import java.io.Serial;

public class DuplicateMemberException extends ValidationException {

  @Serial
  private static final long serialVersionUID = -7034897190745766939L;

  public DuplicateMemberException() {
    super("Email taken");
  }

}
