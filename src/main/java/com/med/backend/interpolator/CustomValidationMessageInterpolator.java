package com.med.backend.interpolator;

import com.med.backend.exception.CustomValidationException;
import graphql.GraphQLError;
import graphql.validation.interpolation.MessageInterpolator;
import graphql.validation.rules.ValidationEnvironment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomValidationMessageInterpolator implements MessageInterpolator {

    @Override
    public GraphQLError interpolate(String s, Map<String, Object> map, ValidationEnvironment validationEnvironment) {
        String argumentName = validationEnvironment.getArgument().getName();
        String message = String.format(s, argumentName);
        return new CustomValidationException(message);
    }
}
