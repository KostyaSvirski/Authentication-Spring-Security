package com.epam.esm.validator.realisation.sort;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldValidatorLinkTest {

    private FieldValidatorLink validator = new FieldValidatorLink();
    private String[] paramsToCheck = {"name_of_certificate", "create_date"};
    private String[] incParamsToCheck = {"", null, "asdfda"};

    @Test
    void testValidation() {
        for (String param : paramsToCheck) {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("field", param);
            assertTrue(validator.validate(paramMap));
        }
    }

    @Test
    void testValidationIncParams() {
        for (String param : incParamsToCheck) {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("field", param);
            assertFalse(validator.validate(paramMap));
        }
    }

}