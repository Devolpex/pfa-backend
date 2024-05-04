package com.pfa.pfabackend.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.lang.annotation.*;

@Pattern.List({
    @Pattern(regexp = "(?=.*[0-9]).+", message = "{constraints.password.digit}"),
    @Pattern(regexp = "(?=.*[a-z]).+", message = "{constraints.password.lowercase}"),
    @Pattern(regexp = "(?=.*[A-Z]).+", message = "{constraints.password.uppercase}"),
    @Pattern(regexp = "(?=.*[@#$%^&+=]).+", message = "{constraints.password.special}")
})
@Size(min = 8, message = "{constraints.password.size}")
@ReportAsSingleViolation
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    String message() default "Invalid password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
