package com.example.kitchensinkboot.service;

import jakarta.validation.ValidationException;

public class DuplicateMemberException extends ValidationException {
    public DuplicateMemberException() {
        super("Email taken");
    }

}
