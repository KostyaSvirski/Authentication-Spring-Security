package com.epam.esm.validator.realisation.certificate;

import com.epam.esm.dto.GiftCertificateDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DurationValidatorLinkTest {

    private DurationValidatorLink validator = new DurationValidatorLink();
    private int[] paramsToCheck = {1, 2, 3, 4, 5};
    private int[] incParamsToCheck = {-1};

    @Test
    void testValidation() {
        for (int param : paramsToCheck) {
            GiftCertificateDTO certificate = new GiftCertificateDTO();
            certificate.setDuration(param);
            assertTrue(validator.validate(certificate));
        }
    }

    @Test
    void testValidationIncParams() {
        for (int param : incParamsToCheck) {
            GiftCertificateDTO certificate = new GiftCertificateDTO();
            certificate.setDuration(param);
            assertFalse(validator.validate(certificate));
        }
    }

}