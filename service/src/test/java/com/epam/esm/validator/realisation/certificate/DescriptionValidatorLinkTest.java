package com.epam.esm.validator.realisation.certificate;

import com.epam.esm.dto.GiftCertificateDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DescriptionValidatorLinkTest {

    private DescriptionValidatorLink validator = new DescriptionValidatorLink();
    private String[] paramsToCheck = {"asdf", "as asdsad s", "-a-dcc1", "basbc - AAAAA", "CSAC", "      "};

    @Test
    void testValidation() {
        for (String param : paramsToCheck) {
            GiftCertificateDTO certificate = new GiftCertificateDTO();
            certificate.setDescription(param);
            assertTrue(validator.validate(certificate));
        }
    }

}