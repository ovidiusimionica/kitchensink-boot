package com.example.kitchensinkboot.web;

import com.example.kitchensinkboot.MemberRegistrationConfiguration;
import com.example.kitchensinkboot.service.MemberRegistrationService;
import com.example.kitchensinkboot.web.jsf.MemberJsf;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration
@WebMvcTest
@Import(MemberRegistrationConfiguration.class)
@MockBean(
    value = {
        MemberRegistrationService.class,
        MemberJsf.class
    }
)
public @interface KitchenSinkWebTest {

  @AliasFor(annotation = ContextConfiguration.class, attribute = "classes")
  Class<?>[] value() default {};

}
