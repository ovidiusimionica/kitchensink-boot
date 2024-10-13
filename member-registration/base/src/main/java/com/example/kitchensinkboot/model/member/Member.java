package com.example.kitchensinkboot.model.member;

import org.springframework.data.annotation.Id;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    private String id;

    @NotNull
    @Nonnull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String name;

    @NotNull
    @Nonnull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Nonnull
    @Digits(fraction = 0, integer = 12)
    @Size(min = 10, max = 12)
    private String phoneNumber;

}
