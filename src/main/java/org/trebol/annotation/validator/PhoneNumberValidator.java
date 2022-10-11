package org.trebol.annotation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.trebol.annotation.PhoneNumber;
import org.trebol.config.ValidationProperties;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
	
	@Autowired
	private ValidationProperties validationProperties;
	
	private Pattern pattern;	
	
	@Override
	public void initialize(PhoneNumber constraintAnnotation) {
		pattern = Pattern.compile(validationProperties.getPhoneNumberRegexp());			
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Matcher matcher = pattern.matcher(value);		
		return matcher.matches();
	}
}
