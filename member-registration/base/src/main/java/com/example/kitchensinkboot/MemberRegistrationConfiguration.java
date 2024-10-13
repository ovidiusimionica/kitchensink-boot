package com.example.kitchensinkboot;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
    "com.example.kitchensinkboot.web",
    "com.example.kitchensinkboot.service",
    "jakarta.faces.context"
})
public class MemberRegistrationConfiguration {

}
