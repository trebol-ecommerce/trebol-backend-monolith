package org.trebol.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.trebol.annotation.validator.PhoneNumberValidator;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
	
	 String message() default "Phone number is not valid";

	 Class<?>[] groups() default {};

	 Class<? extends Payload>[] payload() default {};
}
